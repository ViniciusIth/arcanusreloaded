package cloud.viniciusith.arcanus.spell;

import cloud.viniciusith.arcanus.entity.SolarStrikeEntity;
import cloud.viniciusith.arcanus.helpers.SpellCastHelpers;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.ChunkPos;

public class SolarStrikeSpell extends Spell {
    public SolarStrikeSpell() {
        super(10);
    }

    @Override
    public void OnCast(ServerPlayerEntity caster) {
        HitResult result = SpellCastHelpers.raycast(caster, 640F, false, false);

        if (result.getType() != HitResult.Type.BLOCK)
            return;

        ChunkPos chunkPos = new ChunkPos(((BlockHitResult) result).getBlockPos());
        caster.getServerWorld().setChunkForced(chunkPos.x, chunkPos.z, true);
        SolarStrikeEntity solarStrike = new SolarStrikeEntity(caster, caster.getWorld());
        solarStrike.setPos(((BlockHitResult) result).getBlockPos().getX(), ((BlockHitResult) result).getBlockPos()
                .getY(), ((BlockHitResult) result).getBlockPos().getZ());
        caster.getWorld().spawnEntity(solarStrike);
    }

    @Override
    public void OnBurnout(ServerPlayerEntity caster) {

    }
}
