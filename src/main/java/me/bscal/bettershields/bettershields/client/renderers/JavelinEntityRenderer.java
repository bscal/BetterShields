package me.bscal.bettershields.bettershields.client.renderers;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.entity.JavelinEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

@Environment(value = EnvType.CLIENT)
public class JavelinEntityRenderer extends EntityRenderer<JavelinEntity>
{
    public static final Identifier TEXTURE_FLINT = new Identifier(BetterShields.MOD_ID, "textures/entity/projectiles/flint_javelin_entity.png");
    public static final Identifier TEXTURE_IRON = new Identifier(BetterShields.MOD_ID, "textures/entity/projectiles/iron_javelin_entity.png");
    public static final Identifier TEXTURE_DIAMOND = new Identifier(BetterShields.MOD_ID, "textures/entity/projectiles/diamond_javelin_entity.png");

    public JavelinEntityRenderer(EntityRendererFactory.Context context)
    {
        super(context);
    }

    @Override
    public Identifier getTexture(JavelinEntity entity)
    {
        byte tipMaterial = entity.GetTipMaterial();
        if (tipMaterial == JavelinEntity.TipMaterials.IRON)
            return TEXTURE_IRON;
        if (tipMaterial == JavelinEntity.TipMaterials.DIAMOND)
            return TEXTURE_DIAMOND;
        return TEXTURE_FLINT;
    }

    @Override
    public void render(JavelinEntity javelinEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i)
    {
        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, javelinEntity.prevYaw, javelinEntity.getYaw()) - 90.0f));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, javelinEntity.prevPitch, javelinEntity.getPitch())));
        float s = (float) javelinEntity.shake - g;
        if (s > 0.0f)
        {
            float t = -MathHelper.sin(s * 3.0f) * s;
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(t));
        }
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0f));
        matrixStack.scale(0.05625f, 0.05625f, 0.05625f);
        matrixStack.translate(-4.0, 0.0, 0.0);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(this.getTexture(javelinEntity)));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        for (int u = 0; u < 4; ++u)
        {
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f));
            this.vertex(matrix4f, matrix3f, vertexConsumer, -12, -2, 0, 0.0f, 0.0f, 0, 1, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 12, -2, 0, 1.0f, 0.0f, 0, 1, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 12, 2, 0, 1.0f, 1.0f, 0, 1, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -12, 2, 0, 0.0f, 1.0f, 0, 1, 0, i);
        }
        matrixStack.pop();
        super.render(javelinEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public void vertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertexConsumer, int x, int y, int z, float u, float v, int normalX, int normalZ, int normalY, int light)
    {
        vertexConsumer.vertex(positionMatrix, x, y, z).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, normalX, normalY, normalZ).next();
    }

}