package me.bscal.bettershields.bettershields.common.mixin.entity;

import me.bscal.bettershields.bettershields.common.events.ShieldBlockCallback;
import me.bscal.bettershields.bettershields.common.mixin_accessors.LivingEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class) public class LivingEntityMixin implements LivingEntityAccessor
{

	@Unique private float m_tempAmount;

	@Unique private boolean m_OffhandSwinging;
	@Unique private int m_OffhandSwingTicks;
	@Unique private float m_OffhandSwingProgress;
	@Unique private float m_LastOffhandSwingProgress;

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
	public void OnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		LivingEntity ent = (LivingEntity) (Object) this;
		Hand hand = ent.getActiveHand();
		ItemStack stack = ent.getActiveItem();
		ShieldBlockCallback.ShieldEventResult res = ShieldBlockCallback.SHIELD_BLOCK_EVENT.invoker()
				.OnShieldBlock(ent, source, amount, amount, hand, stack);
		m_tempAmount = res.amount;
	}

	@Inject(method = "baseTick", at = @At(value = "HEAD"))
	public void OnBaseTick(CallbackInfo ci)
	{
		this.m_LastOffhandSwingProgress = this.m_OffhandSwingProgress;
	}


	@Inject(method = "tickHandSwing", at = @At(value = "TAIL"))
	public void OnTickHandSwing(CallbackInfo ci)
	{
		LivingEntity ent = (LivingEntity) (Object) this;
		int i = ((LivingEntityInvoker)ent).invokeGetHandSwingDuration();
		if (m_OffhandSwinging)
		{
			++m_OffhandSwingTicks;
			if (m_OffhandSwingTicks >= i)
			{
				m_OffhandSwingTicks = 0;
				m_OffhandSwinging = false;
			}
		}
		else
		{
			m_OffhandSwingTicks = 0;
		}

		this.m_OffhandSwingProgress = (float)this.m_OffhandSwingTicks / (float)i;
	}

	@Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At(value = "HEAD"), cancellable = true)
	public void OnSwingHand(Hand hand, boolean fromServerPlayer, CallbackInfo ci)
	{
		if (hand == Hand.OFF_HAND)
		{
			LivingEntity entity = (LivingEntity) (Object) this;
			if (!this.m_OffhandSwinging || this.m_OffhandSwingTicks >= ((LivingEntityInvoker) entity).invokeGetHandSwingDuration() / 2 || this.m_OffhandSwingTicks < 0)
			{
				this.m_OffhandSwingTicks = -1;
				this.m_OffhandSwinging = true;
				//entity.preferredHand = hand;
				if (entity.world instanceof ServerWorld)
				{
					EntityAnimationS2CPacket entityAnimationS2CPacket = new EntityAnimationS2CPacket(entity,
							EntityAnimationS2CPacket.SWING_OFF_HAND);
					ServerChunkManager serverChunkManager = ((ServerWorld) entity.world).getChunkManager();
					if (fromServerPlayer)
					{
						serverChunkManager.sendToNearbyPlayers(entity, entityAnimationS2CPacket);
					}
					else
					{
						serverChunkManager.sendToOtherNearbyPlayers(entity, entityAnimationS2CPacket);
					}
				}
				ci.cancel();
			}

		}
	}

	/**
	 * Sets the variable `amount` to `m_tempAmount`
	 *
	 * @param amount - this value will be 0 if no other mixins modify it.
	 * @return the amount of damage that should be taken after the block.
	 */
	@ModifyVariable(argsOnly = true, ordinal = 0, method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isProjectile()Z"))
	private float UpdateAmountVar(float amount)
	{
		return m_tempAmount;
	}

	@Unique
	public float GetTempDamage()
	{
		return m_tempAmount;
	}

	@Override
	public float GetOffhandSwingProgress()
	{
		float f = m_OffhandSwingProgress - m_LastOffhandSwingProgress;
		if (f < 0.0F) {
			++f;
		}

		return m_LastOffhandSwingProgress + f;
	}

	@Override
	public boolean IsOffhandSwinging()
	{
		return m_OffhandSwinging;
	}
}
