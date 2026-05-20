package com.craftix.hostile_humans.mixin;

import com.craftix.hostile_humans.entity.entities.Human;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {Zombie.class, AbstractSkeleton.class, Pillager.class, Vindicator.class, Illusioner.class, Evoker.class, Creeper.class})
public abstract class MonsterMobsMixin extends Monster {

    protected MonsterMobsMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Inject(method = "registerGoals", at = @At("RETURN"))
    private void injected(CallbackInfo ci) {
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Human.class, true));
    }
}