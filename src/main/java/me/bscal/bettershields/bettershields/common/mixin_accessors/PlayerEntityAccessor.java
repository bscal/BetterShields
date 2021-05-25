package me.bscal.bettershields.bettershields.common.mixin_accessors;

import net.minecraft.entity.Entity;

public interface PlayerEntityAccessor
{
	void DisableShield(int duration);

	void AttackOffhand(Entity target);
}
