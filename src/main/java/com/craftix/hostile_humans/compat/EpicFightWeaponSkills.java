package com.craftix.hostile_humans.compat;

import com.craftix.hostile_humans.entity.entities.Human;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class EpicFightWeaponSkills {
    private static final float TRIGGER_CHANCE = 0.12F;

    public static boolean tryUseAdvancedSkill(Human human, Entity target) {
        if (human.level().isClientSide || !ModList.get().isLoaded("epicfight") || human.getEpicFightSkillCooldown() > 0) {
            return false;
        }
        if (!(target instanceof LivingEntity livingTarget) || !livingTarget.isAlive() || human.getRandom().nextFloat() >= TRIGGER_CHANCE) {
            return false;
        }

        SkillType skillType = getSkillType(human);
        if (skillType == SkillType.NONE) {
            return false;
        }

        human.setEpicFightSkillCooldown(80 + human.getRandom().nextInt(50));
        switch (skillType) {
            case SPEAR -> useSingleTargetSkill(human, livingTarget, "biped/skill/heartpiercer", 1.6F, 0.65D, 60);
            case LONGSWORD -> useArcSkill(human, livingTarget, "biped/combat/longsword_airslash", 1.0F, 3.2D, 0.45D, 30);
            case DAGGER -> useSingleTargetSkill(human, livingTarget, human.getRandom().nextBoolean() ? "biped/skill/eviscerate_first" : "biped/skill/eviscerate_second", 1.25F, 0.25D, 50);
            case GREATSWORD -> useArcSkill(human, livingTarget, human.getRandom().nextBoolean() ? "biped/skill/the_guillotine" : "biped/skill/steel_whirlwind", 1.45F, 3.6D, 0.9D, 45);
            case FIST -> useSingleTargetSkill(human, livingTarget, "biped/skill/relentless_combo", 1.05F, 0.5D, 35);
            case DUAL_SWORD -> useArcSkill(human, livingTarget, "biped/skill/dancing_edge", 0.9F, 3.0D, 0.35D, 25);
            case TACHI -> useSingleTargetSkill(human, livingTarget, human.getRandom().nextBoolean() ? "biped/skill/rushing_tempo1" : "biped/skill/rushing_tempo2", 1.4F, 0.8D, 45);
            case UCHIGATANA -> useSingleTargetSkill(human, livingTarget, human.distanceToSqr(livingTarget) > 12.0D ? "biped/skill/battojutsu_dash" : "biped/skill/battojutsu", 1.35F, 0.55D, 40);
        }
        return true;
    }

    private static SkillType getSkillType(Human human) {
        ItemStack mainHand = human.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack offHand = human.getItemBySlot(EquipmentSlot.OFFHAND);
        ResourceLocation mainId = ForgeRegistries.ITEMS.getKey(mainHand.getItem());

        if (mainHand.getItem() instanceof SwordItem && offHand.getItem() instanceof SwordItem) {
            return SkillType.DUAL_SWORD;
        }
        if (mainId == null || !"epicfight".equals(mainId.getNamespace())) {
            return SkillType.NONE;
        }

        String path = mainId.getPath();
        if (path.contains("spear")) {
            return SkillType.SPEAR;
        }
        if (path.contains("longsword")) {
            return SkillType.LONGSWORD;
        }
        if (path.contains("dagger")) {
            return SkillType.DAGGER;
        }
        if (path.contains("greatsword")) {
            return SkillType.GREATSWORD;
        }
        if (path.contains("tachi")) {
            return SkillType.TACHI;
        }
        if (path.contains("uchigatana") || path.contains("katana")) {
            return SkillType.UCHIGATANA;
        }
        if (path.equals("glove")) {
            return SkillType.FIST;
        }
        return SkillType.NONE;
    }

    private static void useSingleTargetSkill(Human human, LivingEntity target, String animationPath, float damageMultiplier, double knockback, int slowTicks) {
        playSkillAnimation(human, animationPath);
        dealBonusDamage(human, target, damageMultiplier);
        applySkillControl(human, target, knockback, slowTicks);
    }

    private static void useArcSkill(Human human, LivingEntity target, String animationPath, float damageMultiplier, double radius, double knockback, int slowTicks) {
        playSkillAnimation(human, animationPath);
        for (LivingEntity entity : human.level().getEntitiesOfClass(LivingEntity.class, human.getBoundingBox().inflate(radius, 1.0D, radius),
                entity -> entity != human && entity.isAlive() && !entity.isAlliedTo(human) && human.hasLineOfSight(entity))) {
            if (entity == target || human.getLookAngle().normalize().dot(entity.position().subtract(human.position()).normalize()) > 0.05D) {
                dealBonusDamage(human, entity, damageMultiplier);
                applySkillControl(human, entity, knockback, slowTicks);
            }
        }
    }

    private static void dealBonusDamage(Human human, LivingEntity target, float multiplier) {
        float baseDamage = (float) human.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
        target.hurt(human.damageSources().mobAttack(human), Math.max(1.0F, baseDamage * multiplier));
    }

    private static void applySkillControl(Human human, LivingEntity target, double knockback, int slowTicks) {
        if (slowTicks > 0) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slowTicks, 0), human);
        }
        if (knockback > 0.0D) {
            target.knockback(knockback, human.getX() - target.getX(), human.getZ() - target.getZ());
        }
        human.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.85F, 0.85F + human.getRandom().nextFloat() * 0.3F);
    }

    private static void playSkillAnimation(Human human, String path) {
        EpicFightGuardAnimations.playAnimation(human, "epicfightx:" + path, "epicfight:" + path);
    }

    private enum SkillType {
        NONE,
        SPEAR,
        LONGSWORD,
        DAGGER,
        GREATSWORD,
        FIST,
        DUAL_SWORD,
        TACHI,
        UCHIGATANA
    }
}
