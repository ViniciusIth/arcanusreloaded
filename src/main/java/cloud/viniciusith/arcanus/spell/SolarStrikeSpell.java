package cloud.viniciusith.arcanus.spell;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.component.base.MagicCaster;
import cloud.viniciusith.arcanus.entity.SolarStrikeEntity;
import cloud.viniciusith.arcanus.helpers.SpellCastHelpers;
import cloud.viniciusith.arcanus.spell.base.Spell;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.Optional;

public class SolarStrikeSpell extends Spell {
    public SolarStrikeSpell() {
        super(80);
    }

    @Override
    public void OnCast(MagicCaster caster) {
        Optional<ServerPlayerEntity> casterEntity = caster.asPlayer();
        if (casterEntity.isEmpty()) {
            ArcanusReloaded.LOGGER.warn("Player only spell cast by another entity");
            return;
        }


        HitResult result = SpellCastHelpers.raycast(casterEntity.get(), 50F, false, false);

        if (result.getType() != HitResult.Type.BLOCK)
            return;

        ChunkPos chunkPos = new ChunkPos(((BlockHitResult) result).getBlockPos());
        casterEntity.get().getServerWorld().setChunkForced(chunkPos.x, chunkPos.z, true);
        SolarStrikeEntity solarStrike = new SolarStrikeEntity(casterEntity.get(), casterEntity.get().getWorld());
        solarStrike.setPos(((BlockHitResult) result).getBlockPos().getX(), ((BlockHitResult) result).getBlockPos()
                .getY(), ((BlockHitResult) result).getBlockPos().getZ());
        casterEntity.get().getWorld().spawnEntity(solarStrike);

        casterEntity.get().getWorld().createExplosion(casterEntity.get(), ((BlockHitResult) result).getBlockPos()
                .getX(), ((BlockHitResult) result).getBlockPos().getY(), ((BlockHitResult) result).getBlockPos()
                                                              .getZ(), 100.0F, World.ExplosionSourceType.MOB);
    }

    @Override
    public void OnBurnout(ServerPlayerEntity caster) {

    }
}
