package com.craftix.hostile_humans.entity.ai.goal;

import java.util.EnumSet;

import com.craftix.hostile_humans.entity.entities.Human;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;

public class HumanFloatGoal extends Goal {
	private final Human mob;

	public HumanFloatGoal(Human p_25230_) {
		this.mob = p_25230_;
		this.setFlags(EnumSet.of(Goal.Flag.JUMP));
		p_25230_.getNavigation().setCanFloat(true);
	}

	public boolean canUse() {
		if (!this.mob.prefersToFloat()) return false;
		return this.mob.isInWater() && this.mob.getFluidHeight(FluidTags.WATER) > this.mob.getFluidJumpThreshold() || this.mob.isInLava();
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	public void tick() {
		if (this.mob.getRandom().nextFloat() < 0.8F) {
			this.mob.getJumpControl().jump();
		}

	}
}