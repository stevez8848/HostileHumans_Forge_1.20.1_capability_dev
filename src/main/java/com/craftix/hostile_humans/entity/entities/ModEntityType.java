package com.craftix.hostile_humans.entity.entities;

import com.craftix.hostile_humans.HostileHumans;
import com.craftix.hostile_humans.compat.TravelersBackpack;
import com.craftix.hostile_humans.entity.HumanEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityType {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HostileHumans.MOD_ID);
    public static final RegistryObject<EntityType<Human>> HUMAN1 = ENTITIES.register("human_tier1",
            () -> EntityType.Builder.<Human>of((entityEntityType, level) -> new Human(entityEntityType, level, HumanTier.LEVEL1), HumanEntity.CATEGORY)
                    .sized(0.6F, 1.8F).clientTrackingRange(16).build("human_tier1"));
    public static final RegistryObject<EntityType<Human>> HUMAN2 = ENTITIES.register("human_tier2",
            () -> EntityType.Builder.<Human>of((entityEntityType, level) -> new Human(entityEntityType, level, HumanTier.LEVEL2), HumanEntity.CATEGORY)
                    .sized(0.6F, 1.8F).clientTrackingRange(16).build("human_tier2"));
    public static final RegistryObject<EntityType<Human>> ROAMER = ENTITIES.register("human_roamer",
            () -> EntityType.Builder.<Human>of((entityEntityType, level) -> new Human(entityEntityType, level, HumanTier.ROAMER), HumanEntity.CATEGORY)
                    .sized(0.6F, 1.8F).clientTrackingRange(16).build("human_roamer"));
    public static final RegistryObject<EntityType<Ronin>> RONIN = ENTITIES.register("human_ronin",
            () -> EntityType.Builder.<Ronin>of(Ronin::new, HumanEntity.CATEGORY)
                    .sized(0.6F, 1.8F).clientTrackingRange(16).build("human_ronin"));
    public static final RegistryObject<EntityType<Samurai>> SAMURAI1 = ENTITIES.register("human_samurai1",
            () -> EntityType.Builder.<Samurai>of((entityType, level) -> new Samurai(entityType, level, HumanTier.SAMURAI1), HumanEntity.CATEGORY)
                    .sized(0.6F, 1.8F).clientTrackingRange(16).build("human_samurai1"));
    public static final RegistryObject<EntityType<Samurai>> SAMURAI2 = ENTITIES.register("human_samurai2",
            () -> EntityType.Builder.<Samurai>of((entityType, level) -> new Samurai(entityType, level, HumanTier.SAMURAI2), HumanEntity.CATEGORY)
                    .sized(0.6F, 1.8F).clientTrackingRange(16).build("human_samurai2"));
    public static final RegistryObject<EntityType<Bandit>> BANDIT = ENTITIES.register("human_bandit",
            () -> EntityType.Builder.<Bandit>of(Bandit::new, HumanEntity.CATEGORY)
                    .sized(0.6F, 1.8F).clientTrackingRange(16).build("human_bandit"));
    public static final RegistryObject<EntityType<Mercenary>> MERCENARY = ENTITIES.register("human_mercenary",
            () -> EntityType.Builder.<Mercenary>of(Mercenary::new, HumanEntity.CATEGORY)
                    .sized(0.6F, 1.8F).clientTrackingRange(16).build("human_mercenary"));
    public static final RegistryObject<EntityType<SpawnerEntity>> SPAWNER_ENTITY = ENTITIES.register("human_group",
            () -> EntityType.Builder.of(SpawnerEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(16).build("human_group"));

    protected ModEntityType() {

    }

    @SubscribeEvent
    public static void entityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(HUMAN1.get(), Human.createAttributes().build());
        event.put(HUMAN2.get(), Human.createAttributes().build());
        event.put(ROAMER.get(), Human.createAttributes().build());
        event.put(RONIN.get(), Human.createAttributes().build());
        event.put(SAMURAI1.get(), Human.createAttributes().build());
        event.put(SAMURAI2.get(), Human.createAttributes().build());
        event.put(BANDIT.get(), Human.createAttributes().build());
        event.put(MERCENARY.get(), Human.createAttributes().build());
        event.put(SPAWNER_ENTITY.get(), Human.createAttributes().build());

        if (ModList.get().isLoaded("travelersbackpack")) {
            TravelersBackpack.apply();
        }
    }
}
