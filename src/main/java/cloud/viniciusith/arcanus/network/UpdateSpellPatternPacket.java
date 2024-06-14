package cloud.viniciusith.arcanus.network;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.item.SpellPageItem;
import cloud.viniciusith.arcanus.spell.base.Spell;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

public class UpdateSpellPatternPacket {
    private final ItemStack itemStack;
    private final ArrayList<Spell.Pattern> newSpellPattern;

    public UpdateSpellPatternPacket(ItemStack itemStack, ArrayList<Spell.Pattern> newSpellPattern) {
        this.itemStack = itemStack;
        this.newSpellPattern = newSpellPattern;
    }

    public static UpdateSpellPatternPacket decode(PacketByteBuf buf) {
        ItemStack itemStack = buf.readItemStack();
        ArrayList<Spell.Pattern> newSpellPattern = new ArrayList<>();
        for (int i = 0; i < Spell.Pattern.MAX_SIZE; i++) {
            newSpellPattern.add(Spell.Pattern.fromSymbol(buf.readString()));
        }

        return new UpdateSpellPatternPacket(itemStack, newSpellPattern);
    }

    public static void handle(UpdateSpellPatternPacket packet, ServerPlayerEntity serverPlayer) {
        ArcanusReloaded.LOGGER.info("Update package");
        SpellPageItem.setSpellPattern(packet.itemStack, packet.newSpellPattern);

        // Syncs the item back to the player
        // FIXME: Currently can duplicate items and even delete others, this happens because it just looks at the currently selected slot and changes it, even if the item is on the offhand
        serverPlayer.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(-2, 0, serverPlayer.getInventory().selectedSlot, packet.itemStack));
    }

    public void encode(PacketByteBuf buf) {
        buf.writeItemStack(itemStack);
        for (Spell.Pattern pattern : newSpellPattern) {
            buf.writeString(pattern.getSymbol());
        }
    }

    public PacketByteBuf getByteBuf() {
        PacketByteBuf buf = PacketByteBufs.create();
        this.encode(buf);

        return buf;
    }
}
