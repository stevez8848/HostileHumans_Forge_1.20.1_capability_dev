package com.mrh0.createaddition;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mrh0.createaddition.blocks.liquid_blaze_burner.LiquidBlazeBurnerBlock;
import com.mrh0.createaddition.commands.CCApiCommand;
import com.mrh0.createaddition.config.Config;
import com.mrh0.createaddition.index.CAArmInteractions;
import com.mrh0.createaddition.index.CABlockEntities;
import com.mrh0.createaddition.index.CABlocks;
import com.mrh0.createaddition.index.CACreativeModeTabs;
import com.mrh0.createaddition.index.CADamageTypes;
import com.mrh0.createaddition.index.CADisplaySources;
import com.mrh0.createaddition.index.CAEffects;
import com.mrh0.createaddition.index.CAFluids;
import com.mrh0.createaddition.index.CAItemProperties;
import com.mrh0.createaddition.index.CAItems;
import com.mrh0.createaddition.index.CAPartials;
import com.mrh0.createaddition.index.CARecipes;
import com.mrh0.createaddition.index.CASounds;
import com.mrh0.createaddition.network.EnergyNetworkPacket;
import com.mrh0.createaddition.network.ObservePacket;
import com.mrh0.createaddition.ponder.CAPonderPlugin;
import com.mrh0.createaddition.trains.schedule.CASchedule;
import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.utility.DistExecutor;

import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.network.simple.SimpleChannel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(CreateAddition.MODID)
public class CreateAddition {
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "createaddition";

    public static boolean IE_ACTIVE = false;
    public static boolean CC_ACTIVE = false;
    public static boolean AE2_ACTIVE = false;

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CreateAddition.MODID);

    private static final String PROTOCOL = "1";

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public CreateAddition(IEventBus modEventBus, Dist dist, ModContainer container) {
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::postInit);
        modEventBus.addListener(this::onRegister);
        //modEventBus.addGenericListener(RecipeSerializer.class, CARecipes::register);

        IEventBus eventBus = modEventBus;
        NeoForge.EVENT_BUS.register(this);

        container.registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("createaddition-common.toml"));

        IE_ACTIVE = ModList.get().isLoaded("immersiveengineering");
        CC_ACTIVE = ModList.get().isLoaded("computercraft");
        AE2_ACTIVE = ModList.get().isLoaded("ae2");

        CACreativeModeTabs.register(eventBus);
        REGISTRATE.registerEventListeners(eventBus);
        CABlocks.register();
        CABlockEntities.register();
        CAItems.register();
        CAFluids.register();
        CAEffects.register(eventBus);
        CARecipes.register(eventBus);
        CASounds.register(eventBus);
        CASchedule.register();
        CADamageTypes.register();
        CADisplaySources.register();
//        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CAPartials::init);
    }
    
    private void packets(final RegisterPayloadHandlersEvent event) {
    	event.registrar(PROTOCOL).playToServer(new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("cas", "obs")), StreamCodec.ofMember(ObservePacket::encode, ObservePacket::decode), ObservePacket::handle);
    	event.registrar(PROTOCOL).playToClient(new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("cas", "eng")), StreamCodec.ofMember(EnergyNetworkPacket::encode, EnergyNetworkPacket::decode), EnergyNetworkPacket::handle);
    }

    private void setup(final FMLCommonSetupEvent event) {
    	// BlockStressValues.CAPACITIES.registerProvider(MODID, AllConfigs.server().kinetics.stressValues);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	// event.enqueueWork(CAPonder::register);
        event.enqueueWork(CAItemProperties::register);

        PonderIndex.addPlugin(new CAPonderPlugin());

        RenderType cutout = RenderType.cutoutMipped();

        ItemBlockRenderTypes.setRenderLayer(CABlocks.TESLA_COIL.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(CABlocks.BARBED_WIRE.get(), cutout);
//        ItemBlockRenderTypes.setRenderLayer(CABlocks.SMALL_LIGHT_CONNECTOR.get(), cutout);
    }

    public void postInit(FMLLoadCompleteEvent evt) {
        BoilerHeater.REGISTRY.register(CABlocks.LIQUID_BLAZE_BURNER.get(), (level, pos, state) -> {
            BlazeBurnerBlock.HeatLevel value = state.getValue(LiquidBlazeBurnerBlock.HEAT_LEVEL);
            if (value == BlazeBurnerBlock.HeatLevel.NONE) return -1;
            if (value == BlazeBurnerBlock.HeatLevel.SEETHING) return 2;
            if (value.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) return 1;
            return 0;
        });

    	System.out.println("Create Crafts & Additions Initialized!");
    }

    public void onRegister(final RegisterEvent event) {
        CAArmInteractions.register();
    }

    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterCommandsEvent event) {
    	CommandDispatcher<CommandSourceStack> dispather = event.getDispatcher();
    	CCApiCommand.register(dispather);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
