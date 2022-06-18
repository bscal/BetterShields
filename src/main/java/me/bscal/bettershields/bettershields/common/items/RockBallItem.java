package me.bscal.bettershields.bettershields.common.items;

import me.bscal.bettershields.bettershields.common.entity.RockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class RockBallItem extends Item
{

    public SoundEvent HitEntitySoundEvent;

    public RockBallItem(SoundEvent hitEntitySoundEvent)
    {
        super(new FabricItemSettings().group(ItemGroup.COMBAT));
        HitEntitySoundEvent = hitEntitySoundEvent;
    }

    public RockEntity Create(World world, ItemStack stack, LivingEntity shooter) {
        return new RockEntity(world, shooter);
    }

}
