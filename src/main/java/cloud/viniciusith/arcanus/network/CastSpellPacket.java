package cloud.viniciusith.arcanus.network;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.component.base.MagicCaster;
import cloud.viniciusith.arcanus.item.WandItem;
import cloud.viniciusith.arcanus.registry.ComponentRegistry;
import cloud.viniciusith.arcanus.registry.SpellRegistry;
import cloud.viniciusith.arcanus.spell.base.Spell;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class CastSpellPacket {
    public static final Identifier ID = new Identifier(ArcanusReloaded.MODID, ("cast_spell"));

    public static void send(String spellName) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(spellName);

        ClientPlayNetworking.send(ID, buf);
    }

    public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String spellName = buf.readString();

        Optional<Class<? extends Spell>> spellClass = SpellRegistry.getSpellClass(spellName);
        if (spellClass.isEmpty()) return;

        Spell spell;

        try {
            spell = spellClass.get().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            ArcanusReloaded.LOGGER.error(e);
            return;
        }

        server.execute(() -> {
            MagicCaster caster = player.getComponent(ComponentRegistry.MAGIC_CASTER_COMPONENT);

            caster.cast(spell);

            ItemStack stack = player.getMainHandStack();
            if (stack.getItem() instanceof WandItem wand && wand.hasUpgrade()) {
                int realManaCost = spell.getManaCost() * wand.getCastingCostMultiplier();

                NbtCompound tag = stack.getOrCreateSubNbt(ArcanusReloaded.MODID);
                int exp = tag.getInt("Exp") + realManaCost;
                tag.putInt("Exp", exp);

                if (exp >= wand.getMaxExperience()) {
                    ItemStack newStack = new ItemStack(wand.getUpgrade());
                    NbtCompound newTag = newStack.getOrCreateSubNbt(ArcanusReloaded.MODID);
                    newTag.putInt("Exp", wand.getMaxExperience());
                    player.setStackInHand(Hand.MAIN_HAND, newStack);
                }
            }
        });
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID, CastSpellPacket::handle);
    }
}
