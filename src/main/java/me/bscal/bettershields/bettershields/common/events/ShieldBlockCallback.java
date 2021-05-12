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
	 * <br>
	 * Will return a <code>ShieldEventResult</code> and sets the amount (damage taken)
	 * variable in <code>LivingEntity.damage()</code>. If no listeners defaults to 0.
	 * <br>
	 * Upon return:<br>
	 * - SUCCESS will continue and set current damage amount to <code>ShieldBlockEvent.amount</code> value.
	 * 		Useful so multiple listeners can receive the updated amount variable.<br>
	 * - PASS will continue.<br>
	 * - FAIL returns current ShieldEventResult.<br>
	 */
	public static Event<ShieldBlockEvent> SHIELD_BLOCK_EVENT = EventFactory.createArrayBacked(
			ShieldBlockEvent.class,
			(listeners) -> (ent, source, amount, baseAmount, hand, stack) -> {
				// Defaults to 0 damage taken. Listeners will need to set to 0 however.
				ShieldEventResult res = new ShieldEventResult(ActionResult.PASS, 0, baseAmount);

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
		/**
		 * The Result of the event.
		 */
		public final ActionResult result;
		/**
		 * The amount of damage to take. This value will update the <code>amount</code> variable
		 * in <code>damage()</code> function. Event listeners can change this value.
		 */
		public final float amount;
		/**
		 * The initial damage taken before any events
		 */
		public final float baseAmount;

		public ShieldEventResult(ActionResult res, float amount, float baseAmount)
		{
			this.result = res;
			this.amount = amount;
			this.baseAmount = baseAmount;
		}
	}

}
