package me.bscal.bettershields.bettershields.common.listeners;

import me.bscal.bettershields.bettershields.common.api.OffhandWeapon;
import me.bscal.bettershields.bettershields.common.util.DuelWieldUtils;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class UseEntityListener implements UseEntityCallback
{
	@Override
	public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity,
			@Nullable EntityHitResult hitResult)
	{
		if (!world.isClient())
		{
			if (entity instanceof LivingEntity && !entity.isSpectator() && hand == Hand.OFF_HAND)
			{
				ItemStack itemStack = player.getStackInHand(hand);
				if (itemStack.getItem() instanceof OffhandWeapon)
				{
					OffhandWeapon offhandWeapon = (OffhandWeapon) itemStack.getItem();
					if (offhandWeapon.IsOffhandWeapon())
					{
						offhandWeapon.AttackOffhand(world, player, hand, (LivingEntity) entity);
						player.swingHand(hand, true);
						DuelWieldUtils.StopAttackingInOffhand((ServerPlayerEntity) player);
						return ActionResult.SUCCESS;
					}
				}
			}
		}
		return ActionResult.PASS;
	}
}
