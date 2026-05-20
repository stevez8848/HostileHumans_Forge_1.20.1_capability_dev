package com.craftix.hostile_humans.mixin;

import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = WorldGenRegion.class, priority = 999)
public abstract class WorldGenRegionMix {

}