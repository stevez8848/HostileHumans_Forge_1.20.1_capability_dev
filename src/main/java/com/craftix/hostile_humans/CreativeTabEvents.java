package com.craftix.hostile_humans;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraft.world.item.CreativeModeTabs;

import com.craftix.hostile_humans.item.ModItems;

@Mod.EventBusSubscriber(modid = HostileHumans.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CreativeTabEvents {
    @SubscribeEvent
    public static void buildTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.HUMAN1_SPAWN_EGG.get());
            event.accept(ModItems.HUMAN2_SPAWN_EGG.get());
            event.accept(ModItems.ROAMER_SPAWN_EGG.get());
            event.accept(ModItems.RONIN_SPAWN_EGG.get());
            event.accept(ModItems.SAMURAI1_SPAWN_EGG.get());
            event.accept(ModItems.SAMURAI2_SPAWN_EGG.get());
            event.accept(ModItems.BANDIT_SPAWN_EGG.get());
            event.accept(ModItems.MERCENARY_SPAWN_EGG.get());
        }
    }
}
