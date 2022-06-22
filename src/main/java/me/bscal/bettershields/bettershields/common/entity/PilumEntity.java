package me.bscal.bettershields.bettershields.common.entity;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.items.JavelinItem;
import me.bscal.bettershields.bettershields.common.items.PilumItem;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;

public class PilumEntity extends PersistentProjectileEntity
{

    public ToolMaterial TipMaterial;

    public PilumEntity(EntityType<? extends PilumEntity> entityType, World world)
    {
        super(entityType, world);
    }

    public PilumEntity(World world, double x, double y, double z)
    {
        super(BetterShields.PILUM_ENTITY, x, y, z, world);
    }

    public PilumEntity(World world, LivingEntity owner)
    {
        super(BetterShields.PILUM_ENTITY, owner, world);
    }

    @Override
    protected ItemStack asItemStack()
    {
        return PilumItem.GetStackForTip(TipMaterial);
    }

}
