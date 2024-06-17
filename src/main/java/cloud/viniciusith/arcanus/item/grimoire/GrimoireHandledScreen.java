// Inspired by Inmis screen drawing system
// https://github.com/Draylar/inmis/blob/1.20/src/main/java/draylar/inmis/ui/BackpackHandledScreen.java
package cloud.viniciusith.arcanus.item.grimoire;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.item.SpellPageItem;
import cloud.viniciusith.arcanus.spell.base.Spell;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.util.List;

public class GrimoireHandledScreen extends HandledScreen<GrimoireScreenHandler> {
    private static final int SLOT_SIZE = 18;

    private static final Identifier GUI_TEXTURE = new Identifier(ArcanusReloaded.MODID, "textures/gui/grimoire_container.png");
    private static final Identifier SLOT_TEXTURE = new Identifier(ArcanusReloaded.MODID, "textures/gui/grimoire_slot.png");

    public GrimoireHandledScreen(GrimoireScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.playerInventoryTitleY = 6 + SLOT_SIZE * 2 - 2;
    }

    private static void drawTexturedQuad(DrawContext context, Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x1, y1, z).texture(u1, v1).next();
        bufferBuilder.vertex(matrix4f, x1, y2, z).texture(u1, v2).next();
        bufferBuilder.vertex(matrix4f, x2, y2, z).texture(u2, v2).next();
        bufferBuilder.vertex(matrix4f, x2, y1, z).texture(u2, v1).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static boolean arePatternsEqual(List<Spell.Pattern> pattern1, List<Spell.Pattern> pattern2) {
        if (pattern1.size() != pattern2.size()) {
            return false;
        }
        for (int i = 0; i < pattern1.size(); i++) {
            if (!pattern1.get(i).equals(pattern2.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        renderBackgroundTexture(context, GUI_TEXTURE, x, y, backgroundWidth, backgroundHeight - SLOT_SIZE * 2 + 2);

        // Iterate over each slot in the screen handler
        for (Slot slot : getScreenHandler().slots) {
            context.drawTexture(SLOT_TEXTURE, x + slot.x - 1, y + slot.y - 1, 0, 0, 18, 18, 18, 18);
        }

        // Nested loops to compare each slot with every other slot
        for (int s = 0; s < getScreenHandler().slots.size(); s++) {
            Slot currentSlot = getScreenHandler().slots.get(s);
            var currentSpellSlotPattern = SpellPageItem.getSpellPattern(currentSlot.getStack());

            // Skip the current slot if it has no spell pattern
            if (currentSpellSlotPattern.isEmpty())
                continue;

            for (int i = 0; i < getScreenHandler().slots.size(); i++) {
                if (s == i)
                    continue; // Skip comparing the slot with itself

                Slot otherSlot = getScreenHandler().slots.get(i);
                var otherSpellSlotPattern = SpellPageItem.getSpellPattern(otherSlot.getStack());

                // Skip the other slot if it has no spell pattern
                if (otherSpellSlotPattern.isEmpty())
                    continue;

                // If patterns match, highlight the slot and show tooltip if the mouse is over it
                if (arePatternsEqual(currentSpellSlotPattern.get(), otherSpellSlotPattern.get())) {
                    context.fill(x + otherSlot.x - 1, y + otherSlot.y - 1, x + otherSlot.x - 1 + 18, y + otherSlot.y - 1 + 18, 0x2FFF0000);

                    if (mouseX >= (double) x + otherSlot.x - 1 && mouseY >= (double) y + otherSlot.y - 1 &&
                            mouseX < (double) (x + otherSlot.x - 1 + 18) && mouseY < (double) (y + otherSlot.y - 1 + 18)) {
                        context.drawTooltip(this.textRenderer, Text.of("Some spell already has this pattern"), mouseX + x + otherSlot.x - 1, mouseY - y + otherSlot.y - 1);
                    }
                }
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    public void renderBackgroundTexture(DrawContext context, Identifier texture, int x, int y, int width, int height) {
        int xTextureOffset = 0;
        int yTextureOffset = 66;
        int cornerSize = 8;
        float textureSize = 256f;

        /// 9 Patch Texture

        // Four Corners
        // Top-left corner
        context.drawTexture(texture, x, y, 106 + xTextureOffset, 124 + yTextureOffset, cornerSize, cornerSize);
        // Top-right corner
        context.drawTexture(texture, x + width - cornerSize, y, 248 + xTextureOffset, 124 + yTextureOffset, cornerSize, cornerSize);
        // Bottom-left corner
        context.drawTexture(texture, x, y + height - cornerSize, 106 + xTextureOffset, 182 + yTextureOffset, cornerSize, cornerSize);
        // Bottom-right corner
        context.drawTexture(texture, x + width - cornerSize, y + height - cornerSize, 248 + xTextureOffset, 182 + yTextureOffset, cornerSize, cornerSize);

        // Sides
        // Top side
        drawTexturedQuad(context, texture, x + cornerSize, x + width - cornerSize, y, y + cornerSize, 0, (114 + xTextureOffset) / textureSize, (248 + xTextureOffset) / textureSize, (124 + yTextureOffset) / textureSize, (132 + yTextureOffset) / textureSize);
        // Bottom side
        drawTexturedQuad(context, texture, x + cornerSize, x + width - cornerSize, y + height - cornerSize, y + height, 0, (114 + xTextureOffset) / textureSize, (248 + xTextureOffset) / textureSize, (182 + yTextureOffset) / textureSize, (190 + yTextureOffset) / textureSize);
        // Left side
        drawTexturedQuad(context, texture, x, x + cornerSize, y + cornerSize, y + height - cornerSize, 0, (106 + xTextureOffset) / textureSize, (114 + xTextureOffset) / textureSize, (132 + yTextureOffset) / textureSize, (182 + yTextureOffset) / textureSize);
        // Right side
        drawTexturedQuad(context, texture, x + width - cornerSize, x + width, y + cornerSize, y + height - cornerSize, 0, (248 + xTextureOffset) / textureSize, (256 + xTextureOffset) / textureSize, (132 + yTextureOffset) / textureSize, (182 + yTextureOffset) / textureSize);

        // Center
        drawTexturedQuad(context, texture, x + cornerSize, x + width - cornerSize, y + cornerSize, y + height - cornerSize, 0, (114 + xTextureOffset) / textureSize, (248 + xTextureOffset) / textureSize, (132 + yTextureOffset) / textureSize, (182 + yTextureOffset) / textureSize);
    }
}

//public class GrimoireHandledScreen extends HandledScreen<GrimoireScreenHandler> {
//    public static final Identifier BOOK_TEXTURE = new Identifier(ArcanusReloaded.MODID, "textures/gui/grimoire_book.png");
//    private static final Identifier SLOT_TEXTURE = new Identifier(ArcanusReloaded.MODID, "textures/gui/grimoire_slot.png");
//    private static final Identifier GUI_TEXTURE = new Identifier(ArcanusReloaded.MODID, "textures/gui/grimoire_container.png");
//    private static final int SLOT_SQUARE_SIZE = 18;
//    private static final int SLOT_PADDING = 10; // Padding around the slots
//
//    private final int pageCount;
//    private int currentPage = 1;
//
//
//    public GrimoireHandledScreen(GrimoireScreenHandler handler, PlayerInventory player, Text title) {
//        super(handler, player, title);
//
//        pageCount = handler.getItem().spellSlots;
//
//        Dimension dimension = handler.getDimension();
//        this.backgroundWidth = dimension.getWidth();
//        this.backgroundHeight = dimension.getHeight();
//    }
//
//    @Override
//    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
//        // Render the player inventory background
//        int bookX = (this.width - this.backgroundWidth) / 2; // Position to the right of the book
//        int bookY = (this.height - this.backgroundHeight) / 2;
//
//        context.drawTexture(BOOK_TEXTURE, handler.xOffset, handler.yOffset, 0, 0, 192, 192);
//
//        // Render the player inventory background
//        int playerInvX = bookX + this.backgroundWidth + 10;
//        int playerInvY = bookY;
//        Rectangle playerInvBounds = new Rectangle(playerInvX, playerInvY, 176, 96); // 176 width for 9 slots, 96 height for 3 rows
//
//        renderBackgroundTexture(context, GUI_TEXTURE, playerInvBounds, delta, 0xFFFFFF);
//    }
//
//    @Override
//    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        this.renderBackground(context);
//        super.render(context, mouseX, mouseY, delta);
//        this.drawMouseoverTooltip(context, mouseX, mouseY);
//    }
//
//    @Override
//    public void drawForeground(DrawContext context, int mouseX, int mouseY) {
//        drawItemInformation(context, 0, 0, handler.slots.get(0));
//    }
//
//    public void renderBackgroundTexture(DrawContext context, Identifier texture, Rectangle bounds, float delta, int color) {
//        int x = bounds.x;
//        int y = bounds.y;
//        int width = bounds.width;
//        int height = bounds.height;
//        int xTextureOffset = 0;
//        int yTextureOffset = 66;
//        int cornerSize = 8;
//        float textureSize = 256f;
//
//        /// 9 Patch Texture
//
//        // Four Corners
//        // Top-left corner
//        context.drawTexture(texture, x, y, 106 + xTextureOffset, 124 + yTextureOffset, cornerSize, cornerSize);
//        // Top-right corner
//        context.drawTexture(texture, x + width - cornerSize, y, 248 + xTextureOffset, 124 + yTextureOffset, cornerSize, cornerSize);
//        // Bottom-left corner
//        context.drawTexture(texture, x, y + height - cornerSize, 106 + xTextureOffset, 182 + yTextureOffset, cornerSize, cornerSize);
//        // Bottom-right corner
//        context.drawTexture(texture, x + width - cornerSize, y + height - cornerSize, 248 + xTextureOffset, 182 + yTextureOffset, cornerSize, cornerSize);
//
//        // Sides
//        // Top side
//        drawTexturedQuad(context, texture, x + cornerSize, x + width - cornerSize, y, y + cornerSize, 0,
//                (114 + xTextureOffset) / textureSize, (248 + xTextureOffset) / textureSize, (124 + yTextureOffset) / textureSize, (132 + yTextureOffset) / textureSize);
//        // Bottom side
//        drawTexturedQuad(context, texture, x + cornerSize, x + width - cornerSize, y + height - cornerSize, y + height, 0,
//                (114 + xTextureOffset) / textureSize, (248 + xTextureOffset) / textureSize, (182 + yTextureOffset) / textureSize, (190 + yTextureOffset) / textureSize);
//        // Left side
//        drawTexturedQuad(context, texture, x, x + cornerSize, y + cornerSize, y + height - cornerSize, 0,
//                (106 + xTextureOffset) / textureSize, (114 + xTextureOffset) / textureSize, (132 + yTextureOffset) / textureSize, (182 + yTextureOffset) / textureSize);
//        // Right side
//        drawTexturedQuad(context, texture, x + width - cornerSize, x + width, y + cornerSize, y + height - cornerSize, 0,
//                (248 + xTextureOffset) / textureSize, (256 + xTextureOffset) / textureSize, (132 + yTextureOffset) / textureSize, (182 + yTextureOffset) / textureSize);
//
//        // Center
//        drawTexturedQuad(context, texture, x + cornerSize, x + width - cornerSize, y + cornerSize, y + height - cornerSize, 0,
//                (114 + xTextureOffset) / textureSize, (248 + xTextureOffset) / textureSize, (132 + yTextureOffset) / textureSize, (182 + yTextureOffset) / textureSize);
//    }
//
//    private static void drawTexturedQuad(DrawContext context, Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
//        RenderSystem.setShaderTexture(0, texture);
//        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
//        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
//        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
//        bufferBuilder.vertex(matrix4f, x1, y1, z).texture(u1, v1).next();
//        bufferBuilder.vertex(matrix4f, x1, y2, z).texture(u1, v2).next();
//        bufferBuilder.vertex(matrix4f, x2, y2, z).texture(u2, v2).next();
//        bufferBuilder.vertex(matrix4f, x2, y1, z).texture(u2, v1).next();
//        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
//    }
//
//    private int 0 {
//        return 0;
//    }
//
//    private void drawItemInformation(DrawContext context, int x, int y, Slot slot) {
//        if (slot.hasStack()) {
//            // Draw item information here
//            String itemInfo = slot.getStack().getName().getString(); // Example: get item name
//            context.drawText(this.textRenderer, itemInfo, x + SLOT_SQUARE_SIZE + SLOT_PADDING, y + SLOT_SQUARE_SIZE / 2, 0x000000, false);
//        }
//    }
//}
