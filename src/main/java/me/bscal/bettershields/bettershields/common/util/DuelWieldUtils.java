package me.bscal.bettershields.bettershields.common.util;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.api.OffhandWeapon;
import me.bscal.bettershields.bettershields.common.mixin_accessors.PlayerEntityAccessor;
import me.bscal.bettershields.bettershields.common.networking.BSNetManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class DuelWieldUtils
{

	private DuelWieldUtils()
	{
	}

	/**
	 * Sends a empty packet to the ServerPlayerEntity which will set <code>keyUse = false</code>.
	 * This stops from itemUse repeating when held down.
	 * @param sPlayer - ServerPlayer to turn update
	 */
	public static void StopAttackingInOffhand(ServerPlayerEntity sPlayer)
	{
		ServerPlayNetworking.send(sPlayer, BetterShields.STOP_USE_PACKET, PacketByteBufs.empty());
	}

	/**
	 * Based off Minecrafts default implementation of GameRenderer.updateTargetedEntity
	 * @return - true if successful attack, false otherwise
	 */
	public static boolean TryAttackOffhand(World world, PlayerEntity user, Hand hand)
	{
		double d = user.isCreative() ? 5.0F : 4.5F;
		float tickDelta = user.getServer().getTickTime();
		HitResult result = user.raycast(d, tickDelta, false);
		Vec3d vec3d = user.getCameraPosVec(tickDelta);
		double e = d;
		if (user.isCreative())
		{
			e = 6.0D;
			d = e;
		}
		e *= e;
		if (result != null)
		{
			e = result.getPos().squaredDistanceTo(vec3d);
		}

		Vec3d vec3d2 = user.getRotationVec(tickDelta);
		Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
		Box box = user.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0D, 1.0D, 1.0D);
		EntityHitResult entityHitResult = ProjectileUtil.raycast(user, vec3d, vec3d3, box,
				(entityx) -> !entityx.isSpectator() && entityx.collides(), e);
		if (entityHitResult != null)
		{
			Entity entity = entityHitResult.getEntity();
			ItemStack item = user.getStackInHand(hand);
			if ((entity instanceof LivingEntity || entity instanceof ItemFrameEntity) && item.getItem() instanceof OffhandWeapon)
			{

				PlayerEntityAccessor pAccessor = (PlayerEntityAccessor) user;
				pAccessor.AttackOffhand(entity, item, (OffhandWeapon) item.getItem());

				if (BetterShields.DEBUG)
					BetterShields.LOGGER.info("Attacked with the offhand!");

				return true;
			}
		}
		return false;
	}

}
