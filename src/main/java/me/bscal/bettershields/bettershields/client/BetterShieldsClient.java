package me.bscal.bettershields.bettershields.client;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.networking.ProjectilePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.DrawableHelper;

@Environment(EnvType.CLIENT) public class BetterShieldsClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		ClientPlayNetworking.registerGlobalReceiver(BetterShields.STOP_USE_PACKET, (client, handler, buf, responseSender) ->
		{
			client.execute(() -> client.options.useKey.setPressed(false));
		});

		HudRenderCallback.EVENT.register(new SlingTargetUi());

		//ClientPlayNetworking.registerGlobalReceiver(BetterShields.SPAWN_PACKET, ((client, handler, buf, responseSender) -> ProjectilePacket.Deserialize(client, buf)));

	}
}
