package me.bscal.bettershields.bettershields.common.mixin.items;

import me.bscal.bettershields.bettershields.common.util.DuelWieldUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.SoftOverride;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnresolvedMixinReference") @Mixin(SwordItem.class) public abstract class SwordItemMixin
		extends ToolItem implements Vanishable
{

	public SwordItemMixin(ToolMaterial material, Settings settings)
	{
		super(material, settings);
	}

	@Intrinsic
	@SoftOverride
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		return super.use(world, user, hand);
	}

	@Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at = @At(value = "HEAD"), cancellable = true)
	public void onUse(World world, PlayerEntity user, Hand hand,
			CallbackInfoReturnable<TypedActionResult<ItemStack>> cir)
	{
		if (!world.isClient() && hand == Hand.OFF_HAND)
		{
			DuelWieldUtils.TryAttackOffhand(world, user, hand);
			cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand)));
			return;
		}
		cir.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
	}
}
