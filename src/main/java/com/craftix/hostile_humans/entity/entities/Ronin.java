package com.craftix.hostile_humans.entity.entities;

import com.craftix.hostile_humans.compat.EpicFightGuardAnimations;
import com.craftix.hostile_humans.entity.HumanEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class Ronin extends Human {
    private static final int SHEATH_IDLE_TICKS = 20 * 5;
    private static final String SHEATH_IDLE_TAG = "RoninSheathIdleTicks";
    private static final String SHEATHED_TAG = "RoninSheathed";

    private int sheathIdleTicks;
    private boolean sheathed;

    public Ronin(EntityType<? extends HumanEntity> entityType, Level level) {
        super(entityType, level, HumanTier.RONIN);
    }

    protected Ronin(EntityType<? extends HumanEntity> entityType, Level level, HumanTier tier) {
        super(entityType, level, tier);
    }

    @Override
    public void tick() {
        super.tick();
        tickSheathState();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean wasSheathed = this.sheathed && isUchigatanaLike(getMainHandItem());
        boolean result = super.doHurtTarget(entityIn);

        if (result) {
            if (wasSheathed && entityIn instanceof LivingEntity target && target.isAlive()) {
                performSheathedStrike(target);
            }

            this.sheathed = false;
            this.sheathIdleTicks = 0;
        }

        return result;
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        ItemStack sword = getMainHandItem();
        if (!sword.isEmpty()) {
            spawnAtLocation(sword.copy());
        }
        spawnAtLocation(new ItemStack(Items.EMERALD, 24));
        spawnAtLocation(new ItemStack(Items.COOKED_COD, 12));
    }

    @Override
    protected void dropEquipment() {
        // Ronin uses fixed custom drops instead of Human's random equipment/inventory drops.
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt(SHEATH_IDLE_TAG, this.sheathIdleTicks);
        compound.putBoolean(SHEATHED_TAG, this.sheathed);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.sheathIdleTicks = compound.getInt(SHEATH_IDLE_TAG);
        this.sheathed = compound.getBoolean(SHEATHED_TAG);
    }

    private void tickSheathState() {
        if (level().isClientSide || !isUchigatanaLike(getMainHandItem())) {
            this.sheathed = false;
            this.sheathIdleTicks = 0;
            return;
        }

        if (this.swinging || this.isUsingItem() || getTarget() != null && distanceToSqr(getTarget()) < 16.0D) {
            this.sheathed = false;
            this.sheathIdleTicks = 0;
            return;
        }

        if (this.sheathIdleTicks < SHEATH_IDLE_TICKS) {
            this.sheathIdleTicks++;
        }
        if (this.sheathIdleTicks >= SHEATH_IDLE_TICKS) {
            this.sheathed = true;
        }
    }

    private void performSheathedStrike(LivingEntity target) {
        float baseDamage = (float) getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
        target.hurt(damageSources().mobAttack(this), Math.max(2.0F, baseDamage * 0.75F));
        playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 0.75F + getRandom().nextFloat() * 0.25F);

        if (distanceToSqr(target) > 12.0D) {
            EpicFightGuardAnimations.playAnimation(this, "epicfightx:biped/skill/battojutsu_dash", "epicfight:biped/skill/battojutsu_dash");
        } else {
            EpicFightGuardAnimations.playAnimation(this, "epicfightx:biped/skill/battojutsu", "epicfight:biped/skill/battojutsu");
        }
    }

    private static boolean isUchigatanaLike(ItemStack stack) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null || !"epicfight".equals(itemId.getNamespace())) {
            return false;
        }

        String path = itemId.getPath();
        return path.contains("uchigatana") || path.contains("katana");
    }
}
