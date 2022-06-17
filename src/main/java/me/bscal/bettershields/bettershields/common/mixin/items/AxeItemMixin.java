package me.bscal.bettershields.bettershields.common.mixin.items;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.api.OffhandWeapon;
import me.bscal.bettershields.bettershields.common.mixin_accessors.PlayerEntityAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//@SuppressWarnings("UnresolvedMixinReference")
@Mixin(AxeItem.class)
public abstract class AxeItemMixin extends MiningToolItem implements OffhandWeapon
{
    private boolean reset;

    protected AxeItemMixin(float attackDamage, float attackSpeed, ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings)
    {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
    }


/*	@Intrinsic
	@SoftOverride
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		return super.use(world, user, hand);
	}

	@Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at = @At(value = "HEAD"), cancellable = true)
	public void onUse(World world, PlayerEntity user, Hand hand,
			CallbackInfoReturnable<TypedActionResult<ItemStack>> cir)
	{
		//boolean onCd = (((PlayerEntityAccessor)user).GetOffhandAttackCooldownProgress(0.5f) < 1.0f); // TODO
		if (!world.isClient() && hand == Hand.OFF_HAND)
		{
			DuelWieldUtils.TryAttackOffhand(world, user, hand);
			DuelWieldUtils.StopAttackingInOffhand((ServerPlayerEntity) user);
			cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand)));
			return;
		}
		cir.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
	}*/

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
