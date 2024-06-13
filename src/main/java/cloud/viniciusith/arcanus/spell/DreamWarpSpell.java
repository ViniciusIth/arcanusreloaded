package cloud.viniciusith.arcanus.spell;

import cloud.viniciusith.arcanus.component.base.MagicCaster;
import cloud.viniciusith.arcanus.spell.base.Spell;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.util.Optional;

public class DreamWarpSpell extends Spell {
    public DreamWarpSpell() {
        super(10);
    }

    @Override
    public void OnCast(MagicCaster caster) {
        // FIXME: If the player uses a respawn anchor and break it, the overworld spawn doesn't actually take over
        Optional<ServerPlayerEntity> casterEntity = caster.asPlayer();
        if (casterEntity.isEmpty())
            return;

        Optional<Vec3d> spawn = getPlayerSpawn(casterEntity.get());
        RegistryKey<World> spawnDimension = casterEntity.get().getSpawnPointDimension();

        if (spawn.isEmpty()) {
            casterEntity.get().sendMessage(Text.translatable("block.minecraft.spawn.not_valid"));
            return;
        }

        teleportPlayerTo(casterEntity.get(), spawn.get(), spawnDimension);
        casterEntity.get().getWorld()
                .playSoundFromEntity(null, casterEntity.get(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
    }

    @Override
    public void OnBurnout(ServerPlayerEntity caster) {
    }

    public static Optional<Vec3d> getPlayerSpawn(ServerPlayerEntity serverPlayerEntity) {
        ServerWorld targetWorld = serverPlayerEntity.server.getWorld(serverPlayerEntity.getSpawnPointDimension());
        BlockPos spawnPoint = serverPlayerEntity.getSpawnPointPosition();

        if (spawnPoint == null) {
            return Optional.empty();
        }

        BlockState respawnBlockState = targetWorld.getBlockState(spawnPoint);
        Block respawnBlock = respawnBlockState.getBlock();

        if (respawnBlock instanceof RespawnAnchorBlock) {
            return RespawnAnchorBlock.findRespawnPosition(EntityType.PLAYER, targetWorld, spawnPoint); // Doesn't actually use the anchor charge
        } else if (respawnBlock instanceof BedBlock) {
            return BedBlock.findWakeUpPosition(
                    EntityType.PLAYER,
                    targetWorld,
                    spawnPoint,
                    respawnBlockState.get(BedBlock.FACING),
                    serverPlayerEntity.getSpawnAngle()
            );
        } else if (serverPlayerEntity.isSpawnForced()) {
            boolean footBlockClear = respawnBlock.canMobSpawnInside(respawnBlockState);
            boolean headBlockClear = targetWorld.getBlockState(spawnPoint.up()).getBlock()
                    .canMobSpawnInside(respawnBlockState);

            if (footBlockClear && headBlockClear) {
                return Optional.of(new Vec3d((double) spawnPoint.getX() + 0.5D, (double) spawnPoint.getY() + 0.1D, (double) spawnPoint.getZ() + 0.5D));
            }
        }

        return Optional.empty();
    }


    public static void teleportPlayerTo(ServerPlayerEntity playerEntity, Vec3d targetPos, RegistryKey<World> destination) {
        ServerWorld destinationDim = playerEntity.getServer().getWorld(destination);

        if (!destination.equals(playerEntity.getServerWorld().getRegistryKey())) {
            FabricDimensions.teleport(playerEntity, destinationDim, new TeleportTarget(targetPos, Vec3d.ZERO, 0, 0));
        }

        playerEntity.teleport(targetPos.getX(), targetPos.getY(), targetPos.getZ());
    }
}
