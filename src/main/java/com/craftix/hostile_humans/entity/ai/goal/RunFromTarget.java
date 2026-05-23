package com.craftix.hostile_humans.entity.ai.goal;

import com.craftix.hostile_humans.Config;
import com.craftix.hostile_humans.HumanUtil;
import com.craftix.hostile_humans.entity.entities.Human;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class RunFromTarget extends Goal {
    protected final Human human;
    protected final float maxDist;
    protected final PathNavigation pathNav;
    protected final Predicate<LivingEntity> avoidPredicate;
    protected final Predicate<LivingEntity> predicateOnAvoidEntity;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;

    @Nullable
    protected Path path;
    Vec3 targetPos = null;
    boolean jump;
    private int retreatCooldown;
    private int healTicks;
    private boolean healingAtDistance;
    @Nullable
    private LivingEntity returnTarget;

    public RunFromTarget(Human p_25027_, float p_25029_, double p_25030_, double p_25031_) {
        this(p_25027_, (p_25052_) -> {
            return true;
        }, p_25029_, p_25030_, p_25031_, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
    }

    public RunFromTarget(Human p_25040_, Predicate<LivingEntity> p_25042_, float p_25043_, double p_25044_, double p_25045_, Predicate<LivingEntity> p_25046_) {
        this.human = p_25040_;
        this.avoidPredicate = p_25042_;
        this.maxDist = p_25043_;
        this.walkSpeedModifier = p_25044_;
        this.sprintSpeedModifier = p_25045_;
        this.predicateOnAvoidEntity = p_25046_;
        this.pathNav = p_25040_.getNavigation();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        if (retreatCooldown > 0) {
            retreatCooldown--;
            return false;
        }
        if (human.getOffhandItem().is(Items.TOTEM_OF_UNDYING))
            return false;

        if (!isRetreatHealth())
            return false;

        human.toAvoid = human.getTarget();
        returnTarget = human.toAvoid;
        if (human.toAvoid == null) {
            return false;
        }

        if (human.getRandom().nextFloat() >= Config.fleeChance.get()) {
            retreatCooldown = 20 * 8 + human.getRandom().nextInt(20 * 4);
            return false;
        }

        human.setTarget(null);
        healingAtDistance = false;
        healTicks = 0;

        if (!generatePathAwayFromAttacker()) {
            retreatCooldown = 20 * 8 + human.getRandom().nextInt(20 * 4);
            return false;
        }

        return true;
    }

    private boolean generatePathAwayFromAttacker() {

        Vec3 vec3 = null;
        for (int i = 0; i < 10; ++i) {
            vec3 = DefaultRandomPos.getPosAway(this.human, 30, 7, human.toAvoid.position());
            if (vec3 != null) {
                break;
            }
        }
        if (vec3 == null) {
            return false;
        } else if (human.distanceToSqr(vec3.x, vec3.y, vec3.z) < human.toAvoid.distanceToSqr(this.human)) {
            return false;
        } else {
            this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
            if (path != null) targetPos = vec3;
            return this.path != null;
        }
    }

    public boolean canContinueToUse() {
        if (human.toAvoid == null) {
            return false;
        }
        if (!isRetreatHealth() && !healingAtDistance) {
            return false;
        }
        if (human.toAvoid instanceof Player player && (player.isSpectator() || player.isCreative())) {
            return false;
        }

        if (healingAtDistance) {
            return healTicks < 20 * 8 && human.getHealth() < human.getMaxHealth() * 0.6F;
        }

        double distanceSqr = this.human.distanceToSqr(human.toAvoid);
        if (distanceSqr >= 20 * 20) {
            startHealingAtDistance();
            return true;
        }

        if (distanceSqr > 30 * 30) {
            return false;
        }

        if (!human.onGround()) {
            jump = true;
        } else if (jump) {
            jump = false;
            pathNav.stop();
            generatePathAwayFromAttacker();
            if (this.path != null) {
                this.pathNav.moveTo(this.path, this.sprintSpeedModifier);
            }
        }

        if (this.pathNav.isDone()) {
            if (distanceSqr >= 16 * 16) {
                startHealingAtDistance();
                return true;
            }
            if (generatePathAwayFromAttacker() && this.path != null) {
                this.pathNav.moveTo(this.path, this.sprintSpeedModifier);
                return true;
            }
            return false;
        }

        return true;
    }

    public void start() {
        this.human.setSprinting(true);
        this.pathNav.moveTo(this.path, this.sprintSpeedModifier);
    }

    public void stop() {
        //System.out.println("stop flee");
        human.isFleeing = false;
        human.setSprinting(false);
        human.onPlayerJumpCoolDown = 20;
        if (returnTarget != null && returnTarget.isAlive() && human.canAttack(returnTarget)) {
            human.setTarget(returnTarget);
        }
        human.toAvoid = null;
        returnTarget = null;
        healingAtDistance = false;
        healTicks = 0;
        retreatCooldown = 20 * 25 + human.getRandom().nextInt(20 * 10);
    }

    public void tick() {

        if (human.toAvoid == null) return;
        //System.out.println("tick flee");
        human.isFleeing = true;
        human.setTarget(null);
        if (healingAtDistance) {
            this.human.getNavigation().stop();
            this.human.setSprinting(false);
            healTicks++;
            if (healTicks % 20 == 0 && human.getHealth() < human.getMaxHealth()) {
                human.heal(2.0F);
            }
            return;
        }

        this.human.setSprinting(true);
        this.human.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
    }

    private boolean isRetreatHealth() {
        return human.getHealth() > 0.0F && human.getHealth() < human.getMaxHealth() * 0.3F;
    }

    private void startHealingAtDistance() {
        this.pathNav.stop();
        this.healingAtDistance = true;
        this.healTicks = 0;
    }
}
