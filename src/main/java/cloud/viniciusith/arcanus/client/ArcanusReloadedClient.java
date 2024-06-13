package cloud.viniciusith.arcanus.client;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.client.entity.SolarStrikeEntityRenderer;
import cloud.viniciusith.arcanus.client.hud.ManaOverlay;
import cloud.viniciusith.arcanus.item.grimoire.GrimoireHandledScreen;
import cloud.viniciusith.arcanus.registry.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class ArcanusReloadedClient implements ClientModInitializer {
    public static final String MOD_ID = "arcanus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static RenderLayer getMagicCircles(Identifier texture) {
        return RenderLayer.of(
                texture.toString(),
                VertexFormats.POSITION,
                VertexFormat.DrawMode.QUADS,
                256,
                false,
                true,
                RenderLayer.of(RenderPhase.LIGHTNING_PROGRAM)
        );
    }

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.SOLAR_STRIKE, SolarStrikeEntityRenderer::new);

        HandledScreens.register(ArcanusReloaded.GRIMOIRE_CONTAINER_TYPE, GrimoireHandledScreen::new);

        HudRenderCallback.EVENT.register(new ManaOverlay());
    }
}
