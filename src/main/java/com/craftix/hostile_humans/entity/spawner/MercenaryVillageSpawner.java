package com.craftix.hostile_humans.entity.spawner;

import com.craftix.hostile_humans.HostileHumans;
import com.craftix.hostile_humans.entity.entities.Mercenary;
import com.craftix.hostile_humans.entity.entities.ModEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = HostileHumans.MOD_ID)
public class MercenaryVillageSpawner {
    private static final int SCAN_INTERVAL_TICKS = 20 * 20;
    private static final int VILLAGE_SCAN_RADIUS = 96;
    private static final int MIN_VILLAGE_POIS = 3;
    private static int scanTicker;

    private MercenaryVillageSpawner() {
    }

    @SubscribeEvent
    public static void handleServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || ++scanTicker < SCAN_INTERVAL_TICKS) {
            return;
        }
        scanTicker = 0;

        for (ServerLevel level : event.getServer().getAllLevels()) {
            for (ServerPlayer player : level.players()) {
                findVillageNear(level, player.blockPosition()).ifPresent(village -> ensureMercenaries(level, village));
            }
        }
    }

    private static Optional<VillageInfo> findVillageNear(ServerLevel level, BlockPos origin) {
        List<BlockPos> pois = level.getPoiManager()
                .getInRange(holder -> holder.is(PoiTypes.HOME), origin, VILLAGE_SCAN_RADIUS, PoiManager.Occupancy.ANY)
                .map(PoiRecord::getPos)
                .toList();
        if (pois.size() < MIN_VILLAGE_POIS) {
            return Optional.empty();
        }

        int x = 0;
        int y = 0;
        int z = 0;
        for (BlockPos pos : pois) {
            x += pos.getX();
            y += pos.getY();
            z += pos.getZ();
        }

        BlockPos center = new BlockPos(x / pois.size(), y / pois.size(), z / pois.size());
        int poiRadius = 0;
        for (BlockPos pos : pois) {
            poiRadius = Math.max(poiRadius, Mth.ceil(Math.sqrt(pos.distSqr(center))));
        }

        int patrolRadius = Mth.clamp(poiRadius + 10, 24, 72);
        int targetCount = 1 + Math.floorMod(center.getX() * 31 + center.getZ() * 17 + level.dimension().location().hashCode(), 6);
        return Optional.of(new VillageInfo(center, patrolRadius, targetCount));
    }

    private static void ensureMercenaries(ServerLevel level, VillageInfo village) {
        AABB villageBounds = new AABB(village.center()).inflate(village.patrolRadius() + 8.0D);
        int current = level.getEntitiesOfClass(Mercenary.class, villageBounds, mercenary -> mercenary.isAlive() && mercenary.isVillageMercenary()).size();
        int missing = village.targetCount() - current;
        if (missing <= 0) {
            return;
        }

        for (int i = 0; i < missing; i++) {
            spawnMercenary(level, village);
        }
    }

    private static void spawnMercenary(ServerLevel level, VillageInfo village) {
        for (int tries = 0; tries < 12; tries++) {
            int dx = level.random.nextInt(village.patrolRadius() * 2 + 1) - village.patrolRadius();
            int dz = level.random.nextInt(village.patrolRadius() * 2 + 1) - village.patrolRadius();
            if (dx * dx + dz * dz > village.patrolRadius() * village.patrolRadius()) {
                continue;
            }

            BlockPos candidate = village.center().offset(dx, 0, dz);
            BlockPos spawnPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, candidate);
            if (!level.hasChunkAt(spawnPos) || !level.getBlockState(spawnPos.below()).isValidSpawn(level, spawnPos.below(), ModEntityType.MERCENARY.get())) {
                continue;
            }

            Mercenary mercenary = ModEntityType.MERCENARY.get().create(level);
            if (mercenary == null) {
                return;
            }

            mercenary.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, level.random.nextFloat() * 360.0F, 0.0F);
            if (!level.noCollision(mercenary)) {
                mercenary.discard();
                continue;
            }

            DifficultyInstance difficulty = level.getCurrentDifficultyAt(spawnPos);
            mercenary.finalizeSpawn(level, difficulty, MobSpawnType.NATURAL, null, null);
            mercenary.setHomePos(village.center());
            mercenary.setVillagePatrol(village.patrolRadius());
            mercenary.setPersistenceRequired();
            level.addFreshEntity(mercenary);
            return;
        }
    }

    private record VillageInfo(BlockPos center, int patrolRadius, int targetCount) {
    }
}
