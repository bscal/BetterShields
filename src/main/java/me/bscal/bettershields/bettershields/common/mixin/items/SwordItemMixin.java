package me.bscal.bettershields.bettershields.common.mixin.items;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.api.OffhandWeapon;
import me.bscal.bettershields.bettershields.common.mixin_accessors.PlayerEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SwordItem.class) public abstract class SwordItemMixin extends ToolItem
		implements Vanishable, OffhandWeapon
{

	public SwordItemMixin(ToolMaterial material, Settings settings)
	{
		super(material, settings);
	}

	@Unique
	@Override
	public boolean IsOffhandWeapon()
	{
		return true;
	}

	@Unique
	@Override
	public float GetOffhandDamageReduction()
	{
		return 0.8f;
	}

	@Unique
	@Override
	public void AttackOffhand(World world, PlayerEntity user, Hand hand, LivingEntity target)
	{
		PlayerEntityAccessor pAccessor = (PlayerEntityAccessor) user;
		pAccessor.AttackOffhand(target, user.getStackInHand(Hand.OFF_HAND), this);

		if (BetterShields.DEBUG)
			BetterShields.LOGGER.info("Attacked with the offhand!");
	}
}
