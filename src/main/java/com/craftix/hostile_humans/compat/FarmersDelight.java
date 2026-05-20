package com.craftix.hostile_humans.compat;

import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.craftix.hostile_humans.HumanUtil.EDIBLE_ITEMS;
import static vectorwing.farmersdelight.common.registry.ModItems.*;

public class FarmersDelight {

    public static void addFoodItems() {
        var list = new java.util.ArrayList<>(List.of(EDIBLE_ITEMS));
        list.addAll(List.of(
                FRIED_EGG.get().getDefaultInstance(),
                APPLE_CIDER.get().getDefaultInstance(),
                MELON_JUICE.get().getDefaultInstance(),
                PUMPKIN_SLICE.get().getDefaultInstance(),
                CABBAGE_LEAF.get().getDefaultInstance(),
                MINCED_BEEF.get().getDefaultInstance(),
                BEEF_PATTY.get().getDefaultInstance(),
                COOKED_CHICKEN_CUTS.get().getDefaultInstance(),
                COOKED_BACON.get().getDefaultInstance(),
                COOKED_COD_SLICE.get().getDefaultInstance(),
                COOKED_SALMON_SLICE.get().getDefaultInstance(),
                COOKED_MUTTON_CHOPS.get().getDefaultInstance(),
                HAM.get().getDefaultInstance(),
                SMOKED_HAM.get().getDefaultInstance(),
                PIE_CRUST.get().getDefaultInstance(),
                APPLE_PIE.get().getDefaultInstance(),
                SWEET_BERRY_CHEESECAKE.get().getDefaultInstance(),
                CHOCOLATE_PIE.get().getDefaultInstance(),
                CAKE_SLICE.get().getDefaultInstance(),
                APPLE_PIE_SLICE.get().getDefaultInstance(),
                SWEET_BERRY_CHEESECAKE_SLICE.get().getDefaultInstance(),
                CHOCOLATE_PIE_SLICE.get().getDefaultInstance(),
                SWEET_BERRY_COOKIE.get().getDefaultInstance(),
                HONEY_COOKIE.get().getDefaultInstance(),
                MELON_POPSICLE.get().getDefaultInstance(),
                GLOW_BERRY_CUSTARD.get().getDefaultInstance(),
                FRUIT_SALAD.get().getDefaultInstance(),
                MIXED_SALAD.get().getDefaultInstance(),
                NETHER_SALAD.get().getDefaultInstance(),
                BARBECUE_STICK.get().getDefaultInstance(),
                EGG_SANDWICH.get().getDefaultInstance(),
                CHICKEN_SANDWICH.get().getDefaultInstance(),
                HAMBURGER.get().getDefaultInstance(),
                BACON_SANDWICH.get().getDefaultInstance(),
                MUTTON_WRAP.get().getDefaultInstance(),
                DUMPLINGS.get().getDefaultInstance(),
                STUFFED_POTATO.get().getDefaultInstance(),
                CABBAGE_ROLLS.get().getDefaultInstance(),
                SALMON_ROLL.get().getDefaultInstance(),
                COD_ROLL.get().getDefaultInstance(),
                KELP_ROLL.get().getDefaultInstance(),
                KELP_ROLL_SLICE.get().getDefaultInstance(),
                COOKED_RICE.get().getDefaultInstance(),
                BONE_BROTH.get().getDefaultInstance(),
                BEEF_STEW.get().getDefaultInstance(),
                CHICKEN_SOUP.get().getDefaultInstance(),
                VEGETABLE_SOUP.get().getDefaultInstance(),
                FISH_STEW.get().getDefaultInstance(),
                FRIED_RICE.get().getDefaultInstance(),
                PUMPKIN_SOUP.get().getDefaultInstance(),
                BAKED_COD_STEW.get().getDefaultInstance(),
                NOODLE_SOUP.get().getDefaultInstance(),
                BACON_AND_EGGS.get().getDefaultInstance(),
                PASTA_WITH_MEATBALLS.get().getDefaultInstance(),
                PASTA_WITH_MUTTON_CHOP.get().getDefaultInstance(),
                MUSHROOM_RICE.get().getDefaultInstance(),
                ROASTED_MUTTON_CHOPS.get().getDefaultInstance(),
                VEGETABLE_NOODLES.get().getDefaultInstance(),
                STEAK_AND_POTATOES.get().getDefaultInstance(),
                RATATOUILLE.get().getDefaultInstance(),
                SQUID_INK_PASTA.get().getDefaultInstance(),
                GRILLED_SALMON.get().getDefaultInstance(),
                ROAST_CHICKEN.get().getDefaultInstance(),
                STUFFED_PUMPKIN.get().getDefaultInstance(),
                HONEY_GLAZED_HAM.get().getDefaultInstance(),
                SHEPHERDS_PIE.get().getDefaultInstance()
        ));

        EDIBLE_ITEMS = list.toArray(new ItemStack[]{});
    }
}
