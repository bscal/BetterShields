package me.bscal.bettershields.bettershields.client;

import me.bscal.bettershields.bettershields.common.networking.BSNetManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT) public class BetterShieldsClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		ClientPlayNetworking.registerGlobalReceiver(BSNetManager.STOP_USE_PACKET, (client, handler, buf, responseSender) ->
		{
			client.execute(() -> {
				client.options.keyUse.setPressed(false);
			});
		});
	}
}
