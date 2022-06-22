package me.bscal.bettershields.bettershields.common.entity;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.items.JavelinItem;
import me.bscal.bettershields.bettershields.common.registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.world.World;

public class JavelinEntity extends PersistentProjectileEntity
{

    public ToolMaterial TipMaterial;

    public JavelinEntity(EntityType<? extends JavelinEntity> entityType, World world)
    {
        super(entityType, world);
    }

    public JavelinEntity(World world, double x, double y, double z)
    {
        super(BetterShields.JAVELIN_ENTITY, x, y, z, world);
    }

    public JavelinEntity(World world, LivingEntity owner)
    {
        super(BetterShields.JAVELIN_ENTITY, owner, world);
    }

    @Override
    protected ItemStack asItemStack()
    {
        return JavelinItem.GetStackForTip(TipMaterial);
    }
}
