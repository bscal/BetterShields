package me.bscal.bettershields.bettershields.common.mixin;

import me.bscal.bettershields.bettershields.common.mixin_accessors.LivingEntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HeldItemRenderer.class )
public class HeldItemRendererMixin
{

	@ModifyVariable(
			method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V",
			at = @At(value = "STORE", ordinal = 2),
			ordinal = 3
	)
	private float ModifyOffhandSwingProgress(float original)
	{
		PlayerEntity player = MinecraftClient.getInstance().player;
		LivingEntityAccessor lAccessor = ((LivingEntityAccessor) player);
		return lAccessor.GetOffhandSwingProgress();
	}
}
