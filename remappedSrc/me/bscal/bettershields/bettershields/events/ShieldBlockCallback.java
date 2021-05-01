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

	public static Event<ShieldBlockAttempt> BLOCK_ATTEMPT_EVENT = EventFactory.createArrayBacked(ShieldBlockAttempt.class,
			(listeners) -> (ent, source, amount, baseAmount, hand, stack) -> {
				for (ShieldBlockAttempt listener : listeners)
				{
					ShieldEventResult res = listener.OnShieldBlockAttempt(ent, source, amount, baseAmount, hand, stack);

					if (res.result != ActionResult.PASS)
					{
						return res;
					}
				}

				return new ShieldEventResult(ActionResult.PASS, amount);
			});


	@FunctionalInterface
	public interface ShieldBlockAttempt {
		ShieldEventResult OnShieldBlockAttempt(LivingEntity ent, DamageSource source, float amount, float baseAmount, Hand hand, ItemStack stack);
	}

	public static class ShieldEventResult
	{
		public final ActionResult result;
		public final float value;

		public ShieldEventResult(ActionResult res, float val)
		{
			result = res;
			value = val;
		}
	}

}
