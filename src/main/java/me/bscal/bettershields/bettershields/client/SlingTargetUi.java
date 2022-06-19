package me.bscal.bettershields.bettershields.client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.bscal.bettershields.bettershields.BetterShields;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;

public class SlingTargetUi implements HudRenderCallback
{


    public boolean Enabled;
    public int X;
    public int Y;
    private final Sprite Target;
    private final Sprite TargetOutline;
    private final int AtlasGlId;

    private static final float GRAY = 80f / 255f;

    public SlingTargetUi()
    {
        Target = BetterShieldsClient.AtlasTexture.getSprite(BetterShieldsClient.UI_CIRCLE);
        TargetOutline = BetterShieldsClient.AtlasTexture.getSprite(BetterShieldsClient.UI_CIRCLE_OUTLINE);
        AtlasGlId = BetterShieldsClient.AtlasTexture.getGlId();
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta)
    {
        if (Enabled)
        {
            var client = MinecraftClient.getInstance();
            if (client == null) return;

            int x = (client.getWindow().getScaledWidth() / 2) + X - 6;
            int y = (client.getWindow().getScaledHeight() / 2) + Y - 6;
            RenderSystem.setShaderTexture(0, AtlasGlId);
            RenderSystem.setShaderColor(GRAY, GRAY, GRAY, .6f);
            DrawableHelper.drawSprite(matrixStack, x, y, 1, 12, 12, Target);
            RenderSystem.setShaderColor(GRAY, GRAY, GRAY, .9f);
            DrawableHelper.drawSprite(matrixStack, x, y, 1, 12, 12, TargetOutline);
        }
    }
}
