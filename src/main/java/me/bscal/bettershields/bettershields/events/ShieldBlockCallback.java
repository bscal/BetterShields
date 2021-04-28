package me.bscal.bettershields.bettershields.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public final class ShieldBlockCallback
{

	public static Event<ShieldBlockAttempt> BLOCK_ATTEMPT_EVENT = EventFactory.createArrayBacked(ShieldBlockAttempt.class,
			(listeners) -> (ent, source, amount, baseAmount, hand, stack) -> {
				ShieldEventResult res = new ShieldEventResult(ActionResult.PASS, amount, amount);

				for (ShieldBlockAttempt listener : listeners)
				{
					res = listener.OnShieldBlockAttempt(ent, source, amount, baseAmount, hand, stack);

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
	public interface ShieldBlockAttempt {
		ShieldEventResult OnShieldBlockAttempt(LivingEntity ent, DamageSource source, float amount, float baseAmount, Hand hand, ItemStack stack);
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
