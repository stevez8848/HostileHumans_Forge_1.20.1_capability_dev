package com.craftix.hostile_humans.mixin;

import com.tiviacz.travelersbackpack.util.Reference;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(value = Reference.class)
public abstract class ReferenceMix {

    @Shadow
    @Final
    public static final List<EntityType> COMPATIBLE_TYPE_ENTRIES = new ArrayList<>(Arrays.asList(
            EntityType.ENDERMAN,
            EntityType.PIGLIN,
            EntityType.SKELETON,
            EntityType.WITHER_SKELETON,
            EntityType.ZOMBIE
    ));
}