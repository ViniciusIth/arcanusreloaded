package cloud.viniciusith.arcanus.registry;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.component.base.MagicCaster;
import cloud.viniciusith.arcanus.component.entity.PlayerMagicCaster;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ComponentRegistry implements EntityComponentInitializer {
    public static final ComponentKey<MagicCaster> MAGIC_CASTER_COMPONENT =
            dev.onyxstudios.cca.api.v3.component.ComponentRegistry.getOrCreate(
                    new Identifier(ArcanusReloaded.MODID, "magic_caster"),
                    MagicCaster.class
            );


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, MAGIC_CASTER_COMPONENT)
                .impl(PlayerMagicCaster.class)
                .respawnStrategy(RespawnCopyStrategy.CHARACTER)
                .end(PlayerMagicCaster::new);
    }
}
