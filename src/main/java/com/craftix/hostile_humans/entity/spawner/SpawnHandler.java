package com.craftix.hostile_humans.entity.spawner;

import com.craftix.hostile_humans.entity.entities.ModEntityType;
import com.craftix.hostile_humans.entity.entities.SpawnerEntity;
import com.craftix.hostile_humans.entity.entities.Human;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static net.minecraft.world.entity.Mob.checkMobSpawnRules;

@Mod.EventBusSubscriber(modid = com.craftix.hostile_humans.HostileHumans.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnHandler {

    protected SpawnHandler() {}

    public static void registerSpawnPlacements(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(ModEntityType.ROAMER.get(), SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkHumanSpawnRules);
            SpawnPlacements.register(ModEntityType.RONIN.get(), SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkHumanSpawnRules);
            SpawnPlacements.register(ModEntityType.SAMURAI1.get(), SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkHumanSpawnRules);
            SpawnPlacements.register(ModEntityType.SAMURAI2.get(), SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkHumanSpawnRules);
            SpawnPlacements.register(ModEntityType.BANDIT.get(), SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkHumanSpawnRules);
            SpawnPlacements.register(ModEntityType.MERCENARY.get(), SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkHumanSpawnRules);
            SpawnPlacements.register(ModEntityType.SPAWNER_ENTITY.get(), SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkSpawnerEntityRules);
        });
    }

    public static boolean checkSpawnerEntityRules(EntityType<SpawnerEntity> type, ServerLevelAccessor level, MobSpawnType reason,
                                                  BlockPos pos, RandomSource random) {
        if (random.nextInt(200) != 0) return false;
        return isBrightEnoughToSpawn(level, pos, random) && checkMobSpawnRules(type, level, reason, pos, random);
    }

    public static boolean checkHumanSpawnRules(EntityType<? extends Human> type, ServerLevelAccessor level, MobSpawnType reason,
                                              BlockPos pos, RandomSource random) {
        if (random.nextInt(200) != 0) return false;
        return isBrightEnoughToSpawn(level, pos, random) && checkMobSpawnRules(type, level, reason, pos, random);
    }

    public static boolean isBrightEnoughToSpawn(ServerLevelAccessor level, BlockPos pos, RandomSource random) {
        return level.getBrightness(LightLayer.SKY, pos) > 10;
    }
}
