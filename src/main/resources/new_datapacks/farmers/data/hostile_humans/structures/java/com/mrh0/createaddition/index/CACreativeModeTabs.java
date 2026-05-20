package com.mrh0.createaddition.index;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import com.mrh0.createaddition.CreateAddition;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class CACreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateAddition.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.createaddition.main"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(CABlocks.ELECTRIC_MOTOR::asStack)
                    .displayItems(new RegistrateDisplayItemsGenerator())
                    .build());

    public static void register(IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
    }

    public static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        private List<Item> collectBlocks(DeferredHolder<CreativeModeTab, CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Block, Block> entry : CreateAddition.REGISTRATE.getAll(Registries.BLOCK)) {
                if (!CreateRegistrate.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get()
                        .asItem();
                if (item == Items.AIR)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
            return items;
        }

        private List<Item> collectItems(DeferredHolder<CreativeModeTab, CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();


            for (RegistryEntry<Item, Item> entry : CreateAddition.REGISTRATE.getAll(Registries.ITEM)) {
                if (!CreateRegistrate.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            return items;
        }

        private static void outputAll(CreativeModeTab.Output output, List<Item> items) {
            for (Item item : items) {
                output.accept(item);
            }
        }

        List<Item> exclude = List.of(CAItems.CAKE_BASE.get(), CAItems.CAKE_BASE_BAKED.get());

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
            List<Item> items = new LinkedList<>();
            items.addAll(collectBlocks(MAIN_TAB, (item) -> {
                if (item == CABlocks.DIGITAL_ADAPTER.asItem()) return !CreateAddition.CC_ACTIVE;
                return false;
            }));
            items.addAll(collectItems(MAIN_TAB, (item) -> exclude.contains(item)));

            outputAll(output, items);
        }
    }
}
