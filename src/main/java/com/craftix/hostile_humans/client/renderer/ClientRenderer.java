package com.craftix.hostile_humans.client.renderer;

import com.craftix.hostile_humans.HostileHumans;
import com.craftix.hostile_humans.entity.entities.ModEntityType;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;

@OnlyIn(Dist.CLIENT)
public class ClientRenderer {

    public static final ModelLayerLocation HUMAN_MODEL_LAYER =
            new ModelLayerLocation(new ResourceLocation(HostileHumans.MOD_ID, "human"), "main");

    protected ClientRenderer() {
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityType.HUMAN1.get(), HumanRenderer::new);
        event.registerEntityRenderer(ModEntityType.HUMAN2.get(), HumanRenderer::new);
        event.registerEntityRenderer(ModEntityType.ROAMER.get(), HumanRenderer::new);
        event.registerEntityRenderer(ModEntityType.SPAWNER_ENTITY.get(), SpawnerEntityRenderer::new);
    }

    public static void registerEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HUMAN_MODEL_LAYER, () ->
                LayerDefinition.create(PlayerModel.createMesh(CubeDeformation.NONE, true), 64, 64));
    }
}
