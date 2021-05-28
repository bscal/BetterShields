package me.bscal.bettershields.bettershields.common.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface OffhandWeapon
{

	boolean IsOffhandWeapon();

	float GetOffhandDamageReduction();

	void AttackOffhand(World world, PlayerEntity user, Hand hand, LivingEntity target);

}
