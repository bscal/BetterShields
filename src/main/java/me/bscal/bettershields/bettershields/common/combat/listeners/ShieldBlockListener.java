package me.bscal.bettershields.bettershields.common.combat.listeners;

import me.bscal.bettershields.bettershields.common.events.ShieldBlockCallback;
import me.bscal.bettershields.bettershields.common.mixin.PlayerEntityAccessor;
import me.bscal.bettershields.bettershields.common.mixin.PlayerEntityMixin;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class ShieldBlockListener implements ShieldBlockCallback.ShieldBlockEvent
{

	private static final float SHIELD_DAMAGE_REDUCTION = .7f;

	@Override
	public ShieldBlockCallback.ShieldEventResult OnShieldBlock(LivingEntity ent, DamageSource source,
			float amount, float baseAmount, Hand hand, ItemStack stack)
	{
		if (baseAmount > ent.getHealth() / 2f)
		{
			amount -= amount *= SHIELD_DAMAGE_REDUCTION;
			TryDisableShield(ent,200);
		}

		if (source.getSource() instanceof LivingEntity)
		{
			LivingEntity attacker = (LivingEntity) source.getSource();
			ItemStack mainHand = attacker.getActiveItem();
			if (mainHand.isIn(FabricToolTags.AXES))
			{
				TryDisableShield(ent,200);
			}
		}

		return new ShieldBlockCallback.ShieldEventResult(ActionResult.SUCCESS, amount, baseAmount);
	}

	private void TryDisableShield(LivingEntity entity, int duration)
	{
		if (entity instanceof PlayerEntity)
		{
			((PlayerEntityAccessor)(PlayerEntity) entity).DisableShield(duration);
		}
	}
}
