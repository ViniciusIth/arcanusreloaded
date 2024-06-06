package cloud.viniciusith.arcanus.spell;

import cloud.viniciusith.arcanus.component.base.MagicCaster;
import cloud.viniciusith.arcanus.helpers.SpellCastHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class KineticShockSpell extends Spell {
    public KineticShockSpell() {
        super(10);
    }

    @Override
    public void OnCast(MagicCaster caster) {
        LivingEntity casterEntity = caster.asEntity();

        final float tickDelta = 1.0F;
        final int maxDistance = 35;

        HitResult result = SpellCastHelpers.raycast(casterEntity, maxDistance, true, false);
        Vec3d rotation = casterEntity.getRotationVec(tickDelta);
        double startDivisor = 5D;

        for (int count = 0; count < 8; count++) {
            Vec3d startPos = casterEntity.getEyePos()
                    .add((casterEntity.getRandom().nextInt(3) - 1) / startDivisor, (casterEntity.getRandom()
                            .nextInt(3) - 1) / startDivisor, (casterEntity.getRandom().nextInt(3) - 1) / startDivisor);
            Vec3d endPos = result.getPos()
                    .add((casterEntity.getRandom().nextInt(3) - 1) / (double) maxDistance, (casterEntity.getRandom()
                            .nextInt(3) - 1) / (double) maxDistance, (casterEntity.getRandom()
                            .nextInt(3) - 1) / (double) maxDistance);

            SpellCastHelpers.drawLine(startPos, endPos, casterEntity.getWorld(), 5F, ParticleTypes.SONIC_BOOM);
        }

        casterEntity.getWorld()
                .playSoundFromEntity(null, casterEntity, SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 1F, casterEntity.getRandom()
                        .nextBetween(1, 30));

        switch (result.getType()) {
            case ENTITY -> {
                EntityHitResult entityHit = (EntityHitResult) result;
                Entity entity = entityHit.getEntity();


                entity.setVelocity(rotation.multiply(2.5F).add(0, 1, 0));

                entity.damage(casterEntity.getWorld().getDamageSources().magic(), 1f);
            }
            case BLOCK -> {
                BlockPos pos = ((BlockHitResult) result).getBlockPos();

                BlockState state = casterEntity.getWorld().getBlockState(pos);
                Block block = state.getBlock();

                if (block instanceof TntBlock) {
                    TntBlock.primeTnt(casterEntity.getWorld(), pos);
                    casterEntity.getWorld().removeBlock(pos, false);
                }

                if (block instanceof FallingBlock) {
                    FallingBlockEntity target = FallingBlockEntity.spawnFromBlock(casterEntity.getWorld(), pos, state);
                    target.setVelocity(rotation.multiply(2.5F).add(0, 1, 0));
                }
            }
            case MISS -> {
                casterEntity.setVelocity(rotation.multiply(-2.5F));
                casterEntity.velocityModified = true;

                casterEntity.damage(casterEntity.getWorld().getDamageSources().magic(), 1f);
            }
        }
    }

    @Override
    public void OnBurnout(ServerPlayerEntity caster) {

    }
}

