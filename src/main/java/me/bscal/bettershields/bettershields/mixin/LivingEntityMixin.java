package me.bscal.bettershields.bettershields.mixin;

import me.bscal.bettershields.bettershields.events.ShieldBlockCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"), cancellable = true)
	public void OnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		LivingEntity ent = (LivingEntity) (Object) this;

		if (amount > 0.0F && ent.blockedByShield(source))
		{
			((LivingEntityAccessor)ent).damageShield(amount);

			Hand hand = ent.getActiveHand();
			ItemStack stack = ent.getActiveItem();

			ActionResult res = ShieldBlockCallback.BLOCK_ATTEMPT_EVENT.invoker().OnShieldBlockAttempt(ent, source, amount, hand, stack);

			if (res == ActionResult.SUCCESS)
				cir.setReturnValue(false);
		}
	}

}
