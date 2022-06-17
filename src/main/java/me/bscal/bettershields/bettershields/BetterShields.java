package me.bscal.bettershields.bettershields;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import me.bscal.bettershields.bettershields.common.combat.listeners.ShieldBlockListener;
import me.bscal.bettershields.bettershields.common.events.ShieldBlockCallback;
import me.bscal.bettershields.bettershields.common.items.ItemGeneration;
import me.bscal.bettershields.bettershields.common.listeners.UseEntityListener;
import me.bscal.bettershields.bettershields.common.listeners.UseItemListener;
import me.bscal.bettershields.bettershields.common.registry.ItemRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.impl.tag.convention.TagRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagBuilder;
import net.minecraft.tag.TagEntry;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class BetterShields implements ModInitializer
{

	public static final String MOD_ID = "bettercombat";
	public static final String MOD_NAME = "BetterCombat";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	public static final boolean DEBUG = true;
	public static final Identifier STOP_USE_PACKET = new Identifier(BetterShields.MOD_ID, "stop_use");

	@Override
	public void onInitialize()
	{
		ItemGeneration.Init();
		ItemRegistry.Init();

		UseEntityCallback.EVENT.register(new UseEntityListener());
		UseItemCallback.EVENT.register(new UseItemListener());
		ShieldBlockCallback.SHIELD_BLOCK_EVENT.register(new ShieldBlockListener());
	}

}
