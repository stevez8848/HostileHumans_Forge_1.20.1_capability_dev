package com.craftix.hostile_humans.entity.data;

import com.craftix.hostile_humans.network.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.Set;
import java.util.UUID;

public class HumansServerDataClientSync {

    protected HumansServerDataClientSync() {
    }

    public static void syncHumanData(HumanData humanMobEntityData) {
        if (humanMobEntityData == null || !humanMobEntityData.hasOwner()) {
            return;
        }

        CompoundTag data = exportHHFollowerData(humanMobEntityData);
        UUID humanMobEntityUUID = humanMobEntityData.getUUID();
        UUID ownerUUID = humanMobEntityData.getOwnerUUID();

        NetworkHandler.updateHHFollowerData(humanMobEntityUUID, ownerUUID, data);
    }

    public static void syncHumanData(UUID ownerUUID, Set<HumanData> humanDataSet) {
        if (ownerUUID == null || humanDataSet == null || humanDataSet.isEmpty()) {
            return;
        }
        CompoundTag data = export(humanDataSet);
        NetworkHandler.updateHHFollowersData(ownerUUID, data);
    }

    public static CompoundTag exportHHFollowerData(HumanData humanMobEntityData) {

        if (humanMobEntityData == null || humanMobEntityData.getUUID() == null || humanMobEntityData.getOwnerUUID() == null) {
            return null;
        }

        CompoundTag compoundTag = new CompoundTag();
        humanMobEntityData.saveMetaData(compoundTag);

        return compoundTag;
    }

    public static CompoundTag export(Set<HumanData> humanDataSet) {

        if (humanDataSet == null || humanDataSet.isEmpty()) {
            return null;
        }

        CompoundTag compoundTag = new CompoundTag();
        ListTag humanMobListTag = new ListTag();

        for (HumanData humanMobEntity : humanDataSet) {
            if (humanMobEntity != null) {
                CompoundTag humanMobEntityCompoundTag = new CompoundTag();
                humanMobEntity.saveMetaData(humanMobEntityCompoundTag);
                humanMobListTag.add(humanMobEntityCompoundTag);
            }
        }
        compoundTag.put(HumanServerData.HUMAN_MOBS_TAG, humanMobListTag);

        return compoundTag;
    }
}
