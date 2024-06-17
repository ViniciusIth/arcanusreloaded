package cloud.viniciusith.arcanus.item.spellpage;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.item.SpellPageItem;
import cloud.viniciusith.arcanus.network.UpdateSpellPatternPacket;
import cloud.viniciusith.arcanus.registry.PacketRegistry;
import cloud.viniciusith.arcanus.spell.base.Spell;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
public class SpellPageScreen extends Screen {
    private static final Identifier SCREEN_BACKGROUND = new Identifier(ArcanusReloaded.MODID, "textures/gui/spell_page.png");

    private final PlayerEntity player;
    private final ItemStack itemStack;
    private final ArrayList<Spell.Pattern> pattern;
    private boolean dirty;

    private SingleLetterWidget letterWidget1;
    private SingleLetterWidget letterWidget2;
    private SingleLetterWidget letterWidget3;

    public SpellPageScreen(PlayerEntity player, ItemStack itemStack, Hand hand) {
        super(Text.of(SpellPageItem.getSpellName(itemStack).orElse("Null Spell")));

        this.player = player;
        this.itemStack = itemStack;
        this.pattern = SpellPageItem.getSpellPattern(itemStack).orElse(new ArrayList<>());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        int widgetWidth = 18;
        int widgetHeight = 18;
        int spacing = 10;
        int startX = this.width / 2 - (widgetWidth * 3 + spacing * 2) / 2;
        int startY = this.height / 2 - widgetHeight / 2;

        if (this.pattern.size() < 3) {
            for (int i = 0; i < 3; i++) {
                this.pattern.add(Spell.Pattern.LEFT);
                this.dirty = true;
            }
        }

        letterWidget1 = new SingleLetterWidget(startX, startY, widgetWidth, widgetHeight, this.pattern.get(0), textRenderer);
        letterWidget2 = new SingleLetterWidget(startX + widgetWidth + spacing, startY, widgetWidth, widgetHeight, this.pattern.get(1), textRenderer);
        letterWidget3 = new SingleLetterWidget(startX + 2 * (widgetWidth + spacing), startY, widgetWidth, widgetHeight, this.pattern.get(2), textRenderer);

        this.addSelectableChild(letterWidget1);
        this.addSelectableChild(letterWidget2);
        this.addSelectableChild(letterWidget3);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        final int TEXTURE_WIDTH = 126; // Actual visible texture, the other parameter refers to the width of the file
        final int TEXTURE_HEIGHT = 162;

        int x = (this.width - TEXTURE_WIDTH) / 2;
        int y = (this.height - TEXTURE_HEIGHT) / 2;

        context.drawTexture(SCREEN_BACKGROUND, x, y, 0, 0.0F, 0.0F, TEXTURE_WIDTH, TEXTURE_HEIGHT, 256, 256);

        super.render(context, mouseX, mouseY, delta);

        context.drawText(this.textRenderer, this.title, x + 10, y + 10, 0x000000, false);

        letterWidget1.render(context, mouseX, mouseY, delta);
        letterWidget2.render(context, mouseX, mouseY, delta);
        letterWidget3.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        super.close();

        if (this.dirty) {
            ArrayList<Spell.Pattern> newPattern = new ArrayList<>();
            newPattern.add(letterWidget1.getPatternKey());
            newPattern.add(letterWidget2.getPatternKey());
            newPattern.add(letterWidget3.getPatternKey());

            // Create and send the packet
            UpdateSpellPatternPacket packet = new UpdateSpellPatternPacket(itemStack, newPattern);
            ClientPlayNetworking.send(PacketRegistry.SPELL_PATTERN_PACKET_ID, packet.getByteBuf());
        }
    }

    protected class SingleLetterWidget extends ClickableWidget {
        private final TextRenderer textRenderer;
        private Spell.Pattern patternKey;

        public SingleLetterWidget(int x, int y, int width, int height, Spell.Pattern patternKey, TextRenderer textRenderer) {
            super(x, y + 40, width, height, Text.of(patternKey.getSymbol()));

            this.patternKey = patternKey;
            this.textRenderer = textRenderer;
        }

        @Override
        protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            // Maybe use a custom font?


            context.fill(this.getX(), this.getY() + 18, this.getX() + this.width, this.getY() + 18 - 2, 0xFF000000);
            context.drawText(this.textRenderer, patternKey.getSymbol(), this.getX() + this.width / 3, this.getY() + (this.height - 8) / 2, 0x000000FF, false);

            if (isMouseOver(mouseX, mouseY))
                context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xEFFFFFFF);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.isMouseOver(mouseX, mouseY)) {
                patternKey = patternKey == Spell.Pattern.RIGHT ? Spell.Pattern.LEFT : Spell.Pattern.RIGHT;
                dirty = true;
                return true;
            }

            return false;
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        }

        public Spell.Pattern getPatternKey() {
            return patternKey;
        }
    }
}
