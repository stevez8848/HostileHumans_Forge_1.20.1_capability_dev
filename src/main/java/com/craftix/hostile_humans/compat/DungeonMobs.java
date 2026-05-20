package com.craftix.hostile_humans.compat;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import net.minecraft.world.entity.EntityType;

public class DungeonMobs {

    public static EntityType<?> getRedstoneGolem() {
        return ModEntityTypes.REDSTONE_GOLEM.get();
    }
}
