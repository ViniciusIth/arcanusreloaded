package cloud.viniciusith.arcanus.entity;

import cloud.viniciusith.arcanus.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SolarStrikeEntity extends PersistentProjectileEntity {
    public final List<LivingEntity> hasHit = new ArrayList<>();

    public SolarStrikeEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public SolarStrikeEntity(LivingEntity owner, World world) {
        super(EntityRegistry.SOLAR_STRIKE, owner, world);
    }

    @Override
    public void tick() {
        if (getWorld().isClient()) {
            if (this.age >= 2 && this.age <= 5) {
                getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX() + 2, getY(), getZ(), 1.0D, 0.0D, 0.0D);
                getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX() - 2, getY(), getZ(), 1.0D, 0.0D, 0.0D);
                getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ() + 2, 1.0D, 0.0D, 0.0D);
                getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ() - 2, 1.0D, 0.0D, 0.0D);
            }
            return;
        }

        if (age <= 9) {
            Box box = new Box(getX() - 4, getY() - 1, getZ() - 4, getX() + 4, (getWorld().getHeight() + 2048) - getY(), getZ() + 4);
            float radius = (float) (box.maxX - box.minX) / 2;

            getNearbyEntities(box).forEach(livingEntity -> {
                if (hasHit.contains(livingEntity))
                    return;

                Vec2f pos1 = new Vec2f((float) getX(), (float) getZ());
                Vec2f pos2 = new Vec2f((float) livingEntity.getX(), (float) livingEntity.getZ());

                if (livingEntity instanceof LivingEntity) {
                    livingEntity.setOnFireFor(3);

                    livingEntity.damage(this.getWorld().getDamageSources()
                                                .outOfWorld(), Math.max(10F, 50F * (1 - (MathHelper.sqrt(pos1.distanceSquared(pos2)) / radius))));
                    hasHit.add(livingEntity);
                }
            });
        }

        if (this.age >= 23)
            kill();
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    public void kill() {
        if (!getWorld().isClient())
            ((ServerWorld) getWorld()).setChunkForced(getChunkPos().x, getChunkPos().z, false);

        super.kill();
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Nullable
    @Override
    public ItemStack getPickBlockStack() {
        return ItemStack.EMPTY;
    }

    private Stream<LivingEntity> getNearbyEntities(Box box) {
        return this.getWorld().getEntitiesByClass(LivingEntity.class, box, EntityPredicates.EXCEPT_SPECTATOR).stream();
    }
}
