package com.craftix.hostile_humans.entity.entities;

import com.craftix.hostile_humans.entity.HumanEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class Bandit extends Human {
    private static final String[] EPIC_FIGHT_SKILLS = new String[]{
            "epicfight:roll",
            "epicfight:step",
            "epicfight:guard",
            "epicfight:parrying",
            "epicfight:impact_guard",
            "epicfight:stamina_pillager",
            "epicfight:technician",
            "epicfight:swordmaster",
            "epicfight:berserker",
            "epicfight:heartpiercer",
            "epicfight:eviscerate",
            "epicfight:battojutsu",
            "epicfight:rushing_tempo",
            "epicfight:sweeping_edge",
            "epicfight:the_guillotine"
    };

    public Bandit(EntityType<? extends HumanEntity> entityType, Level level) {
        super(entityType, level, HumanTier.BANDIT);
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource damageSource, int lootingLevel, boolean recentlyHit) {
        RandomSource random = getRandom();
        dropIfPositive(Items.EMERALD, random.nextInt(2, 65));
        dropIfPositive(Items.GOLD_INGOT, random.nextInt(65));
        dropIfPositive(Items.COPPER_INGOT, random.nextInt(65));
        dropIfPositive(Items.DIAMOND, random.nextInt(65));
        dropIfPositive(Items.GOLDEN_APPLE, random.nextInt(5));
        dropIfPositive(Items.ENCHANTED_GOLDEN_APPLE, random.nextInt(3));
        dropIfPositive(Items.NETHERITE_INGOT, random.nextInt(3));

        if (ModList.get().isLoaded("epicfight")) {
            Item skillBook = ForgeRegistries.ITEMS.getValue(new ResourceLocation("epicfight:skillbook"));
            if (skillBook != null && skillBook != Items.AIR) {
                ItemStack stack = new ItemStack(skillBook);
                stack.getOrCreateTag().putString("skill", EPIC_FIGHT_SKILLS[random.nextInt(EPIC_FIGHT_SKILLS.length)]);
                spawnAtLocation(stack);
            }
        }
    }

    @Override
    protected void dropEquipment() {
        // Bandits use their own treasure pool instead of Human's random equipment/inventory drops.
    }

    private void dropIfPositive(Item item, int count) {
        if (count > 0) {
            spawnAtLocation(new ItemStack(item, count));
        }
    }
}
