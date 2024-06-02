package cloud.viniciusith.arcanus.spell;

import cloud.viniciusith.arcanus.component.entity.PlayerMagicCaster;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class RaySpell extends Spell {
    public RaySpell() {
        super(10);
    }

    @Override
    public void OnCast(PlayerMagicCaster caster) {
        LivingEntity entity = caster.getEntity();
        entity.heal(10);

        entity.getWorld().playSound(entity, entity.getBlockPos(), SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.PLAYERS, 2F, 2F);

        for (int amount = 0; amount < 32; amount++) {
            float offsetX = ((entity.getRandom().nextInt(3) - 1) * entity.getRandom().nextFloat());
            float offsetY = entity.getRandom().nextFloat() * 2F;
            float offsetZ = ((entity.getRandom().nextInt(3) - 1) * entity.getRandom().nextFloat());

            if (!entity.getWorld().isClient())
                ((ServerWorld) entity.getWorld()).spawnParticles(ParticleTypes.HAPPY_VILLAGER, entity.getX() + offsetX, entity.getY() - 0.5 + offsetY, entity.getZ() + offsetZ, 3, 0, 0, 0, 0);
        }
    }

    @Override
    public void OnBurnout(PlayerMagicCaster caster) {

    }
}
