package me.bscal.bettershields.bettershields.common.mixin_accessors;

import me.bscal.bettershields.bettershields.common.api.OffhandWeapon;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface PlayerEntityAccessor
{
	void DisableShield(int duration);

	void AttackOffhand(Entity target, final ItemStack offhandStack, final OffhandWeapon offhandWeapon);

	float GetOffhandAttackCooldownProgress(float baseTime);

	void ResetOffhandTicks();
}
