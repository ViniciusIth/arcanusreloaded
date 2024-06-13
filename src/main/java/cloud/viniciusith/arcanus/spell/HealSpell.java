package cloud.viniciusith.arcanus.spell;

import cloud.viniciusith.arcanus.component.base.MagicCaster;
import cloud.viniciusith.arcanus.spell.base.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class HealSpell extends Spell {
    public HealSpell() {
        super(10);
    }

    @Override
    public void OnCast(MagicCaster caster) {
        LivingEntity casterEntity = caster.asEntity();

        casterEntity.heal(5);

        casterEntity.getWorld()
                .playSound(null, casterEntity.getBlockPos(), SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.PLAYERS, 2F, 2F);

        for (int amount = 0; amount < 32; amount++) {
            float offsetX = ((casterEntity.getRandom().nextInt(3) - 1) * casterEntity.getRandom().nextFloat());
            float offsetY = casterEntity.getRandom().nextFloat() * 2F;
            float offsetZ = ((casterEntity.getRandom().nextInt(3) - 1) * casterEntity.getRandom().nextFloat());

            if (!casterEntity.getWorld().isClient())
                ((ServerWorld) casterEntity.getWorld()).spawnParticles(ParticleTypes.HAPPY_VILLAGER, casterEntity.getX() + offsetX, casterEntity.getY() - 0.5 + offsetY, casterEntity.getZ() + offsetZ, 3, 0, 0, 0, 0);
        }
    }

    @Override
    public void OnBurnout(ServerPlayerEntity caster) {

    }
}
