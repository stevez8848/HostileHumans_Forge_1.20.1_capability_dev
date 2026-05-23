package com.craftix.hostile_humans.entity.entities;

import com.craftix.hostile_humans.HostileHumans;
import com.craftix.hostile_humans.HumanUtil;
import com.craftix.hostile_humans.compat.ImmersiveArmors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.util.RandomSource;

import static com.craftix.hostile_humans.HumanUtil.isRangedWeapon;
import static com.craftix.hostile_humans.entity.entities.HumanTier.LEVEL1;
import static com.craftix.hostile_humans.entity.entities.HumanTier.LEVEL2;

public class HumanInventoryGenerator {

    public static int[] tier1Armor = new int[]{0, 2, 3, 1};
    public static int[] tier2Armor = new int[]{3, 4};
    public static Item[] tier1Weapons = new Item[]{Items.IRON_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.STONE_SWORD, Items.CROSSBOW, Items.BOW};
    public static Item[] tier2Weapons = new Item[]{Items.DIAMOND_SWORD, Items.DIAMOND_AXE, Items.DIAMOND_SWORD, Items.DIAMOND_SWORD, Items.DIAMOND_AXE, Items.CROSSBOW, Items.BOW};
    public static Item[] rangedWeapons = new Item[]{Items.CROSSBOW, Items.BOW};
    public static Item[] extraWeapons = new Item[]{Items.IRON_SWORD, Items.STONE_SWORD};

    public static void generateInventory(Human human, boolean forceRanged) {
        if (human.getData() == null) {
            HostileHumans.LOGGER.warn("Missing data during inventory generation" + " " + human);
            human.discard();
            return;
        }
        int[] armorPick = tier1Armor;
        Item[] weaponPick = tier1Weapons;
        float enchantChance = 0.3f;
        float armorChance = 0.25f;
        RandomSource random = human.getRandom();
        HumanTier humanTier = human.getTier();

        switch (humanTier) {
            case RONIN -> {
                generateRoninInventory(human, random, false);
                return;
            }
            case SAMURAI1 -> {
                generateRoninInventory(human, random, false);
                return;
            }
            case SAMURAI2 -> {
                generateRoninInventory(human, random, true);
                return;
            }
            case BANDIT -> {
                generateBanditInventory(human, random);
                return;
            }
            case LEVEL2 -> {
                armorPick = tier2Armor;
                weaponPick = tier2Weapons;
                enchantChance = 1f;
                armorChance = 0.15f;
            }
            case ROAMER -> {
                enchantChance = 1f;
                armorChance = 0.2f;
                if (random.nextFloat() < 0.3f) {
                    armorPick = tier2Armor;
                }
                if (random.nextFloat() < 0.3f) {
                    weaponPick = tier2Weapons;
                }
            }
        }

        if (forceRanged) {
            weaponPick = rangedWeapons;
        }

        boolean dualSwords = !forceRanged && maybeEquipDualSwords(human, humanTier, random);
        if (!dualSwords) {
            ItemStack stack = pickMainHandWeapon(weaponPick, humanTier, random).getDefaultInstance();
            damage(human, stack);
            human.setItemSlot(EquipmentSlot.MAINHAND, stack);
        }

        if (!dualSwords && canUseOffhandDefense(human.getItemBySlot(EquipmentSlot.MAINHAND)) && random.nextFloat() < (humanTier == LEVEL2 ? 0.5f : ((humanTier == LEVEL1) ? 0.2f : 0.4f)) && !isRangedWeapon(human.getItemBySlot(EquipmentSlot.MAINHAND))) {

            if (random.nextFloat() < 0.15 && humanTier == LEVEL2) {
                human.setItemSlot(EquipmentSlot.OFFHAND, Items.TOTEM_OF_UNDYING.getDefaultInstance());
            } else {
                ItemStack shield = Items.SHIELD.getDefaultInstance();
                damage(human, shield);
                human.setItemSlot(EquipmentSlot.OFFHAND, shield);
            }
        }

        if (HumanUtil.isRangedWeapon(human.getItemBySlot(EquipmentSlot.MAINHAND))) {
            ItemStack extraWeapon = extraWeapons[random.nextInt(extraWeapons.length)].getDefaultInstance();
            damage(human, extraWeapon);
            extraWeapon.enchant(Enchantments.VANISHING_CURSE, 1);
            human.getData().setInventoryItem(0, extraWeapon);
        }

        int staticImmersivePick = -1;
        if (random.nextFloat() < 0.45 && ModList.get().isLoaded("immersive_armors")) {
            List<Integer> list = new ArrayList<>();
            for (int i : armorPick) {
                list.add(i);
            }
            list.addAll(Arrays.asList(5, 6));
            if (humanTier == LEVEL2) {
                list.add(7);
            }
            staticImmersivePick = list.get(random.nextInt(list.size()));

            //slight adjustments
            if ((staticImmersivePick == 3 || staticImmersivePick == 5) && random.nextFloat() < 0.25f) {
                staticImmersivePick = list.get(random.nextInt(list.size()));
            }

            if ((staticImmersivePick == 5) && random.nextFloat() < 0.05f) {
                staticImmersivePick = list.get(random.nextInt(list.size()));
            }
        }

        int pick = staticImmersivePick != -1 ? staticImmersivePick : armorPick[random.nextInt(armorPick.length)];

        int pickForChest = -1;

        var slots = EquipmentSlot.values();
        ArrayUtils.reverse(slots);// head to toes

        for (EquipmentSlot equipmentslot : slots) {
            if (equipmentslot.getType() == EquipmentSlot.Type.ARMOR) {

                if (humanTier != LEVEL2 && random.nextFloat() < armorChance && staticImmersivePick != -1) {
                    continue;
                }

                if (equipmentslot == EquipmentSlot.LEGS && pickForChest > pick) {
                    pick = pickForChest;
                }

                Item item = customGetEquipmentForSlot(equipmentslot, pick);
                if (staticImmersivePick != -1) {
                    item = ImmersiveArmors.getItemForSlot(equipmentslot, pick);
                }

                if (equipmentslot == EquipmentSlot.CHEST) {
                    pickForChest = pick;
                }

                if (item != null) {
                    human.setItemSlot(equipmentslot, damage(human, item.getDefaultInstance()));
                }
            }
        }

        //-Change how humans spawn with armor (Don't change immersive armor)
        //
        //1. Spawn with full set of armor
        //
        //2. Spawn with full set of armor but not helmet
        //
        //3. Spawn with full set of armor but random leggings

        if (staticImmersivePick == -1) {
            int randomValue = random.nextInt(3) + 1;
            switch (randomValue) {
                case 2 -> human.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                case 3 -> {
                    int pickOther = armorPick[random.nextInt(armorPick.length)];
                    Item item = customGetEquipmentForSlot(EquipmentSlot.LEGS, pickOther);
                    if (item != null)
                        human.setItemSlot(EquipmentSlot.LEGS, damage(human, item.getDefaultInstance()));
                }
            }
        }

        human.enchantGeneratedWeapon(random, enchantChance);

        for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
            if (equipmentslot.getType() == EquipmentSlot.Type.ARMOR) {
                human.enchantGeneratedArmor(random, enchantChance, equipmentslot);
            }
        }

        if (humanTier == LEVEL2 && random.nextFloat() < 0.05) {
            ItemStack tridentStack = Items.TRIDENT.getDefaultInstance();
            tridentStack.enchant(Enchantments.LOYALTY, 1);
            tridentStack.enchant(Enchantments.VANISHING_CURSE, 1);
            human.setItemSlot(EquipmentSlot.MAINHAND, tridentStack);
        }
    }

    private static void generateRoninInventory(Human human, RandomSource random, boolean armored) {
        ItemStack sword = pickRoninSword(random);

        if (random.nextFloat() < 0.3F) {
            sword.enchant(Enchantments.SHARPNESS, random.nextInt(1, Enchantments.SHARPNESS.getMaxLevel() + 1));
            sword.enchant(Enchantments.SWEEPING_EDGE, random.nextInt(1, Enchantments.SWEEPING_EDGE.getMaxLevel() + 1));
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            human.setItemSlot(slot, ItemStack.EMPTY);
        }

        human.setItemSlot(EquipmentSlot.MAINHAND, sword);
        human.setDropChance(EquipmentSlot.MAINHAND, 0.0F);

        if (armored) {
            equipArmorSet(human, random, tier2Armor, 0.75F, 0.7F);
        }
    }

    private static void generateBanditInventory(Human human, RandomSource random) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            human.setItemSlot(slot, ItemStack.EMPTY);
        }

        ItemStack dagger = pickBanditDagger(random);
        damage(human, dagger);
        if (random.nextFloat() < 0.8F) {
            dagger.enchant(Enchantments.SHARPNESS, random.nextInt(1, Enchantments.SHARPNESS.getMaxLevel() + 1));
            dagger.enchant(Enchantments.MOB_LOOTING, random.nextInt(1, Enchantments.MOB_LOOTING.getMaxLevel() + 1));
        }
        human.setItemSlot(EquipmentSlot.MAINHAND, dagger);
        human.setDropChance(EquipmentSlot.MAINHAND, 0.0F);

        if (random.nextFloat() < 0.3F) {
            equipArmorSet(human, random, tier1Armor, 0.3F, 0.3F);
        }
    }

    private static ItemStack pickBanditDagger(RandomSource random) {
        List<Item> daggers = new ArrayList<>();
        addIfPresent(daggers, "epicfight:iron_dagger", 3);
        addIfPresent(daggers, "epicfight:diamond_dagger", 2);
        addIfPresent(daggers, "epicfight:netherite_dagger", 1);

        if (daggers.isEmpty()) {
            return (random.nextFloat() < 0.2F ? Items.DIAMOND_SWORD : Items.IRON_SWORD).getDefaultInstance();
        }

        return daggers.get(random.nextInt(daggers.size())).getDefaultInstance();
    }

    private static void equipArmorSet(Human human, RandomSource random, int[] armorPick, float enchantChance, float omitHelmetChance) {
        int pick = armorPick[random.nextInt(armorPick.length)];
        int pickForChest = -1;

        var slots = EquipmentSlot.values();
        ArrayUtils.reverse(slots);
        for (EquipmentSlot slot : slots) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) {
                continue;
            }
            if (slot == EquipmentSlot.HEAD && random.nextFloat() < omitHelmetChance) {
                continue;
            }
            if (slot == EquipmentSlot.LEGS && pickForChest > pick) {
                pick = pickForChest;
            }

            Item item = customGetEquipmentForSlot(slot, pick);
            if (slot == EquipmentSlot.CHEST) {
                pickForChest = pick;
            }
            if (item != null) {
                human.setItemSlot(slot, damage(human, item.getDefaultInstance()));
                human.enchantGeneratedArmor(random, enchantChance, slot);
            }
        }
    }

    private static ItemStack pickRoninSword(RandomSource random) {
        List<Item> uchigatana = new ArrayList<>();
        List<Item> tachi = new ArrayList<>();
        List<Item> katana = new ArrayList<>();

        addIfPresent(uchigatana, "epicfight:iron_uchigatana", 1);
        addIfPresent(uchigatana, "epicfight:diamond_uchigatana", 1);
        addIfPresent(uchigatana, "epicfight:uchigatana", 1);
        addIfPresent(tachi, "epicfight:iron_tachi", 1);
        addIfPresent(tachi, "epicfight:diamond_tachi", 1);
        addIfPresent(tachi, "epicfight:tachi", 1);
        addIfPresent(katana, "epicfight:iron_katana", 1);
        addIfPresent(katana, "epicfight:diamond_katana", 1);
        addIfPresent(katana, "epicfight:katana", 1);

        List<Item> weapons = pickWeightedRoninWeaponGroup(random, uchigatana, tachi, katana);
        if (weapons == null || weapons.isEmpty()) {
            return (random.nextBoolean() ? Items.DIAMOND_SWORD : Items.IRON_SWORD).getDefaultInstance();
        }

        return weapons.get(random.nextInt(weapons.size())).getDefaultInstance();
    }

    private static List<Item> pickWeightedRoninWeaponGroup(RandomSource random, List<Item> uchigatana, List<Item> tachi, List<Item> katana) {
        int uchigatanaWeight = uchigatana.isEmpty() ? 0 : 4;
        int tachiWeight = tachi.isEmpty() ? 0 : 3;
        int katanaWeight = katana.isEmpty() ? 0 : 3;
        int totalWeight = uchigatanaWeight + tachiWeight + katanaWeight;
        if (totalWeight == 0) {
            return null;
        }

        int pick = random.nextInt(totalWeight);
        if (pick < uchigatanaWeight) {
            return uchigatana;
        }
        if (pick < uchigatanaWeight + tachiWeight) {
            return tachi;
        }
        return katana;
    }

    private static Item pickMainHandWeapon(Item[] vanillaWeapons, HumanTier humanTier, RandomSource random) {
        List<Item> weapons = new ArrayList<>(Arrays.asList(vanillaWeapons));

        if (humanTier == LEVEL2) {
            addIfPresent(weapons, "epicfight:diamond_spear", 2);
            addIfPresent(weapons, "epicfight:diamond_longsword", 3);
            addIfPresent(weapons, "epicfight:diamond_dagger", 3);
            addIfPresent(weapons, "epicfight:diamond_greatsword", 2);
            addIfPresent(weapons, "epicfight:iron_spear", 1);
            addIfPresent(weapons, "epicfight:iron_longsword", 1);
            addIfPresent(weapons, "epicfight:iron_dagger", 1);
            addIfPresent(weapons, "epicfight:iron_greatsword", 1);
        } else {
            addIfPresent(weapons, "epicfight:iron_spear", 2);
            addIfPresent(weapons, "epicfight:iron_longsword", 2);
            addIfPresent(weapons, "epicfight:iron_dagger", 2);
            addIfPresent(weapons, "epicfight:iron_greatsword", 1);
            addIfPresent(weapons, "epicfight:stone_spear", 1);
            addIfPresent(weapons, "epicfight:stone_longsword", 1);
            addIfPresent(weapons, "epicfight:stone_dagger", 1);
            addIfPresent(weapons, "epicfight:stone_greatsword", 1);
        }

        addIfPresent(weapons, "epicfight:glove", humanTier == LEVEL2 ? 3 : 2);
        return weapons.get(random.nextInt(weapons.size()));
    }

    private static boolean maybeEquipDualSwords(Human human, HumanTier humanTier, RandomSource random) {
        float chance = humanTier == LEVEL2 ? 0.24f : (humanTier == LEVEL1 ? 0.12f : 0.18f);
        if (random.nextFloat() >= chance) {
            return false;
        }

        Item mainSword = humanTier == LEVEL2 ? Items.DIAMOND_SWORD : Items.IRON_SWORD;
        Item offhandSword = humanTier == LEVEL2 && random.nextFloat() < 0.5f ? Items.DIAMOND_SWORD : Items.IRON_SWORD;
        if (humanTier == LEVEL1 && random.nextFloat() < 0.45f) {
            mainSword = Items.STONE_SWORD;
        }

        human.setItemSlot(EquipmentSlot.MAINHAND, damage(human, mainSword.getDefaultInstance()));
        human.setItemSlot(EquipmentSlot.OFFHAND, damage(human, offhandSword.getDefaultInstance()));
        return true;
    }

    private static void addIfPresent(List<Item> weapons, String itemId, int weight) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
        if (item == null || item == Items.AIR) {
            return;
        }

        for (int i = 0; i < weight; i++) {
            weapons.add(item);
        }
    }

    private static boolean canUseOffhandDefense(ItemStack mainHand) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(mainHand.getItem());
        if (itemId == null || !"epicfight".equals(itemId.getNamespace())) {
            return true;
        }

        String path = itemId.getPath();
        return !path.contains("greatsword") && !path.contains("longsword") && !path.equals("glove");
    }

    public static ItemStack damage(Human human, ItemStack inStack) {
        if (!inStack.isDamageableItem() || inStack.getMaxDamage() <= 1) {
            return inStack;
        }

        int damage = (int) (human.getRandom().nextInt(inStack.getMaxDamage() - 1) * 1.35f);
        if (damage > inStack.getMaxDamage()) {
            damage = inStack.getMaxDamage() - 1;
        }
        inStack.setDamageValue(damage);
        return inStack;
    }

    public static Item customGetEquipmentForSlot(EquipmentSlot slot, int level) {
        switch (slot) {
            case HEAD:
                if (level == 0) {
                    return Items.LEATHER_HELMET;
                } else if (level == 1) {
                    return Items.GOLDEN_HELMET;
                } else if (level == 2) {
                    return Items.CHAINMAIL_HELMET;
                } else if (level == 3) {
                    return Items.IRON_HELMET;
                } else if (level == 4) {
                    return Items.DIAMOND_HELMET;
                }
            case CHEST:
                if (level == 0) {
                    return Items.LEATHER_CHESTPLATE;
                } else if (level == 1) {
                    return Items.GOLDEN_CHESTPLATE;
                } else if (level == 2) {
                    return Items.CHAINMAIL_CHESTPLATE;
                } else if (level == 3) {
                    return Items.IRON_CHESTPLATE;
                } else if (level == 4) {
                    return Items.DIAMOND_CHESTPLATE;
                }
            case LEGS:
                if (level == 0) {
                    return Items.LEATHER_LEGGINGS;
                } else if (level == 1) {
                    return Items.GOLDEN_LEGGINGS;
                } else if (level == 2) {
                    return Items.CHAINMAIL_LEGGINGS;
                } else if (level == 3) {
                    return Items.IRON_LEGGINGS;
                } else if (level == 4) {
                    return Items.DIAMOND_LEGGINGS;
                }
            case FEET:
                if (level == 0) {
                    return Items.LEATHER_BOOTS;
                } else if (level == 1) {
                    return Items.GOLDEN_BOOTS;
                } else if (level == 2) {
                    return Items.CHAINMAIL_BOOTS;
                } else if (level == 3) {
                    return Items.IRON_BOOTS;
                } else if (level == 4) {
                    return Items.DIAMOND_BOOTS;
                }
            default:
                return null;
        }
    }
}
