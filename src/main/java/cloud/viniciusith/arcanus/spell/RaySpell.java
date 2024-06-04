package cloud.viniciusith.arcanus.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class RaySpell extends Spell {
    public RaySpell() {
        super(10);
    }

    @Override
    public void OnCast(ServerPlayerEntity caster) {
        caster.heal(10);

        caster.getWorld().playSound((LivingEntity) caster, caster.getBlockPos(), SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.PLAYERS, 2F, 2F);

        for (int amount = 0; amount < 32; amount++) {
            float offsetX = ((caster.getRandom().nextInt(3) - 1) * caster.getRandom().nextFloat());
            float offsetY = caster.getRandom().nextFloat() * 2F;
            float offsetZ = ((caster.getRandom().nextInt(3) - 1) * caster.getRandom().nextFloat());

            if (!caster.getWorld().isClient())
                ((ServerWorld) caster.getWorld()).spawnParticles(ParticleTypes.HAPPY_VILLAGER, caster.getX() + offsetX, caster.getY() - 0.5 + offsetY, caster.getZ() + offsetZ, 3, 0, 0, 0, 0);
        }
    }

    @Override
    public void OnBurnout(ServerPlayerEntity caster) {

    }
}
