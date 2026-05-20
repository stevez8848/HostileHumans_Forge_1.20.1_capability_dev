package com.craftix.hostile_humans.compat;

import com.craftix.hostile_humans.HostileHumans;
import com.craftix.hostile_humans.entity.entities.Human;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Method;

public class EpicFightGuardAnimations {
    private static boolean initialized;
    private static boolean unavailable;
    private static Class<?> livingEntityPatchClass;
    private static Method getEntityPatchMethod;
    private static Method byKeyMethod;
    private static Method playAnimationSynchronizedMethod;

    public static void playPseudoGuard(Human human) {
        playAnimation(human, getGuardHitAnimations(human));
    }

    public static void playAnimation(Human human, String... animationIds) {
        if (!ModList.get().isLoaded("epicfight") || unavailable) {
            return;
        }

        try {
            init();
            Object patch = getEntityPatchMethod.invoke(null, human, livingEntityPatchClass);
            if (patch == null) {
                return;
            }

            for (String animationId : animationIds) {
                Object animation = byKeyMethod.invoke(null, animationId);
                if (animation != null) {
                    playAnimationSynchronizedMethod.invoke(patch, animation, 0.0F);
                    return;
                }
            }
        } catch (ReflectiveOperationException | RuntimeException exception) {
            unavailable = true;
            HostileHumans.LOGGER.warn("Failed to play Epic Fight pseudo guard animation for Human", exception);
        }
    }

    private static void init() throws ReflectiveOperationException {
        if (initialized) {
            return;
        }

        Class<?> capabilitiesClass = Class.forName("yesman.epicfight.world.capabilities.EpicFightCapabilities");
        livingEntityPatchClass = Class.forName("yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch");
        Class<?> animationManagerClass = Class.forName("yesman.epicfight.api.animation.AnimationManager");
        Class<?> assetAccessorClass = Class.forName("yesman.epicfight.api.asset.AssetAccessor");

        getEntityPatchMethod = capabilitiesClass.getMethod("getEntityPatch", Entity.class, Class.class);
        byKeyMethod = animationManagerClass.getMethod("byKey", String.class);
        playAnimationSynchronizedMethod = livingEntityPatchClass.getMethod("playAnimationSynchronized", assetAccessorClass, float.class);
        initialized = true;
    }

    private static String[] getGuardHitAnimations(Human human) {
        ItemStack mainHand = human.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack offHand = human.getItemBySlot(EquipmentSlot.OFFHAND);
        ResourceLocation mainId = ForgeRegistries.ITEMS.getKey(mainHand.getItem());
        boolean hasEpicFightExtra = ModList.get().isLoaded("epicfightx");
        String path = "biped/skill/guard_sword_hit";

        if (mainHand.getItem() instanceof SwordItem && offHand.getItem() instanceof SwordItem) {
            path = "biped/skill/guard_dualsword_hit";
        } else if (mainId != null && "epicfight".equals(mainId.getNamespace())) {
            String itemPath = mainId.getPath();
            if (itemPath.contains("spear")) {
                path = "biped/skill/guard_spear_hit";
            } else if (itemPath.contains("greatsword")) {
                path = "biped/skill/guard_greatsword_hit";
            } else if (itemPath.contains("longsword")) {
                path = "biped/skill/guard_longsword_hit";
            }
        }

        if (hasEpicFightExtra) {
            return new String[] {"epicfightx:" + path, "epicfight:" + path};
        }
        return new String[] {"epicfight:" + path};
    }
}
