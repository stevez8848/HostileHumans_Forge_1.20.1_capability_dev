package com.craftix.hostile_humans.entity.entities;

import com.craftix.hostile_humans.entity.HumanEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class Mercenary extends Human {
    private static final String VILLAGE_MERCENARY_TAG = "VillageMercenary";
    private static final String PATROL_RADIUS_TAG = "MercenaryPatrolRadius";
    private static final int DEFAULT_PATROL_RADIUS = 42;

    private int patrolRadius = DEFAULT_PATROL_RADIUS;

    public Mercenary(EntityType<? extends HumanEntity> entityType, Level level) {
        super(entityType, level, HumanTier.MERCENARY);
    }

    public void setVillagePatrol(int patrolRadius) {
        this.patrolRadius = Math.max(20, patrolRadius);
        this.addTag(VILLAGE_MERCENARY_TAG);
    }

    public boolean isVillageMercenary() {
        return getTags().contains(VILLAGE_MERCENARY_TAG);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target instanceof Player ? null : target);
    }

    @Override
    public boolean canLootChests() {
        return false;
    }

    @Override
    public double getHomePatrolRadius() {
        return patrolRadius;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt(PATROL_RADIUS_TAG, patrolRadius);
        compoundTag.putBoolean(VILLAGE_MERCENARY_TAG, isVillageMercenary());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains(PATROL_RADIUS_TAG)) {
            patrolRadius = compoundTag.getInt(PATROL_RADIUS_TAG);
        }
        if (compoundTag.getBoolean(VILLAGE_MERCENARY_TAG)) {
            this.addTag(VILLAGE_MERCENARY_TAG);
        }
    }
}
