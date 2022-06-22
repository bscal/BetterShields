package me.bscal.bettershields.bettershields.client;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.client.renderers.JavelinEntityRenderer;
import me.bscal.bettershields.bettershields.client.renderers.PilumEntityRenderer;
import me.bscal.bettershields.bettershields.common.entity.JavelinEntity;
import me.bscal.bettershields.bettershields.common.entity.PilumEntity;
import me.bscal.bettershields.bettershields.common.networking.ProjectilePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;

import java.util.stream.Stream;

@Environment(EnvType.CLIENT) public class BetterShieldsClient implements ClientModInitializer
{

	public static final Identifier UI_CIRCLE = IdUi("ui_circle");
	public static final Identifier UI_CIRCLE_OUTLINE = IdUi("ui_circle_outline");
	public static SpriteAtlasTexture AtlasTexture;
	public static SpriteAtlasTexture.Data AtlasTextureData;
	public static SlingTargetUi SlingTargetUi;

	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.register(BetterShields.ROCK_ENTITY, FlyingItemEntityRenderer::new);
		EntityRendererRegistry.register(BetterShields.JAVELIN_ENTITY, JavelinEntityRenderer::new);
		EntityRendererRegistry.register(BetterShields.PILUM_ENTITY, PilumEntityRenderer::new);

		ClientPlayNetworking.registerGlobalReceiver(BetterShields.STOP_USE_PACKET, (client, handler, buf, responseSender) ->
		{
			client.execute(() -> client.options.useKey.setPressed(false));
		});

		// Create sprite sheet
		ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
			var rm = client.getResourceManager();
			var id = new Identifier(BetterShields.MOD_ID, "textures");
			AtlasTexture = new SpriteAtlasTexture(id);
			Stream<Identifier> textures = Stream.of
			(
					UI_CIRCLE,
					UI_CIRCLE_OUTLINE
			);
			AtlasTextureData = AtlasTexture.stitch(rm, textures, client.getProfiler(), 0);
			AtlasTexture.upload(AtlasTextureData);
			AtlasTexture.registerTexture(client.getTextureManager(), rm, id, (runnable) -> {
			});
			// Registered later so textures are loaded.
			HudRenderCallback.EVENT.register(SlingTargetUi = new SlingTargetUi());
		});
	}

	public static Identifier IdUi(String filename)
	{
		return new Identifier(BetterShields.MOD_ID,  "ui/" + filename);
	}
}
