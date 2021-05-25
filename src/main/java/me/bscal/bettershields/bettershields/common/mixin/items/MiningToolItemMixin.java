package me.bscal.bettershields.bettershields.common.mixin.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.SoftOverride;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin extends ToolItem implements Vanishable
{
	public MiningToolItemMixin(ToolMaterial material, Settings settings)
	{
		super(material, settings);
	}

	@Intrinsic
	@SoftOverride
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		return super.use(world, user, hand);
	}
}
