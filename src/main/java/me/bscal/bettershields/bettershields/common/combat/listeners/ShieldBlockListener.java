package me.bscal.bettershields.bettershields.common.combat.listeners;

import me.bscal.bettershields.bettershields.common.events.ShieldBlockCallback;
import me.bscal.bettershields.bettershields.common.mixin_accessors.PlayerEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class ShieldBlockListener implements ShieldBlockCallback.ShieldBlockEvent
{

	private static final float SHIELD_DAMAGE_REDUCTION = .5f;

	@Override
	public ShieldBlockCallback.ShieldEventResult OnShieldBlock(LivingEntity ent, DamageSource source,
			float amount, float baseAmount, Hand hand, ItemStack stack)
	{
		/*
			If damage is > than half the entities hp.
			Reduce the damage and disable shield.
		 */
		if (baseAmount > ent.getHealth() / 2f)
		{
			amount -= amount *= SHIELD_DAMAGE_REDUCTION;
			TryDisableShield(ent, 300);
			return new ShieldBlockCallback.ShieldEventResult(ActionResult.PASS, amount, baseAmount);
		}

		/*
			If Player and Attacker is a LivingEntity using an axe:
			than disable shield.
		 */
		if (ent.isPlayer() && source.getSource() instanceof LivingEntity)
		{
			LivingEntity attacker = (LivingEntity) source.getSource();
			ItemStack mainHand = attacker.getActiveItem();
			if (mainHand.getItem() instanceof AxeItem)
			{
				TryDisableShield(ent, 200);
				return new ShieldBlockCallback.ShieldEventResult(ActionResult.PASS,
						amount - amount * SHIELD_DAMAGE_REDUCTION, baseAmount);
			}
		}

		return new ShieldBlockCallback.ShieldEventResult(ActionResult.PASS, 0, baseAmount);
	}

	private void TryDisableShield(LivingEntity entity, int duration)
	{
		if (entity instanceof PlayerEntity)
		{
			((PlayerEntityAccessor) (PlayerEntity) entity).DisableShield(duration);
		}
	}
}
