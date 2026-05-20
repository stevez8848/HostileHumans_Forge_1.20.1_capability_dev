package com.craftix.hostile_humans.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class RandomStrollGoalFar extends RandomStrollGoal {

    public RandomStrollGoalFar(PathfinderMob p_25741_, double p_25742_, int p_25743_, boolean p_25744_) {
        super(p_25741_, p_25742_, p_25743_, p_25744_);
    }

    @Override
    protected Vec3 getPosition() {
        return DefaultRandomPos.getPos(this.mob, 10 * 3, 7 * 3);
    }
}