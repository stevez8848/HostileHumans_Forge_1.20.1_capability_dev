package com.craftix.hostile_humans.entity.entities;

import com.craftix.hostile_humans.entity.HumanEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Samurai extends Ronin {
    public Samurai(EntityType<? extends HumanEntity> entityType, Level level, HumanTier tier) {
        super(entityType, level, tier);
    }
}
