package com.craftix.hostile_humans.entity.data;

import com.craftix.hostile_humans.HostileHumans;
import com.craftix.hostile_humans.entity.HumanEntity;
import com.craftix.hostile_humans.network.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber
public class HumanServerData extends SavedData {

    public static final String HUMAN_MOBS_TAG = "HumanMobs";

    private static ConcurrentHashMap<UUID, HumanData> humanMobEntitiesMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<UUID, Set<HumanData>> humanMobsPerPlayerMap = new ConcurrentHashMap<>();

    private static MinecraftServer server;
    private static HumanServerData data;

    public HumanServerData() {
        this.setDirty();
    }

    @SubscribeEvent
    public static void handleServerAboutToStartEvent(ServerAboutToStartEvent event) {
        humanMobEntitiesMap = new ConcurrentHashMap<>();
        humanMobsPerPlayerMap = new ConcurrentHashMap<>();
    }

    public static void prepare(MinecraftServer server) {

        if (server == null || server == HumanServerData.server && HumanServerData.data != null) {
            return;
        }

        HumanServerData.server = server;

        HumanServerData.data = server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(HumanServerData::load, HumanServerData::new, HumanServerData.getFileId());
    }

    public static void setData(HumanServerData data) {
        HumanServerData.data = data;
        HumanServerData.data.setDirty();
    }

    public static HumanServerData get() {
        if (HumanServerData.data == null) {
            prepare(ServerLifecycleHooks.getCurrentServer());
        }
        return HumanServerData.data;
    }

    public static String getFileId() {
        return HostileHumans.MOD_ID;
    }

    private static void addHuman(HumanData humanMobEntity) {
        humanMobEntitiesMap.put(humanMobEntity.getUUID(), humanMobEntity);
        UUID ownerUUID = humanMobEntity.getOwnerUUID();
        if (ownerUUID != null) {
            Set<HumanData> humanMobEntities = humanMobsPerPlayerMap.computeIfAbsent(ownerUUID, key -> ConcurrentHashMap.newKeySet());

            humanMobEntities.remove(humanMobEntity);
            humanMobEntities.add(humanMobEntity);
        }
    }

    private static void addHuman(CompoundTag compoundTag) {
        addHuman(new HumanData(compoundTag));
    }

    public static HumanServerData load(CompoundTag compoundTag) {

        HumanServerData humanMobEntitiesData = new HumanServerData();

        if (compoundTag.contains(HUMAN_MOBS_TAG)) {
            ListTag humanMobListTag = compoundTag.getList(HUMAN_MOBS_TAG, 10);
            for (int i = 0; i < humanMobListTag.size(); ++i) {
                addHuman(humanMobListTag.getCompound(i));
            }
        }

        return humanMobEntitiesData;
    }

    public HumanData getHumanMob(UUID humanMobUUID) {
        return humanMobEntitiesMap.get(humanMobUUID);
    }

    public Entity getHumanMobEntity(UUID humanMobUUID, ServerLevel serverLevel) {
        HumanData humanMobEntityData = getHumanMob(humanMobUUID);
        if (humanMobEntityData != null && serverLevel != null) {
            return serverLevel.getEntity(humanMobEntityData.getUUID());
        }
        return null;
    }

    public Set<HumanData> getHumanMobs(UUID ownerUUID) {
        return humanMobsPerPlayerMap.get(ownerUUID);
    }

    public Set<Entity> getHumanMobsEntity(UUID ownerUUID, ServerLevel serverLevel) {
        Set<Entity> result = new HashSet<>();
        Set<HumanData> humanMobEntitiesData = getHumanMobs(ownerUUID);
        if (humanMobEntitiesData != null) {
            for (HumanData humanMobEntityData : humanMobEntitiesData) {
                Entity entity = getHumanMobEntity(humanMobEntityData.getUUID(), serverLevel);
                if (entity != null) {
                    result.add(entity);
                }
            }
        }
        return result;
    }

    public void updateOrRegisterHumanMob(HumanEntity humanMobEntity) {
        if (humanMobEntitiesMap.get(humanMobEntity.getUUID()) == null) {
            registerHumanMob(humanMobEntity);
        } else {
            updateHumanMob(humanMobEntity);
        }
    }

    public HumanData updateHumanMob(HumanEntity entity) {
        HumanData humanData = humanMobEntitiesMap.get(entity.getUUID());
        if (humanData == null) {

            registerHumanMob(entity);
            return null;
        }

        humanData.load(entity);

        UUID ownerUUID = humanData.getOwnerUUID();
        if (ownerUUID != null) {
            Set<HumanData> humanDataSet = humanMobsPerPlayerMap.computeIfAbsent(ownerUUID, key -> ConcurrentHashMap.newKeySet());

            humanDataSet.remove(humanData);
            humanDataSet.add(humanData);
        }

        this.setDirty();

        syncHumanData(humanData);

        return humanData;
    }

    public void updateHumanData(HumanEntity humanMobEntity) {
        if (humanMobEntity.getId() > 1) {
            updateHumanMob(humanMobEntity);
        }
    }

    public HumanData registerHumanMob(HumanEntity humanMobEntity) {
        return registerHumanMob(humanMobEntity, false);
    }

    public HumanData registerHumanMob(HumanEntity humanMobEntity, boolean requiredOwner) {
        if (humanMobEntitiesMap.get(humanMobEntity.getUUID()) != null) {

            return humanMobEntitiesMap.get(humanMobEntity.getUUID());
        }
        if (requiredOwner && !humanMobEntity.hasOwner()) {

            return null;
        }

        HumanData humanMobEntity2 = new HumanData(humanMobEntity);
        addHuman(humanMobEntity2);
        this.setDirty();

        if (humanMobEntity2.hasOwner()) {
            syncHumanData(humanMobEntity2);
        }

        return humanMobEntity2;
    }

    public void syncHumanData(UUID ownerUUID) {
        if (ownerUUID == null) {
            return;
        }
        Set<HumanData> humanMobEntitiesData = getHumanMobs(ownerUUID);
        if (humanMobEntitiesData == null || humanMobEntitiesData.isEmpty()) {
            return;
        }

        CompoundTag data = exportHumanData(humanMobEntitiesData);
        NetworkHandler.updateHHFollowersData(ownerUUID, data);
    }

    public void syncHumanData(HumanData humanMobEntityData) {
        if (humanMobEntityData == null || !humanMobEntityData.hasOwner()) {
            return;
        }

        CompoundTag data = exportHumanData(humanMobEntityData);
        NetworkHandler.updateHHFollowerData(humanMobEntityData.getUUID(), humanMobEntityData.getOwnerUUID(), data);
    }

    private static CompoundTag exportHumanData(HumanData humanMobEntityData) {
        if (humanMobEntityData == null || humanMobEntityData.getUUID() == null || humanMobEntityData.getOwnerUUID() == null) {
            return null;
        }

        CompoundTag compoundTag = new CompoundTag();
        humanMobEntityData.saveMetaData(compoundTag);
        return compoundTag;
    }

    private static CompoundTag exportHumanData(Set<HumanData> humanMobEntitiesData) {
        if (humanMobEntitiesData == null || humanMobEntitiesData.isEmpty()) {
            return null;
        }

        CompoundTag compoundTag = new CompoundTag();
        ListTag humanMobListTag = new ListTag();

        for (HumanData humanMobEntity : humanMobEntitiesData) {
            if (humanMobEntity != null) {
                CompoundTag humanMobEntityCompoundTag = new CompoundTag();
                humanMobEntity.saveMetaData(humanMobEntityCompoundTag);
                humanMobListTag.add(humanMobEntityCompoundTag);
            }
        }
        compoundTag.put(HUMAN_MOBS_TAG, humanMobListTag);
        return compoundTag;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {

        ListTag humanMobListTag = new ListTag();
        for (HumanData humanMobEntity : humanMobEntitiesMap.values()) {
            if (humanMobEntity != null) {
                CompoundTag humanMobEntityCompoundTag = new CompoundTag();
                humanMobEntity.save(humanMobEntityCompoundTag);
                humanMobListTag.add(humanMobEntityCompoundTag);
            }
        }
        compoundTag.put(HUMAN_MOBS_TAG, humanMobListTag);

        return compoundTag;
    }
}
