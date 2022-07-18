package me.bscal.bettershields.bettershields.common.entity;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.items.PilumItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PilumEntity extends JavelinEntity
{

    public PilumEntity(EntityType<? extends PilumEntity> entityType, World world)
    {
        super(entityType, world);
    }

    public PilumEntity(World world, double x, double y, double z)
    {
        super(BetterShields.PILUM_ENTITY, world, x, y, z);
    }

    public PilumEntity(World world, LivingEntity owner)
    {
        super(BetterShields.PILUM_ENTITY, world, owner);
    }

    @Override
    public byte getPierceLevel()
    {
        return 1;
    }

    @Override
    protected ItemStack asItemStack()
    {
        return PilumItem.GetStackForTip(GetTipMaterial());
    }

}
