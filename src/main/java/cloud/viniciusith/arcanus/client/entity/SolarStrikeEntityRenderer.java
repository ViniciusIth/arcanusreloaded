package cloud.viniciusith.arcanus.client.entity;

import cloud.viniciusith.arcanus.ArcanusReloaded;
import cloud.viniciusith.arcanus.client.ArcanusReloadedClient;
import cloud.viniciusith.arcanus.entity.SolarStrikeEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class SolarStrikeEntityRenderer extends EntityRenderer<SolarStrikeEntity> {

    public SolarStrikeEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(SolarStrikeEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        renderBeam(entity, matrices, vertexConsumers, 0, (float) ((entity.getWorld()
                .getHeight() + 2048) - entity.getY()), 0, tickDelta);
    }

    public void renderBeam(SolarStrikeEntity entity, MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float z, float tickDelta) {
        // Calculate the length of the beam
        float length = MathHelper.sqrt(x * x + y * y + z * z);

        // Calculate the beam's age and determine the scale and alpha transparency
        float age = (entity.age - 1) + tickDelta;
        float scale = age < 3 ? age / 3F : age > 9 ? 1 - ((age - 9F) / 15F) : 1F;

        final int segments = 16; // Number of segments in the beam
        final float radius = 2.25F; // Radius of the beam
        matrices.push();

        // Translate to the top of the beam and rotate it to point slightly downward
        matrices.multiply(new Quaternionf().rotationX((float) Math.toRadians(-70)));
        matrices.scale(scale, scale, 1);

        VertexConsumer vertexConsumer = provider.getBuffer(ArcanusReloadedClient.getMagicCircles(new Identifier(ArcanusReloaded.MODID, "solar_strike")));
        MatrixStack.Entry matrixEntry = matrices.peek();
        Matrix4f matrix = matrixEntry.getPositionMatrix();

        float vertX1 = 0F;
        float vertY1 = radius;

        for (int i = 1; i <= segments; i++) {
            float vertX2 = MathHelper.sin(i * 6.2831855F / segments) * radius;
            float vertY2 = MathHelper.cos(i * 6.2831855F / segments) * radius;

            vertexConsumer.vertex(matrix, vertX1, vertY1, 0F).next();
            vertexConsumer.vertex(matrix, vertX1, vertY1, length).next();
            vertexConsumer.vertex(matrix, vertX2, vertY2, length).next();
            vertexConsumer.vertex(matrix, vertX2, vertY2, 0F).next();

            vertX1 = vertX2;
            vertY1 = vertY2;
        }

        matrices.pop();
    }

    @Override
    public boolean shouldRender(SolarStrikeEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    @Override
    public Identifier getTexture(SolarStrikeEntity entity) {
        return null;
    }
}
