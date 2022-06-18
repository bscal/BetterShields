package me.bscal.bettershields.bettershields.common.entity;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.items.RockBallItem;
import me.bscal.bettershields.bettershields.common.registry.ItemRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class RockEntity extends ThrownItemEntity
{

    private static final Vec3f STONE_COLOR = new Vec3f(Vec3d.unpackRgb(0x808080));
    private static final DustParticleEffect DUST_PARTICLE_EFFECT = new DustParticleEffect(STONE_COLOR, .5f);

    public float Damage = 2.0f;

    public RockEntity(EntityType<? extends RockEntity> entityType, World world)
    {
        super(entityType, world);
    }

    public RockEntity(World world, LivingEntity owner)
    {
        super(BetterShields.ROCK_ENTITY, owner, world);
    }

    public RockEntity(World world, double x, double y, double z)
    {
        super(BetterShields.ROCK_ENTITY, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem()
    {
        return ItemRegistry.ROCK_BALL;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status)
    {
        if (status == 3)
        {
            ItemStack itemStack = this.getItem();
            ParticleEffect particleEffect = itemStack.isEmpty() ? DUST_PARTICLE_EFFECT : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);
            for (int i = 0; i < 5; ++i)
            {
                this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult)
    {
        var entity = entityHitResult.getEntity();
        if (!entity.world.isClient)
        {
            var owner = this.getOwner();
            float velocity = (float) this.getVelocity().length();
            int damage = MathHelper.ceil(MathHelper.clamp(velocity * Damage, 0.0f, 20.0f));
            DamageSource damageSource;
            if (owner == null)
            {
                damageSource = DamageSource.thrownProjectile(this, this);
            }
            else
            {
                damageSource = DamageSource.thrownProjectile(this, owner);
                if (owner instanceof LivingEntity)
                {
                    ((LivingEntity) owner).onAttacking(entity);
                }
            }
            boolean isEnderman = entity.getType() == EntityType.ENDERMAN;
            if (entity.damage(damageSource, damage))
            {
                if (isEnderman) return;
                if (this.getDefaultItem() instanceof RockBallItem rock)
                {
                    this.playSound(rock.HitEntitySoundEvent, 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
                }
            }
            BetterShields.LOGGER.info("onEntityHit");
        }
    }

    @Override
    protected void onCollision(HitResult hitResult)
    {
        super.onCollision(hitResult);
        if (!this.world.isClient)
        {
            this.world.sendEntityStatus(this, (byte) 3);
            this.kill();
        }
    }
}
