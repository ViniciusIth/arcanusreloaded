package cloud.viniciusith.arcanus.registry;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.network.CastSpellPacket;
import cloud.viniciusith.arcanus.network.UpdateSpellPatternPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class PacketRegistry {
    public static final Identifier SPELL_PATTERN_PACKET_ID = new Identifier(ArcanusReloaded.MODID, ("spell_pattern"));
    public static final Identifier SPELL_CAST_PACKET_ID = new Identifier(ArcanusReloaded.MODID, ("cast_spell"));

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SPELL_PATTERN_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            UpdateSpellPatternPacket packet = UpdateSpellPatternPacket.decode(buf);
            server.execute(() -> UpdateSpellPatternPacket.handle(packet, player));
        });

        ServerPlayNetworking.registerGlobalReceiver(SPELL_PATTERN_PACKET_ID, CastSpellPacket::handle);
    }
}
