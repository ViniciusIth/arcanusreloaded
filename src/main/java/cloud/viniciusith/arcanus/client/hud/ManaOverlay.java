package cloud.viniciusith.arcanus.client.hud;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.component.base.MagicCaster;
import cloud.viniciusith.arcanus.registry.ComponentRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ManaOverlay implements HudRenderCallback {
    private static final Identifier HUD_ELEMENTS = new Identifier(ArcanusReloaded.MODID, "textures/gui/hud_elements.png");

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();


        if (client.cameraEntity instanceof PlayerEntity player && !player.isSpectator() && !player.isCreative()) {
            MagicCaster caster = player.getComponent(ComponentRegistry.MAGIC_CASTER_COMPONENT);

            if (caster.getMana() == caster.getMaxMana())
                return;

            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width / 2;
            y = height;

            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, HUD_ELEMENTS);

            int mana = Math.min(caster.getMana(), caster.getMaxMana());

            // Draw background
            for (int i = 0; i < 10; i++)
                drawContext.drawTexture(HUD_ELEMENTS, x - 91 + (i * 8), y - 54, 0, 15, 9, 9, 256, 256);

            // Draw full mana orb
            for (int i = 0; i < mana / 10; i++) {
                drawContext.drawTexture(HUD_ELEMENTS, x - 91 + (i * 8), y - 54, 0, 0, 8, 8, 256, 256);
            }

            // Draw half mana orbs
            if (mana % 2 == 1)
                drawContext.drawTexture(HUD_ELEMENTS, x - 94 + ((mana / 10) * 8), y - 54, 8, 0, 8, 8, 256, 256);

            // Draw full mana orb
            if (mana < 0) {
                int iterations = Math.min(Math.abs(mana) / 10, caster.getMaxMana() / 10);
                for (int i = 0; i < iterations; i++) {
                    drawContext.drawTexture(HUD_ELEMENTS, x - 91 + (i * 8), y - 54, 16, 0, 8, 8, 256, 256);
                }
            }
        }
    }
}