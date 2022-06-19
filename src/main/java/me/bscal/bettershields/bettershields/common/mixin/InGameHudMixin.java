package me.bscal.bettershields.bettershields.common.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.api.OffhandWeapon;
import me.bscal.bettershields.bettershields.common.mixin_accessors.PlayerEntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class) public abstract class InGameHudMixin extends DrawableHelper
{

	@Shadow private int scaledHeight;
	@Shadow private int scaledWidth;

	@Unique private static final Identifier BETTER_COMBAT_ICONS_TEXTURE = new Identifier(BetterShields.MOD_ID,
			"textures/ui/icons.png");

	@Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0, shift = At.Shift.AFTER), cancellable = true)
	public void OnRenderCrosshair(MatrixStack matrices, CallbackInfo ci)
	{
		InGameHud hud = (InGameHud) (Object) this;
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null && client.player.getOffHandStack().getItem() instanceof OffhandWeapon
				&& client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR)
		{
			PlayerEntityAccessor player = (PlayerEntityAccessor) client.player;
			RenderSystem.setShaderTexture(0, BETTER_COMBAT_ICONS_TEXTURE);
			RenderMainHand(matrices, hud, client, player);
			RenderOffHand(matrices, hud, client, player);
			ci.cancel();
		}

	}

	private void RenderMainHand(MatrixStack matrices, InGameHud hud, MinecraftClient client,
			PlayerEntityAccessor player)
	{
		float f = client.player.getAttackCooldownProgress(0.0F);
		boolean bl = false;
		if (client.targetedEntity instanceof LivingEntity && f >= 1.0F)
		{
			bl = client.player.getAttackCooldownProgressPerTick() > 5.0F;
			bl &= client.targetedEntity.isAlive();
		}

		int j = scaledHeight / 2 - 7 + 16;
		int k = scaledWidth / 2 + 4;
		if (bl)
		{
			hud.drawTexture(matrices, k, j, 68, 94, 16, 16);
		}
		else if (f < 1.0F)
		{
			int l = (int) (f * 17.0F);
			hud.drawTexture(matrices, k, j, 36, 94, 16, 4);
			hud.drawTexture(matrices, k, j, 52, 94, l, 4);
		}
	}

	private void RenderOffHand(MatrixStack matrices, InGameHud hud, MinecraftClient client,
			PlayerEntityAccessor player)
	{
		matrices.push();

		float f = player.GetOffhandAttackCooldownProgress(0f);
		boolean bl = false;
		if (client.targetedEntity instanceof LivingEntity && f >= 1.0F)
		{
			bl = client.player.getAttackCooldownProgressPerTick() > 5.0F;
			bl &= client.targetedEntity.isAlive();
		}

		int j = scaledHeight / 2 - 7 + 16;
		int k = scaledWidth / 2 - 16 - 4;
		if (bl)
		{
			hud.drawTexture(matrices, k, j, 116, 94, 16, 16);
		}
		else if (f < 1.0F)
		{
			int l = (int) (18 - (f * 17));
			hud.drawTexture(matrices, k, j, 84, 94, 16, 4);
			hud.drawTexture(matrices, k + l, j, 100 + l, 94, 16 - l, 4);
		}

		matrices.pop();
	}

}
