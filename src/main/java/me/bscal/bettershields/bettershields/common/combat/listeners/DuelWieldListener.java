package me.bscal.bettershields.bettershields.common.combat.listeners;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DuelWieldListener implements UseEntityCallback
{

	@Override
	public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity,
			@Nullable EntityHitResult hitResult)
	{
		return null;
	}
}
