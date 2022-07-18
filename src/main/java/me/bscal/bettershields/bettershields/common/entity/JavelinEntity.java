package me.bscal.bettershields.bettershields.common.entity;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.items.JavelinItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;

public class JavelinEntity extends PersistentProjectileEntity
{

    public static class TipMaterials
    {
        public static final byte NULL = 0;
        public static final byte FLINT = 1;
        public static final byte STONE = 2;
        public static final byte IRON = 3;
        public static final byte GOLD = 4;
        public static final byte DIAMOND = 5;
        public static final byte NETHERITE = 6;
    }

    private static final TrackedData<Byte> TIP_MATERIAL = DataTracker.registerData(JavelinEntity.class, TrackedDataHandlerRegistry.BYTE);

    public JavelinEntity(EntityType<? extends JavelinEntity> entityType, World world)
    {
        super(entityType, world);
    }

    public JavelinEntity(EntityType<? extends JavelinEntity> entityType, World world, double x, double y, double z)
    {
        super(entityType, x, y, z, world);
    }

    public JavelinEntity(EntityType<? extends JavelinEntity> entityType, World world, LivingEntity owner)
    {
        super(entityType, owner, world);
    }

    public void SetTipMaterial(byte tipMaterial)
    {
        this.getDataTracker().set(TIP_MATERIAL, tipMaterial);
    }

    public byte GetTipMaterial()
    {
        return this.getDataTracker().get(TIP_MATERIAL);
    }

    @Override
    public byte getPierceLevel()
    {
        return 0;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt)
    {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("TipMaterial", GetTipMaterial());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt)
    {
        super.readCustomDataFromNbt(nbt);
        SetTipMaterial(nbt.getByte("TipMaterial"));
    }

    @Override
    protected void initDataTracker()
    {
        super.initDataTracker();
        this.getDataTracker().startTracking(TIP_MATERIAL, TipMaterials.NULL);
    }

    @Override
    protected ItemStack asItemStack()
    {
        return JavelinItem.GetStackForTip(GetTipMaterial());
    }
}
