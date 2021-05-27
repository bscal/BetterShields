package me.bscal.bettershields.bettershields.common.mixin.entity;

import me.bscal.bettershields.bettershields.common.mixin_accessors.PlayerEntityAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(PlayerEntity.class) public abstract class PlayerEntityMixin extends LivingEntity
		implements PlayerEntityAccessor
{

	@Unique private static final float OFFHAND_DMG_REDUCTION = 0.8f;

	@Unique private int m_lastOffhandAttackedTicks;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Shadow
	public abstract float getAttackCooldownProgressPerTick();

	@Inject(method = "tick", at = @At(value = "HEAD"))
	public void OnTick(CallbackInfo ci)
	{
		m_lastOffhandAttackedTicks++;
	}

	@Unique
	@Override
	public void DisableShield(int duration)
	{
		PlayerEntity player = (PlayerEntity) (Object) this;
		if (!player.isCreative())
		{
			player.getItemCooldownManager().set(Items.SHIELD, duration);
			player.clearActiveItem();
			player.world.sendEntityStatus(this, (byte) 30);
		}
	}

	/**
	 * Based off the default Minecraft implementation of <code>PlayerEntity.attack()</code>
	 * Offhand should deal 80% of final damage.
	 */
	@Unique
	@Override
	public void AttackOffhand(Entity target)
	{
		PlayerEntity player = (PlayerEntity) (Object) this;
		if (target.isAttackable())
		{
			if (!target.handleAttack(this))
			{
				float f = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
				float h;
				if (target instanceof LivingEntity)
				{
					h = EnchantmentHelper.getAttackDamage(this.getOffHandStack(),
							((LivingEntity) target).getGroup());
				}
				else
				{
					h = EnchantmentHelper.getAttackDamage(this.getOffHandStack(), EntityGroup.DEFAULT);
				}

				float i = GetOffhandAttackCooldownProgress(0.5F);
				f *= 0.2F + i * i * 0.8F;
				h *= i;
				m_lastOffhandAttackedTicks = 0; //this.resetLastAttackedTicks();
				if (f > 0.0F || h > 0.0F)
				{
					boolean bl = i > 0.9F;
					boolean bl2 = false;
					int j = 0;
					j = j + EnchantmentHelper.getKnockback(this);
					if (this.isSprinting() && bl)
					{
						this.world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(),
								SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0F,
								1.0F);
						++j;
						bl2 = true;
					}

					boolean bl3 = bl && this.fallDistance > 0.0F && !this.onGround && !this.isClimbing() && !this
							.isTouchingWater() && !this.hasStatusEffect(
							StatusEffects.BLINDNESS) && !this.hasVehicle() && target instanceof LivingEntity;
					bl3 = bl3 && !this.isSprinting();
					if (bl3)
					{
						f *= 1.5F;
					}

					f += h;
					boolean bl4 = false;
					double d = (double) (this.horizontalSpeed - this.prevHorizontalSpeed);
					if (bl && !bl3 && !bl2 && this.onGround && d < (double) this.getMovementSpeed())
					{
						ItemStack itemStack = this.getStackInHand(Hand.OFF_HAND);
						if (itemStack.getItem() instanceof SwordItem)
						{
							bl4 = true;
						}
					}

					float k = 0.0F;
					boolean bl5 = false;
					int l = EnchantmentHelper.getFireAspect(this);
					if (target instanceof LivingEntity)
					{
						k = ((LivingEntity) target).getHealth();
						if (l > 0 && !target.isOnFire())
						{
							bl5 = true;
							target.setOnFireFor(1);
						}
					}

					Vec3d vec3d = target.getVelocity();
					boolean bl6 = target.damage(DamageSource.player(player), f * OFFHAND_DMG_REDUCTION);
					if (bl6)
					{
						if (j > 0)
						{
							if (target instanceof LivingEntity)
							{
								((LivingEntity) target).takeKnockback((double) ((float) j * 0.5F),
										(double) MathHelper.sin(this.getYaw() * 0.017453292F),
										(double) (-MathHelper.cos(this.getYaw() * 0.017453292F)));
							}
							else
							{
								target.addVelocity((double) (-MathHelper.sin(
										this.getYaw() * 0.017453292F) * (float) j * 0.5F), 0.1D,
										(double) (MathHelper.cos(
												this.getYaw() * 0.017453292F) * (float) j * 0.5F));
							}

							this.setVelocity(this.getVelocity().multiply(0.6D, 1.0D, 0.6D));
							this.setSprinting(false);
						}

						if (bl4)
						{
							float m = 1.0F + EnchantmentHelper.getSweepingMultiplier(this) * f;
							List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class,
									target.getBoundingBox().expand(1.0D, 0.25D, 1.0D));
							Iterator var19 = list.iterator();

							label166:
							while (true)
							{
								LivingEntity livingEntity;
								do
								{
									do
									{
										do
										{
											do
											{
												if (!var19.hasNext())
												{
													this.world.playSound((PlayerEntity) null, this.getX(),
															this.getY(), this.getZ(),
															SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
															this.getSoundCategory(), 1.0F, 1.0F);
													player.spawnSweepAttackParticles();
													break label166;
												}

												livingEntity = (LivingEntity) var19.next();
											}
											while (livingEntity == this);
										}
										while (livingEntity == target);
									}
									while (this.isTeammate(livingEntity));
								}
								while (livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity)
										.isMarker());

								if (this.squaredDistanceTo(livingEntity) < 9.0D)
								{
									livingEntity.takeKnockback(0.4000000059604645D,
											(double) MathHelper.sin(this.getYaw() * 0.017453292F),
											(double) (-MathHelper.cos(this.getYaw() * 0.017453292F)));
									livingEntity.damage(DamageSource.player(player), m * OFFHAND_DMG_REDUCTION);
								}
							}
						}

						if (target instanceof ServerPlayerEntity && target.velocityModified)
						{
							((ServerPlayerEntity) target).networkHandler.sendPacket(
									new EntityVelocityUpdateS2CPacket(target));
							target.velocityModified = false;
							target.setVelocity(vec3d);
						}

						if (bl3)
						{
							this.world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(),
									SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F,
									1.0F);
							player.addCritParticles(target);
						}

						if (!bl3 && !bl4)
						{
							if (bl)
							{
								this.world.playSound((PlayerEntity) null, this.getX(), this.getY(),
										this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
										this.getSoundCategory(), 1.0F, 1.0F);
							}
							else
							{
								this.world.playSound((PlayerEntity) null, this.getX(), this.getY(),
										this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK,
										this.getSoundCategory(), 1.0F, 1.0F);
							}
						}

						if (h > 0.0F)
						{
							player.addEnchantedHitParticles(target);
						}

						this.onAttacking(target);
						if (target instanceof LivingEntity)
						{
							EnchantmentHelper.onUserDamaged((LivingEntity) target, this);
						}

						EnchantmentHelper.onTargetDamaged(this, target);
						ItemStack itemStack2 = this.getOffHandStack();
						Entity entity = target;
						if (target instanceof EnderDragonPart)
						{
							entity = ((EnderDragonPart) target).owner;
						}

						if (!this.world.isClient && !itemStack2.isEmpty() && entity instanceof LivingEntity)
						{
							itemStack2.postHit((LivingEntity) entity, player);
							if (itemStack2.isEmpty())
							{
								this.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
							}
						}

						if (target instanceof LivingEntity)
						{
							float n = k - ((LivingEntity) target).getHealth();
							player.increaseStat(Stats.DAMAGE_DEALT, Math.round(n * 10.0F));
							if (l > 0)
							{
								target.setOnFireFor(l * 4);
							}

							if (this.world instanceof ServerWorld && n > 2.0F)
							{
								int o = (int) ((double) n * 0.5D);
								((ServerWorld) this.world).spawnParticles(ParticleTypes.DAMAGE_INDICATOR,
										target.getX(), target.getBodyY(0.5D), target.getZ(), o, 0.1D, 0.0D,
										0.1D, 0.2D);
							}
						}

						player.addExhaustion(0.1F);
					}
					else
					{
						this.world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(),
								SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F,
								1.0F);
						if (bl5)
						{
							target.extinguish();
						}
					}
				}
			}
		}
	}

	@Unique
	@Override
	public float GetOffhandAttackCooldownProgress(float baseTime)
	{
		return MathHelper.clamp(
				((float) m_lastOffhandAttackedTicks + baseTime) / this.getAttackCooldownProgressPerTick(),
				0.0F, 1.0F);
	}
}
