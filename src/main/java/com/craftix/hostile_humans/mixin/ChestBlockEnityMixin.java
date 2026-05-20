package com.craftix.hostile_humans.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.craftix.hostile_humans.entity.entities.ChestExtension;

import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEnityMixin implements ChestExtension {
	@Shadow @Final private ContainerOpenersCounter openersCounter;

	@Override
	public ContainerOpenersCounter openersCounter() {
		return openersCounter;
	}
}
