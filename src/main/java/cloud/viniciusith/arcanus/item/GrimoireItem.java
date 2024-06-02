package cloud.viniciusith.arcanus.item;

import cloud.viniciusith.arcanus.item.grimoire.GrimoireScreenHandler;
import cloud.viniciusith.arcanus.spell.Spell;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GrimoireItem extends Item {
    public final int spellSlots;

    public GrimoireItem(Settings settings, int spellSlots) {
        super(settings);
        this.spellSlots = spellSlots;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            world.playSound(user, user.getBlockPos(), SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 1, 1);
        }

        openScreen(user, user.getStackInHand(hand));
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public static void openScreen(PlayerEntity player, ItemStack grimoireItemStack) {
        if (player.getWorld() != null && !player.getWorld().isClient()) {
            player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
                    packetByteBuf.writeItemStack(grimoireItemStack);
                }

                @Override
                public Text getDisplayName() {
                    return Text.translatable(grimoireItemStack.getItem().getTranslationKey());
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new GrimoireScreenHandler(syncId, inv, grimoireItemStack);
                }
            });
        }
    }

    public Spell[] getSpells() {
        return null;
    }
}
