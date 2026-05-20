package com.craftix.hostile_humans.entity.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HumansClientData {

    private static final ConcurrentHashMap<UUID, HumanData> humanMobEntitiesMap =
            new ConcurrentHashMap<>();

    public static void load(CompoundTag compoundTag) {
        if (compoundTag.contains(HumanServerData.HUMAN_MOBS_TAG)) {
            ListTag humanMobListTag = compoundTag.getList(HumanServerData.HUMAN_MOBS_TAG, 10);
            for (int i = 0; i < humanMobListTag.size(); ++i) {
                loadData(humanMobListTag.getCompound(i));
            }
        } else if (compoundTag.contains(HumanData.UUID_TAG)) {
            loadData(compoundTag);
        }
    }

    public static void loadData(CompoundTag compoundTag) {
        if (compoundTag != null) {
            UUID humanMobUUID = compoundTag.getUUID(HumanData.UUID_TAG);

            HumanData humanMobEntityData = humanMobEntitiesMap.get(humanMobUUID);
            if (humanMobEntityData != null) {

                humanMobEntityData.load(compoundTag);
            } else {

                HumanData humanMobEntity = new HumanData(compoundTag);
                loadData(humanMobEntity);
            }
        }
    }

    public static void loadData(HumanData humanMobEntity) {
        if (humanMobEntity != null) {
            humanMobEntitiesMap.put(humanMobEntity.getUUID(), humanMobEntity);
        }
    }
}
