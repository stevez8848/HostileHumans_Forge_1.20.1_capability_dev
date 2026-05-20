package com.craftix.hostile_humans.mixin;

import net.minecraft.client.renderer.blockentity.StructureBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = StructureBlockRenderer.class, priority = 999)
public class StructureBlockRenderMix {

    @ModifyConstant(method = "getViewDistance", constant = @Constant(intValue = 96), require = 0)
    public int getRenderDistance(int value) {
        return 256;
    }
}