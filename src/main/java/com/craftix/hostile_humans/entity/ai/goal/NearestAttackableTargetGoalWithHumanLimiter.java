//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.craftix.hostile_humans.entity.ai.goal;

import com.craftix.hostile_humans.Config;
import com.craftix.hostile_humans.HumanUtil;
import com.craftix.hostile_humans.entity.entities.Human;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Team;

import static com.craftix.hostile_humans.HumanUtil.greetings;
import static com.craftix.hostile_humans.HumanUtil.isLookingAtTarget;

public class NearestAttackableTargetGoalWithHumanLimiter<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    Human human;
    private int nextCombatGreetingTick;

    public NearestAttackableTargetGoalWithHumanLimiter(Human p_26060_, Class<T> p_26061_, boolean p_26062_) {
        super(p_26060_, p_26061_, p_26062_);

        human = p_26060_;
    }

    public boolean canContinueToUse() {
        LivingEntity target1 = this.mob.getTarget();
        if (target1 == null) {
            target1 = this.targetMob;
        }

        if (target1 == null) {
            return false;
        } else if (!this.mob.canAttack(target1)) {
            return false;
        } else {
            Team team = this.mob.getTeam();
            Team team1 = target1.getTeam();
            if (team != null && team1 == team) {
                return false;
            } else {
                double d0 = this.getFollowDistance();
                if (this.mob.distanceToSqr(target1) > d0 * d0) {
                    return false;
                } else {
                    //   if (this.mustSee) {
                    //       if (this.mob.getSensing().hasLineOfSight(target1)) {
                    //           this.unseenTicks = 0;
                    //       } else if (++this.unseenTicks > reducedTickDelay(this.unseenMemoryTicks)) {
                    //           return false;
                    //       }
                    //   }

                    this.mob.setTarget(target1);
                    if (target1 instanceof Player player) {
                        trySendCombatGreeting(player);
                    }
                    return true;
                }
            }
        }
    }

    public boolean canUse() {
        if (HumanUtil.isLowHp(mob)) return false;
        boolean usable = super.canUse();
        if (usable) {
            if (target instanceof Player player) {

                if (isLookingAtTarget(human, target)) {
                    human.isAlert = true;
                    var otherHumansOnTeam = human.level().getEntities(human, human.getBoundingBox().inflate(25), entity -> entity instanceof Human otherHuman && otherHuman.team.equals(human.team));
                    for (Entity otherHuman : otherHumansOnTeam) {
                        ((Human) otherHuman).isAlert = true;
                    }
                } else {
                    if (!human.isAlert) {
                        return false;
                    }
                }

                var targetters = player.level().getEntities(player, player.getBoundingBox().inflate(15), entity -> entity instanceof Human human && human.getTarget() == target && human != mob);
                if (targetters.size() >= Config.maxTargeting.get()) {
                    setTarget(null);
                    return false;
                }
            }
        }

        if (usable && !mob.getTags().contains("greeted") && Math.random() < Math.min(1.0D, Config.greetChance.get() * 5.0D)) {
            mob.addTag("greeted");
            if (target != null) {
                sendGreeting(target);
                nextCombatGreetingTick = mob.tickCount + 20 * (8 + mob.getRandom().nextInt(8));
            }
        }

        return usable;
    }

    private void trySendCombatGreeting(Player target) {
        if (mob.tickCount < nextCombatGreetingTick || Math.random() >= Math.min(1.0D, Config.greetChance.get() * 1.2D)) {
            return;
        }

        sendGreeting(target);
        nextCombatGreetingTick = mob.tickCount + 20 * (5 + mob.getRandom().nextInt(8));
    }

    private void sendGreeting(LivingEntity target) {
        String name = "";
        if (mob.hasCustomName()) {
            name = mob.getCustomName().getString();
        }

        if (name.isEmpty()) {
            name = "Human";
        }

        target.sendSystemMessage(Component.literal("<" + name + "> " + greetings[(int) (Math.random() * greetings.length)]));
    }
}
