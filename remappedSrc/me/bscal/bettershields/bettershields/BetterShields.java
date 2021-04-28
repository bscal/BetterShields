package me.bscal.bettershields.bettershields;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class BetterShields implements ModInitializer
{

	public static final String MOD_ID = "bettershields";
	public static final String MOD_NAME = "BetterShields";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	public static final boolean DEBUG = true;
	public static final Random RAND = new Random();

	@Override
	public void onInitialize()
	{

	}
}
