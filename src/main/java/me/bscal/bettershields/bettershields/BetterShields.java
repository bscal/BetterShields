package me.bscal.bettershields.bettershields;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import me.bscal.bettershields.bettershields.common.combat.listeners.ShieldBlockListener;
import me.bscal.bettershields.bettershields.common.entity.RockEntity;
import me.bscal.bettershields.bettershields.common.events.ShieldBlockCallback;
import me.bscal.bettershields.bettershields.common.items.ItemGeneration;
import me.bscal.bettershields.bettershields.common.listeners.UseEntityListener;
import me.bscal.bettershields.bettershields.common.listeners.UseItemListener;
import me.bscal.bettershields.bettershields.common.registry.ItemRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.impl.tag.convention.TagRegistration;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagBuilder;
import net.minecraft.tag.TagEntry;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.util.Random;

public class BetterShields implements ModInitializer
{

	public static final String MOD_ID = "bettercombat";
	public static final String MOD_NAME = "BetterCombat";
	public static final org.apache.logging.log4j.core.Logger LOGGER = (Logger) LogManager.getLogger("AdvancedPlayer");
	public static final boolean DEBUG = true;
	public static final Identifier STOP_USE_PACKET = new Identifier(BetterShields.MOD_ID, "stop_use");
	public static final Identifier SPAWN_PACKET = new Identifier(BetterShields.MOD_ID, "spawn_packet");

	public static final EntityType<RockEntity> ROCK_ENTITY = Registry.register(Registry.ENTITY_TYPE,
			new Identifier(MOD_ID, "rock_ball"),
			FabricEntityTypeBuilder.<RockEntity>create(SpawnGroup.MISC, RockEntity::new)
					.dimensions(EntityDimensions.fixed(.25f, .25f))
					.trackRangeChunks(4).trackedUpdateRate(20)
					.build());

	@Override
	public void onInitialize()
	{
		ItemGeneration.Init();
		ItemRegistry.Init();

		UseEntityCallback.EVENT.register(new UseEntityListener());
		UseItemCallback.EVENT.register(new UseItemListener());
		ShieldBlockCallback.SHIELD_BLOCK_EVENT.register(new ShieldBlockListener());

		EntityRendererRegistry.register(ROCK_ENTITY, FlyingItemEntityRenderer::new);
	}

}
