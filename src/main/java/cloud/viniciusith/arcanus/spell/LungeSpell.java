package cloud.viniciusith.arcanus.spell;

import cloud.viniciusith.arcanus.component.base.MagicCaster;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class LungeSpell extends Spell {
    public LungeSpell() {
        super(10);
    }

    @Override
    public void OnCast(MagicCaster caster) {
        LivingEntity casterEntity = caster.asEntity();

        // TODO: If the player falls, cancel fall damage
        final float tickDelta = 1.0F;

        Vec3d rotation = casterEntity.getRotationVec(tickDelta);

        casterEntity.setVelocity(rotation.multiply(2.5F));
        casterEntity.velocityModified = true;

        casterEntity.getWorld()
                .playSoundFromEntity(null, casterEntity, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1F, 1F);
    }

    @Override
    public void OnBurnout(ServerPlayerEntity caster) {

    }
}
