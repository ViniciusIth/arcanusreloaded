package cloud.viniciusith.arcanus.helpers;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpellCastHelpers {

    public static HitResult raycast(LivingEntity origin, double maxDistance, boolean includeEntity, boolean includeLiquids) {
        maxDistance *= 10;
        final float tickDelta = 1.0F;
        final Vec3d rotation = origin.getRotationVec(tickDelta);
        final Vec3d startPos = origin.getEyePos();
        final Vec3d endPos = startPos.add(rotation.x * maxDistance, rotation.y * maxDistance, rotation.z * maxDistance);

        Box box = origin.getBoundingBox().stretch(rotation.multiply(maxDistance)).expand(1.0, 1.0, 1.0);

        HitResult entityHitResult = ProjectileUtil.raycast(origin, startPos, endPos, box, e -> !e.isSpectator() && includeEntity, maxDistance);

        if (entityHitResult != null) return entityHitResult;

        return origin.raycast(maxDistance, tickDelta, includeLiquids);
    }

    public static void drawLine(Vec3d start, Vec3d end, World world, double density, ParticleEffect particle) {
        double totalDistance = start.distanceTo(end);

        for (double distanceTraveled = 0; distanceTraveled < totalDistance; distanceTraveled += density) {
            double alpha = distanceTraveled / totalDistance;
            double x = interpolate(start.x, end.x, alpha);
            double y = interpolate(start.y, end.y, alpha);
            double z = interpolate(start.z, end.z, alpha);

            if (world.isClient())
                world.addParticle(particle, x, y, z, 0, 0, 0);
            else
                ((ServerWorld) world).spawnParticles(particle, x, y, z, 1, 0, 0, 0, 0);
        }
    }

    private static double interpolate(double start, double end, double alpha) {
        return start + (end - start) * alpha;
    }
}
