package cloud.viniciusith.arcanus.spell;

import cloud.viniciusith.arcanus.helpers.SpellCastHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
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
    public void OnCast(ServerPlayerEntity caster) {
        final float tickDelta = 1.0F;

        HitResult result = SpellCastHelpers.raycast(caster, 35, true, false);
        Vec3d rotation = caster.getRotationVec(tickDelta);
        double startDivisor = 5D;
        double endDivisor = 15D;

        for (int count = 0; count < 8; count++) {
            Vec3d startPos = caster.getEyePos().add((caster.getRandom().nextInt(3) - 1) / startDivisor, (caster.getRandom().nextInt(3) - 1) / startDivisor, (caster.getRandom().nextInt(3) - 1) / startDivisor);
            Vec3d endPos = result.getPos().add((caster.getRandom().nextInt(3) - 1) / endDivisor, (caster.getRandom().nextInt(3) - 1) / endDivisor, (caster.getRandom().nextInt(3) - 1) / endDivisor);

            SpellCastHelpers.drawLine(startPos, endPos, caster.getWorld(), 10F, ParticleTypes.SONIC_BOOM);
        }

        caster.getWorld().playSoundFromEntity(null, caster, SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 1F, caster.getRandom().nextBetween(1, 30));

        switch (result.getType()) {
            case ENTITY -> {
                EntityHitResult entityHit = (EntityHitResult) result;
                Entity entity = entityHit.getEntity();


                entity.setVelocity(rotation.multiply(2.5F).add(0, 1, 0));

                entity.damage(caster.getWorld().getDamageSources().magic(), 1f);
            }
            case BLOCK -> {
                BlockPos pos = ((BlockHitResult) result).getBlockPos();

                BlockState state = caster.getWorld().getBlockState(pos);
                Block block = state.getBlock();

                if (block instanceof TntBlock) {
                    TntBlock.primeTnt(caster.getWorld(), pos);
                    caster.getWorld().removeBlock(pos, false);
                }

                if (block instanceof FallingBlock) {
                    FallingBlockEntity target = FallingBlockEntity.spawnFromBlock(caster.getWorld(), pos, state);
                    target.setVelocity(rotation.multiply(2.5F).add(0, 1, 0));
                }
            }
            case MISS -> {
                caster.setVelocity(rotation.multiply(2.5F));
                caster.velocityModified = true;

                caster.damage(caster.getWorld().getDamageSources().magic(), 1f);
            }
        }
    }

    @Override
    public void OnBurnout(ServerPlayerEntity caster) {

    }
}

