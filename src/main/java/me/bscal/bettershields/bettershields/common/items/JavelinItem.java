package me.bscal.bettershields.bettershields.common.items;

import me.bscal.bettershields.bettershields.common.entity.JavelinEntity;
import me.bscal.bettershields.bettershields.common.registry.ItemRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class JavelinItem extends RangedWeaponItem implements Vanishable
{

    public ToolMaterial TipMaterial;

    public JavelinItem(ToolMaterial tipMaterial)
    {
        this(tipMaterial, new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(8));
    }

    public JavelinItem(ToolMaterial tipMaterial, FabricItemSettings settings)
    {
        super(settings);
        TipMaterial = tipMaterial;
    }

    private JavelinEntity CreateJavelin(World world, ItemStack stack, LivingEntity shooter)
    {
        JavelinEntity javelinEntity = new JavelinEntity(world, shooter);
        javelinEntity.TipMaterial = TipMaterial;
        return javelinEntity;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!itemStack.isEmpty())
        {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks)
    {
        if (!(user instanceof PlayerEntity playerEntity) || !(stack.getItem() instanceof JavelinItem javelinItem) || stack != playerEntity.getMainHandStack())
            return;
        float pullProgress = JavelinItem.GetPullProgress(this.getMaxUseTime(stack) - remainingUseTicks, stack);
        if (pullProgress < 0.1) return;
        if (!world.isClient)
        {
            JavelinEntity javelin = CreateJavelin(world, stack, playerEntity);
            javelin.TipMaterial = javelinItem.TipMaterial;
            float pitch = playerEntity.getPitch() + 1.0f;
            float yaw = playerEntity.getYaw();
            javelin.setVelocity(playerEntity, pitch, yaw, 0.0f, pullProgress * 2f, 2.0f);
            world.spawnEntity(javelin);
        }
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f);
        if (!playerEntity.isCreative())
            stack.decrement(1);
    }


    @Override
    public int getMaxUseTime(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.BOW;
    }

    protected static float GetPullProgress(int useTicks, ItemStack stack)
    {
        float f = (float) useTicks / (float) 40f;
        if (f > 1.0f)
        {
            f = 1.0f;
        }
        return f;
    }

    @Override
    public Predicate<ItemStack> getProjectiles()
    {
        return (itemStack) -> itemStack.getItem() instanceof JavelinItem javelinItem && javelinItem.TipMaterial == TipMaterial;
    }

    @Override
    public int getRange()
    {
        return 15;
    }

    public static ItemStack GetStackForTip(ToolMaterial tipMaterial)
    {
        if (tipMaterial == ToolMaterials.IRON)
            return new ItemStack(ItemRegistry.IRON_JAVELIN);
        if (tipMaterial == ToolMaterials.DIAMOND)
            return new ItemStack(ItemRegistry.DIAMOND_JAVELIN);
        return new ItemStack(ItemRegistry.FLINT_JAVELIN);
    }

}
