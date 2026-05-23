package com.craftix.hostile_humans.item;

import com.craftix.hostile_humans.HostileHumans;
import com.craftix.hostile_humans.entity.entities.ModEntityType;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HostileHumans.MOD_ID);
    public static final RegistryObject<Item> HUMAN1_SPAWN_EGG = ITEMS.register("human_tier1_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityType.HUMAN1, MapColor.COLOR_ORANGE.col,
                    MapColor.TERRACOTTA_WHITE.col,
                    new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> HUMAN2_SPAWN_EGG = ITEMS.register("human_tier2_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityType.HUMAN2, MapColor.COLOR_ORANGE.col,
                    MapColor.GOLD.col,
                    new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> ROAMER_SPAWN_EGG = ITEMS.register("human_roamer_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityType.ROAMER, MapColor.COLOR_ORANGE.col,
                    MapColor.COLOR_BLACK.col,
                    new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> RONIN_SPAWN_EGG = ITEMS.register("human_ronin_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityType.RONIN, MapColor.COLOR_RED.col,
                    MapColor.COLOR_BLACK.col,
                    new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> SAMURAI1_SPAWN_EGG = ITEMS.register("human_samurai1_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityType.SAMURAI1, MapColor.COLOR_RED.col,
                    MapColor.TERRACOTTA_WHITE.col,
                    new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> SAMURAI2_SPAWN_EGG = ITEMS.register("human_samurai2_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityType.SAMURAI2, MapColor.COLOR_RED.col,
                    MapColor.GOLD.col,
                    new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> BANDIT_SPAWN_EGG = ITEMS.register("human_bandit_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityType.BANDIT, MapColor.COLOR_BLACK.col,
                    MapColor.GOLD.col,
                    new Item.Properties().rarity(Rarity.EPIC)));
}
