package me.bscal.bettershields.bettershields.common.items;

import me.bscal.bettershields.bettershields.common.registry.ItemRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;

import java.util.function.Predicate;

public class JavelinItem extends RangedWeaponItem implements Vanishable
{
    public JavelinItem()
    {
        super(new FabricItemSettings().group(ItemGroup.COMBAT));
    }

    @Override
    public Predicate<ItemStack> getProjectiles()
    {
        return (itemStack) -> itemStack.isOf(ItemRegistry.ROCK_BALL);
    }

    @Override
    public int getRange()
    {
        return 15;
    }
}
