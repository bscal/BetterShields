package me.bscal.bettershields.bettershields.common.mixin.entity;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityInvoker
{
	@Invoker("getHandSwingDuration")
	int invokeGetHandSwingDuration();
}
