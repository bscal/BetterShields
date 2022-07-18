package me.bscal.bettershields.bettershields.common.mixin.entity;

import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DamageSource.class)
public interface DamageSourceInvoker
{
    @Invoker
    DamageSource invokeSetBypassesArmor();
}
