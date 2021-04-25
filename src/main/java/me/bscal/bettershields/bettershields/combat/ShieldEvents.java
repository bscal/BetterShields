package me.bscal.bettershields.bettershields.combat;

import me.crimsondawn45.fabricshieldlib.lib.event.ShieldEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ShieldEvents extends ShieldEvent
{
	/**
	 * Shield Event - contains the methods to be fired for a shield or shield enchantment with special effects.
	 *
	 * @param usesOnBlockDamage - Whether or not the event will use the onBlockDamage method
	 * @param usesOnDisable     - Whether or not the event will use the onDisable method
	 * @param usesWhileHolding  - Whether or not the event will use the whileHolding method
	 */
	public ShieldEvents(boolean usesOnBlockDamage, boolean usesOnDisable, boolean usesWhileHolding)
	{
		super(usesOnBlockDamage, usesOnDisable, usesWhileHolding);
	}

	@Override
	public void onBlockDamage(LivingEntity defender, DamageSource source, float amount, int level, Hand hand,
			ItemStack shield)
	{
		super.onBlockDamage(defender, source, amount, level, hand, shield);
	}
}
