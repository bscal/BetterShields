package me.bscal.bettershields.bettershields.common.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class) public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor
{

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Unique
	@Override
	public void DisableShield(int duration)
	{
		PlayerEntity player = (PlayerEntity) (Object) this;
		player.getItemCooldownManager().set(Items.SHIELD, duration);
		player.clearActiveItem();
		player.world.sendEntityStatus(this, (byte)30);
	}
}
