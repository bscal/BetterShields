package me.bscal.bettershields.bettershields.common.mixin.items;

import me.bscal.bettershields.bettershields.common.util.DuelWieldUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.SoftOverride;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnresolvedMixinReference") @Mixin(AxeItem.class) public abstract class AxeItemMixin
		extends MiningToolItem
{
	protected AxeItemMixin(float attackDamage, float attackSpeed, ToolMaterial material,
			Tag<Block> effectiveBlocks, Settings settings)
	{
		super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
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
		if (!world.isClient())
		{
			if (DuelWieldUtils.TryAttackOffhand(world, user, hand))
			{
				cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand)));
				return;
			}
		}
		cir.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
	}

}
