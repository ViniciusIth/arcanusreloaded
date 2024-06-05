package cloud.viniciusith.arcanus.registry;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.entity.SolarStrikeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {
    public static final EntityType<SolarStrikeEntity> SOLAR_STRIKE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ArcanusReloaded.MODID, "solar_strike"),
            FabricEntityTypeBuilder.<SolarStrikeEntity>create(SpawnGroup.MISC, SolarStrikeEntity::new)
                    .trackRangeChunks(8)
                    .dimensions(EntityDimensions.fixed(0, 0)).build()
    );

    public static void registerAllEntities() {}
}
