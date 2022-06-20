package me.bscal.bettershields.bettershields.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class JavelinEntity extends PersistentProjectileEntity
{
    protected JavelinEntity(EntityType<? extends JavelinEntity> entityType, World world)
    {
        super(entityType, world);
    }

    protected JavelinEntity(EntityType<? extends JavelinEntity> type, double x, double y, double z, World world)
    {
        super(type, x, y, z, world);
    }

    protected JavelinEntity(EntityType<? extends JavelinEntity> type, LivingEntity owner, World world)
    {
        super(type, owner, world);
    }

    @Override
    protected ItemStack asItemStack()
    {
        return null;
    }
}
