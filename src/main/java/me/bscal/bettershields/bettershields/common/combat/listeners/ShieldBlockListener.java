package me.bscal.bettershields.bettershields.common.combat.listeners;

import me.bscal.bettershields.bettershields.common.events.ShieldBlockCallback;
import me.bscal.bettershields.bettershields.common.mixin.PlayerEntityAccessor;
import me.bscal.bettershields.bettershields.common.mixin.PlayerEntityMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class ShieldBlockListener implements ShieldBlockCallback.ShieldBlockEvent
{

	private static final float SHIELD_DAMAGE_REDUCTION = .7f;

	@Override
	public ShieldBlockCallback.ShieldEventResult OnShieldBlock(LivingEntity ent, DamageSource source,
			float amount, float baseAmount, Hand hand, ItemStack stack)
	{
		if (ent instanceof PlayerEntity)
		{
			((PlayerEntityAccessor)(PlayerEntity) ent).DisableShield(200);
		}

		if (baseAmount > ent.getHealth() / 2f)
		{
			amount -= amount *= SHIELD_DAMAGE_REDUCTION;
		}

		return new ShieldBlockCallback.ShieldEventResult(ActionResult.SUCCESS, amount, baseAmount);
	}
}
