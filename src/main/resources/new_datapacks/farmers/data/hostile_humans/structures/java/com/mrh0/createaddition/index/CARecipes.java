package com.mrh0.createaddition.index;

import java.util.function.Supplier;

import com.mrh0.createaddition.CreateAddition;
//import com.mrh0.createaddition.recipe.conditions.HasFluidTagCondition;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.CraftingHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CARecipes {
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreateAddition.MODID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, CreateAddition.MODID);

	private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<Recipe<?>>> register(String id) {
		return RECIPE_TYPES.register(id, () -> new RecipeType<>() {
			public String toString() {
				return id;
			}
		});
	}

//	public static final Supplier<RecipeType<RollingRecipe>> ROLLING_TYPE = register("rolling");
//	public static RegistryObject<RecipeSerializer<?>> ROLLING = SERIALIZERS.register("rolling", () ->
//			new SequencedAssemblyRollingRecipeSerializer(new RollingRecipeProcessingFactory()));

//	public static final Supplier<RecipeType<ChargingRecipe>> CHARGING_TYPE = register("charging");
//	public static final RegistryObject<RecipeSerializer<?>> CHARGING = SERIALIZERS.register("charging", ChargingRecipeSerializer::new);

//	public static final Supplier<RecipeType<LiquidBurningRecipe>> LIQUID_BURNING_TYPE = register("liquid_burning");
//	public static final RegistryObject<RecipeSerializer<?>> LIQUID_BURNING = SERIALIZERS.register("liquid_burning", LiquidBurningRecipeSerializer::new);

    public static void register(IEventBus event) {

    	SERIALIZERS.register(event);
		RECIPE_TYPES.register(event);

//        CraftingHelper.register(HasFluidTagCondition.Serializer.INSTANCE);
    }
}
