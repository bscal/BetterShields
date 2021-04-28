package me.bscal.bettershields.bettershields.mixin;

import me.bscal.bettershields.bettershields.events.ShieldBlockCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class) public class LivingEntityMixin
{

	@Unique private float m_tempAmount;

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
	public void OnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		LivingEntity ent = (LivingEntity) (Object) this;
		Hand hand = ent.getActiveHand();
		ItemStack stack = ent.getActiveItem();
		ShieldBlockCallback.ShieldEventResult res = ShieldBlockCallback.BLOCK_ATTEMPT_EVENT.invoker()
				.OnShieldBlockAttempt(ent, source, amount, amount, hand, stack);
		m_tempAmount = res.amount;
	}

	@ModifyVariable(argsOnly = true, ordinal = 0, method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
	private float UpdateAmountVar(float amount)
	{
		return m_tempAmount;
	}

	@ModifyArg(index = 0, method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
	private float ModArgDamageShield(float amount)
	{
		return m_tempAmount;
	}

}
