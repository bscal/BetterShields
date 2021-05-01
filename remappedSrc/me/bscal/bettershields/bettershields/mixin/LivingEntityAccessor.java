package me.bscal.bettershields.bettershields.common.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor
{

	@Invoker("damageShield")
	void damageShield(float amount);


}
