package com.craftix.hostile_humans.entity.ai.goal;

import com.craftix.hostile_humans.HumanUtil;
import com.craftix.hostile_humans.entity.entities.Human;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;

public class EatFoodGoal extends Goal {
    public final Human mob;

    public EatFoodGoal(Human mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return HumanUtil.canStartEating(mob);
    }

    @Override
    public boolean canContinueToUse() {
        return mob.isUsingItem() && mob.getHealth() < mob.getMaxHealth();
    }

    @Override
    public void start() {
        mob.startUsingItem(InteractionHand.OFF_HAND);
    }

    @Override
    public void stop() {
        if (mob.isUsingItem())
            mob.stopUsingItem();
        mob.heal(10);
    }
}
