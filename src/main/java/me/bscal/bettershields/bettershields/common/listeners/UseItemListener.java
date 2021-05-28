package me.bscal.bettershields.bettershields.common.listeners;

import me.bscal.bettershields.bettershields.common.mixin_accessors.PlayerEntityAccessor;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class UseItemListener implements UseItemCallback
{
	@Override
	public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand)
	{
		((PlayerEntityAccessor)player).ResetOffhandTicks();
		return TypedActionResult.pass(player.getStackInHand(hand));
	}
}
