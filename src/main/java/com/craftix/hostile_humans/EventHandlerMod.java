package com.craftix.hostile_humans;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = HostileHumans.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandlerMod {

    /**
     * Forge 1.20+ uses AddPackFindersEvent + Pack.readMetaAndCreate to register built-in datapacks.
     * We expose datapacks under /datapacks/* inside the mod jar.
     */
    @SubscribeEvent
    public static void addPacks(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA) {
            return;
        }

        registerBuiltinDatapack(event, "improved_humans");
        registerBuiltinDatapack(event, "hardcore_humans");

        if (ModList.get().isLoaded("epicfightx")) {
            registerBuiltinDatapack(event, "hostile_humans_efx", true);
        }
    }

    private static void registerBuiltinDatapack(AddPackFindersEvent event, String packName) {
        registerBuiltinDatapack(event, packName, false);
    }

    private static void registerBuiltinDatapack(AddPackFindersEvent event, String packName, boolean required) {
        Path resourcePath = ModList.get().getModFileById(HostileHumans.MOD_ID).getFile()
                .findResource("datapacks/" + packName);

        String packId = "builtin/" + HostileHumans.MOD_ID + "/" + packName;

        Pack pack = Pack.readMetaAndCreate(
                packId,
                Component.literal(packName),
                required,
                id -> new PathPackResources(id, resourcePath, false),
                PackType.SERVER_DATA,
                Pack.Position.TOP,
                PackSource.BUILT_IN
        );

        if (pack != null) {
            event.addRepositorySource(consumer -> consumer.accept(pack));
        } else {
            HostileHumans.LOGGER.warn("Failed to register builtin datapack: {}", packName);
        }
    }
}
