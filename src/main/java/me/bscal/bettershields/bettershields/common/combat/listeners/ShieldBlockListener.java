package me.bscal.bettershields.bettershields.common.combat.listeners;

import me.bscal.bettershields.bettershields.common.events.ShieldBlockCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class ShieldBlockListener implements ShieldBlockCallback.ShieldBlockEvent
{
	@Override
	public ShieldBlockCallback.ShieldEventResult OnShieldBlock(LivingEntity ent, DamageSource source,
			float amount, float baseAmount, Hand hand, ItemStack stack)
	{
		return new ShieldBlockCallback.ShieldEventResult(ActionResult.SUCCESS, 5, baseAmount);
	}
}
