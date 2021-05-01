package me.bscal.bettershields.bettershields.common.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public final class ShieldBlockCallback
{

	/**
	 * Callback when a shield blocks an attack.
	 * Called after block detected but before any logic.
	 * Upon return:
	 * - SUCCESS will continue and set current damage amount to `ShieldBlockEvent.amount` value
	 * - PASS will continue.
	 * - FAIL cancels and returns current ShieldEventResult.
	 */
	public static Event<ShieldBlockEvent> SHIELD_BLOCK_EVENT = EventFactory.createArrayBacked(
			ShieldBlockEvent.class,
			(listeners) -> (ent, source, amount, baseAmount, hand, stack) -> {
				ShieldEventResult res = new ShieldEventResult(ActionResult.PASS, amount, amount);

				for (ShieldBlockEvent listener : listeners)
				{
					res = listener.OnShieldBlock(ent, source, amount, baseAmount, hand, stack);

					if (res.result == ActionResult.SUCCESS)
					{
						amount = res.amount;
					}
					else if (res.result != ActionResult.PASS)
					{
						return res;
					}
				}
				return res;
			});


	@FunctionalInterface
	public interface ShieldBlockEvent
	{
		ShieldEventResult OnShieldBlock(LivingEntity ent, DamageSource source, float amount, float baseAmount, Hand hand, ItemStack stack);
	}

	public static class ShieldEventResult
	{
		public final ActionResult result;
		public final float amount;
		public final float baseAmount;

		public ShieldEventResult(ActionResult res, float amount, float baseAmount)
		{
			this.result = res;
			this.amount = amount;
			this.baseAmount = baseAmount;
		}
	}

}
