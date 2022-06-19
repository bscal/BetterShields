package me.bscal.bettershields.bettershields.common.items;

import me.bscal.bettershields.bettershields.common.entity.RockBallEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
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

    public RockBallEntity Create(World world, ItemStack stack, LivingEntity shooter) {
        return new RockBallEntity(world, shooter);
    }

}
