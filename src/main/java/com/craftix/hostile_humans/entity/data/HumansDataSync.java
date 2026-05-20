package com.craftix.hostile_humans.entity.data;

import com.craftix.hostile_humans.entity.HumanEntity;

import java.util.UUID;

public interface HumansDataSync {

    HumanEntity getSyncReference();

    boolean getDataSyncNeeded();

    void setDataSyncNeeded(boolean dirty);

    default boolean hasSyncReference() {
        return getSyncReference() != null;
    }

    default void syncData() {
        if (hasSyncReference()) {
            syncData(getSyncReference());
            setDataSyncNeeded(false);
        }
    }

    default void registerData() {
        if (hasSyncReference()) {
            registerData(getSyncReference());
            setDataSyncNeeded(false);
        }
    }

    default boolean syncDataIfNeeded() {
        if (getDataSyncNeeded()) {
            syncData();
            return true;
        }
        return false;
    }

    default void syncData(HumanEntity humanEntity) {
        HumanServerData serverData = getServerData();
        if (serverData != null) {
            serverData.updateOrRegisterHumanMob(humanEntity);
        }
    }

    default void registerData(HumanEntity humanEntity) {
        HumanServerData serverData = getServerData();
        if (serverData != null) {
            serverData.registerHumanMob(humanEntity);
        }
    }

    default HumanData getData(UUID uuid) {
        HumanServerData serverData = getServerData();
        if (serverData == null || uuid == null) {
            return null;
        }
        return serverData.getHumanMob(uuid);
    }

    default HumanServerData getServerData() {
        return HumanServerData.get();
    }
}
