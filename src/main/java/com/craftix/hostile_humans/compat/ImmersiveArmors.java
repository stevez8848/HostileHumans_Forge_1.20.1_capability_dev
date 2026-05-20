package com.craftix.hostile_humans.compat;

import immersive_armors.Items;
import immersive_armors.item.ExtendedArmorMaterial;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.world.item.Items.IRON_HELMET;

public class ImmersiveArmors {

    public static Item getItemForSlot(EquipmentSlot equipmentSlot, int equipmentLevel) {
        Map<Integer, ExtendedArmorMaterial> items = new HashMap<>() {
            {
                this.put(0, Items.WOODEN_ARMOR);
                this.put(1, Items.WARRIOR_ARMOR);
                this.put(2, Items.WARRIOR_ARMOR);
                this.put(3, Items.DIVINE_ARMOR);
                this.put(4, Items.PRISMARINE_ARMOR);
                this.put(5, Items.SLIME_ARMOR);
                this.put(6, Items.WOODEN_ARMOR);
                this.put(7, Items.HEAVY_ARMOR);
            }
        };
        if (items.containsKey(equipmentLevel)) {
            String name = items.get(equipmentLevel).m_6082_();
            Supplier<?> supplier;
            switch (equipmentSlot) {
                case HEAD -> {
                    supplier = Items.items.get(name + "_helmet");
                    if (items.get(equipmentLevel) == Items.WARRIOR_ARMOR) {
                        return IRON_HELMET;
                    }
                }
                case CHEST -> supplier = Items.items.get(name + "_chestplate");
                case LEGS -> supplier = Items.items.get(name + "_leggings");
                case FEET -> supplier = Items.items.get(name + "_boots");
                default -> supplier = null;
            }

            if (supplier != null) {
                return (Item) supplier.get();
            }
        }
        return null;
    }
}
