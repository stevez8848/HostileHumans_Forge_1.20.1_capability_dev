package com.craftix.hostile_humans.entity.ai.goal;

import com.craftix.hostile_humans.entity.HumanEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class TameItemGoal extends HumanGoal {

    private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(10.0D).ignoreLineOfSight();
    private final TargetingConditions targetingConditions;
    private final double speedModifier;
    private final Ingredient items;
    private final boolean canScare;
    @Nullable
    protected Player player;
    private double px;
    private double py;
    private double pz;
    private double pRotX;
    private double pRotY;
    private int calmDown;

    public TameItemGoal(HumanEntity humanEntity, double speedModifier) {
        super(humanEntity);
        this.speedModifier = speedModifier;
        this.items = Ingredient.of(humanEntity.getTameItem());
        this.canScare = true;
        this.targetingConditions = TEMP_TARGETING.copy().selector(this::shouldFollow);
    }

    private boolean shouldFollow(LivingEntity livingEntity) {
        return this.items.test(livingEntity.getMainHandItem()) || this.items.test(livingEntity.getOffhandItem());
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isTamable()) {
            return false;
        } else if (this.calmDown > 0) {
            --this.calmDown;
            return false;
        } else {
            this.player = this.mob.getNearestPlayer(this.targetingConditions);
            return this.player != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.mob.isTamable()) {
            return false;
        } else if (this.canScare()) {
            if (this.mob.distanceToSqr(this.player) < 36.0D) {
                if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.01D) {
                    return false;
                }

                if (Math.abs(this.player.getXRot() - this.pRotX) > 5.0D || Math.abs(this.player.getYRot() - this.pRotY) > 5.0D) {
                    return false;
                }
            } else {
                this.px = this.player.getX();
                this.py = this.player.getY();
                this.pz = this.player.getZ();
            }

            this.pRotX = this.player.getXRot();
            this.pRotY = this.player.getYRot();
        }

        return this.canUse();
    }

    protected boolean canScare() {
        return this.canScare;
    }

    @Override
    public void start() {
        this.px = this.player.getX();
        this.py = this.player.getY();
        this.pz = this.player.getZ();
    }

    @Override
    public void stop() {
        this.player = null;
        this.mob.getNavigation().stop();
        this.calmDown = reducedTickDelay(100);
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.player, this.mob.getMaxHeadYRot() + 20.0F, this.mob.getMaxHeadXRot());
        if (this.mob.distanceToSqr(this.player) < 6.25D) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.player, this.speedModifier);
        }
    }
}
