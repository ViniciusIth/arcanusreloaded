package cloud.viniciusith.arcanus.spell;

import cloud.viniciusith.arcanus.component.base.MagicCaster;
import cloud.viniciusith.arcanus.helpers.SpellCastHelpers;
import cloud.viniciusith.arcanus.spell.base.Spell;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.Optional;

public class EffectCloudSpell extends Spell {

    public EffectCloudSpell() {
        super(10);
    }

    @Override
    public void OnCast(MagicCaster caster) {
        Optional<ServerPlayerEntity> casterEntity = caster.asPlayer();
        if (casterEntity.isEmpty())
            return;

        final int maxDistance = 35;

        Optional<ItemStack> potionStack = getItemInOffhand(casterEntity.get());
        if (potionStack.isEmpty()) {
            return;
        }

        HitResult result = SpellCastHelpers.raycast(casterEntity.get(), maxDistance, true, false);
        double startDivisor = 5D;

        for (int count = 0; count < 8; count++) {
            Vec3d startPos = casterEntity.get().getEyePos()
                    .add((casterEntity.get().getRandom().nextInt(3) - 1) / startDivisor, (casterEntity.get().getRandom()
                            .nextInt(3) - 1) / startDivisor, (casterEntity.get().getRandom()
                            .nextInt(3) - 1) / startDivisor);
            Vec3d endPos = result.getPos()
                    .add((casterEntity.get().getRandom().nextInt(3) - 1) / (double) maxDistance, (casterEntity.get()
                            .getRandom().nextInt(3) - 1) / (double) maxDistance, (casterEntity.get().getRandom()
                            .nextInt(3) - 1) / (double) maxDistance);

            SpellCastHelpers.drawLine(startPos, endPos, casterEntity.get().getWorld(), 5F, ParticleTypes.EFFECT);
        }

        if (Objects.requireNonNull(result.getType()) == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) result).getBlockPos();

            applyLingeringPotion(potionStack.get(), PotionUtil.getPotion(potionStack.get()), casterEntity.get(), pos);
        } else if (Objects.requireNonNull(result.getType()) == HitResult.Type.ENTITY) {
            BlockPos pos = ((EntityHitResult) result).getEntity().getBlockPos();

            applyLingeringPotion(potionStack.get(), PotionUtil.getPotion(potionStack.get()), casterEntity.get(), pos);
        }
    }

    @Override
    public void OnBurnout(ServerPlayerEntity caster) {

    }

    private Optional<ItemStack> getItemInOffhand(ServerPlayerEntity caster) {
        ItemStack itemStack = caster.getStackInHand(Hand.OFF_HAND);

        if (itemStack.getItem() instanceof PotionItem) {
            return Optional.of(itemStack);
        }

        return Optional.empty();
    }

    private void applyLingeringPotion(ItemStack stack, Potion potion, ServerPlayerEntity caster, BlockPos destination) {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(caster.getWorld(), destination.getX(), destination.getY() + 1, destination.getZ());

        areaEffectCloudEntity.setOwner(caster);

        areaEffectCloudEntity.setRadius(3.0F);
        areaEffectCloudEntity.setRadiusOnUse(1.5F);
        areaEffectCloudEntity.setWaitTime(0);
        areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
        areaEffectCloudEntity.setPotion(potion);
        areaEffectCloudEntity.setDuration(40);

        for (StatusEffectInstance statusEffectInstance : PotionUtil.getPotionEffects(stack)) {
            areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffectInstance));
        }

        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null && nbtCompound.contains("CustomPotionColor", 99)) {
            areaEffectCloudEntity.setColor(nbtCompound.getInt("CustomPotionColor"));
        }

        caster.getWorld().spawnEntity(areaEffectCloudEntity);
    }
}
