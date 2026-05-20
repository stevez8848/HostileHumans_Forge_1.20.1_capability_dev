package com.craftix.hostile_humans.mixin;

import com.craftix.hostile_humans.entity.entities.Human;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = PathNavigation.class)
public abstract class PathNavMix {

    @Shadow
    @Final
    protected Mob mob;

    @ModifyConstant(method = "recomputePath", constant = @Constant(longValue = 20L))
    private long injected(long constant) {
        if (mob instanceof Human)
            return 5L;
        return constant;
    }
}