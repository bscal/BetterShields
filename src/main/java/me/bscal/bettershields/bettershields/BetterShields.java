package me.bscal.bettershields.bettershields;

import me.bscal.bettershields.bettershields.common.combat.listeners.ShieldBlockListener;
import me.bscal.bettershields.bettershields.common.events.ShieldBlockCallback;
import me.bscal.bettershields.bettershields.common.listeners.UseEntityListener;
import me.bscal.bettershields.bettershields.common.listeners.UseItemListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class BetterShields implements ModInitializer
{

	public static final String MOD_ID = "bettercombat";
	public static final String MOD_NAME = "BetterCombat";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	public static final boolean DEBUG = true;
	public static final Random RAND = new Random();

	@Override
	public void onInitialize()
	{
		UseEntityCallback.EVENT.register(new UseEntityListener());
		UseItemCallback.EVENT.register(new UseItemListener());
		ShieldBlockCallback.SHIELD_BLOCK_EVENT.register(new ShieldBlockListener());
	}
}
