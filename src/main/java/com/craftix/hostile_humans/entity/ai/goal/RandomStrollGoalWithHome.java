package com.craftix.hostile_humans.entity.ai.goal;

import com.craftix.hostile_humans.entity.entities.Human;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

public class RandomStrollGoalWithHome extends RandomStrollGoal {
    Human human;

    public RandomStrollGoalWithHome(Human human, double p_25742_, int p_25743_, boolean p_25744_) {
        super(human, p_25742_, p_25743_, p_25744_);
        this.human = human;
    }

    @Override
    protected Vec3 getPosition() {

        BlockPos homePos = human.getHomePos();
        if (homePos != null && homePos.distToCenterSqr(human.position()) > 10 * 10) {
            return new Vec3(homePos.getX(), homePos.getY(), homePos.getZ());
        }

        return super.getPosition();
    }
}