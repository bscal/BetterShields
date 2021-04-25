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
			(listeners) -> (ent, source, amount, hand, stack) -> {
				for (ShieldBlockAttempt listener : listeners)
				{
					ActionResult res = listener.OnShieldBlockAttempt(ent, source, amount, hand, stack);

					if (res != ActionResult.PASS)
					{
						return res;
					}
				}

				return ActionResult.PASS;
			});


	@FunctionalInterface
	public interface ShieldBlockAttempt {
		ActionResult OnShieldBlockAttempt(LivingEntity ent, DamageSource source, float amount, Hand hand, ItemStack stack);
	}

}
