package com.craftix.hostile_humans.compat;

import com.tiviacz.travelersbackpack.capability.CapabilityUtils;
import com.tiviacz.travelersbackpack.capability.entity.IEntityTravelersBackpack;
import com.tiviacz.travelersbackpack.init.ModItems;
import com.tiviacz.travelersbackpack.util.Reference;
import com.tiviacz.travelersbackpack.util.TimeUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Random;

import static com.craftix.hostile_humans.entity.entities.ModEntityType.*;
import static com.tiviacz.travelersbackpack.capability.CapabilityUtils.getEntityCapability;
import static com.tiviacz.travelersbackpack.init.ModItems.*;

public class TravelersBackpack {

    public static void apply() {
        Reference.COMPATIBLE_TYPE_ENTRIES.add(HUMAN1.get());
        Reference.COMPATIBLE_TYPE_ENTRIES.add(HUMAN2.get());
        Reference.COMPATIBLE_TYPE_ENTRIES.add(ROAMER.get());

        Reference.ALLOWED_TYPE_ENTRIES.add(HUMAN1.get());
        Reference.ALLOWED_TYPE_ENTRIES.add(HUMAN2.get());
        Reference.ALLOWED_TYPE_ENTRIES.add(ROAMER.get());
    }

    public static void applyDeath(LivingDeathEvent event) {
        if (Reference.COMPATIBLE_TYPE_ENTRIES.contains(event.getEntity().getType())) {
            if (CapabilityUtils.isWearingBackpack((LivingEntity) event.getEntity())) {
                LazyOptional<IEntityTravelersBackpack> cap = getEntityCapability((LivingEntity) event.getEntity());
                if (cap.isPresent()) {
                    if (((LivingEntity) event.getEntity()).getRandom().nextFloat() < 0.8)
                        cap.resolve().get().removeWearable();
                }
            }
        }
    }

    public static void apply(LivingEntity living) {
        int spawnChance = 3;

        LazyOptional<IEntityTravelersBackpack> cap = getEntityCapability(living);

        if (cap.isPresent()) {
            IEntityTravelersBackpack travelersBackpack = cap.resolve().get();

            if (living.getRandom().nextInt(0, spawnChance) == 0) {
                boolean isNether = false;
                Random rand = new Random(living.getRandom().nextLong());

                while (true) {
                    ItemStack backpack = isNether ?
                            ModItems.COMPATIBLE_NETHER_BACKPACK_ENTRIES.get(TimeUtils.randomInBetweenInclusive(rand, 0, ModItems.COMPATIBLE_NETHER_BACKPACK_ENTRIES.size() - 1)).getDefaultInstance() :
                            ModItems.COMPATIBLE_OVERWORLD_BACKPACK_ENTRIES.get(TimeUtils.randomInBetweenInclusive(rand, 0, ModItems.COMPATIBLE_OVERWORLD_BACKPACK_ENTRIES.size() - 1)).getDefaultInstance();

                    if (backpack.is(END_TRAVELERS_BACKPACK.get()))
                        continue;

                    if (!(living.getType() == HUMAN2.get()) && (backpack.is(NETHERITE_TRAVELERS_BACKPACK.get()) || backpack.is(DIAMOND_TRAVELERS_BACKPACK.get()) || backpack.is(GOLD_TRAVELERS_BACKPACK.get()) || backpack.is(EMERALD_TRAVELERS_BACKPACK.get()))) {
                        continue;
                    }

                    if (rand.nextFloat() < 0.5f) {
                        backpack = STANDARD_TRAVELERS_BACKPACK.get().getDefaultInstance();
                    }

                    backpack.getOrCreateTag().putInt("SleepingBagColor", DyeColor.values()[TimeUtils.randomInBetweenInclusive(rand, 0, DyeColor.values().length - 1)].getId());

                    travelersBackpack.setWearable(backpack);
                    travelersBackpack.synchronise();
                    break;
                }
            }
        }
    }
}
