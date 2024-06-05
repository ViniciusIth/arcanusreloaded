package cloud.viniciusith.arcanus.spell;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class LungeSpell extends Spell {
    public LungeSpell() {
        super(10);
    }

    @Override
    public void OnCast(ServerPlayerEntity caster) {
        // TODO: If the player falls, cancel fall damage
        final float tickDelta = 1.0F;

        Vec3d rotation = caster.getRotationVec(tickDelta);

        caster.setVelocity(rotation.multiply(2.5F));
        caster.velocityModified = true;

        caster.getWorld()
                .playSoundFromEntity(null, caster, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1F, 1F);
    }

    @Override
    public void OnBurnout(ServerPlayerEntity caster) {

    }
}
