package com.craftix.hostile_humans.compat;

import com.natamus.collective_common_forge.functions.EntityFunctions;
import com.natamus.villagernames_common_forge.util.Names;
import net.minecraft.world.entity.Entity;

public class CollectiveVillagerNames {

    public static void nameEntity(Entity e) {
        EntityFunctions.nameEntity(e, Names.getRandomName());
    }
}
