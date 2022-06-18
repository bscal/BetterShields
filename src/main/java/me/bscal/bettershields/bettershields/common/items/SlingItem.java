package me.bscal.bettershields.bettershields.common.items;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.entity.RockEntity;
import me.bscal.bettershields.bettershields.common.registry.ItemRegistry;
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

public class SlingItem extends RangedWeaponItem implements Vanishable
{
    public static final int RANGE = 15;

    public SlingItem(Item.Settings settings)
    {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks)
    {
        if (!(user instanceof PlayerEntity playerEntity) || !(stack.getItem() instanceof RangedWeaponItem)) return;
        boolean isCreative = playerEntity.isCreative();
        ItemStack itemStack = isCreative ? new ItemStack(ItemRegistry.ROCK_BALL) : FindFirstItemStack(playerEntity, ItemRegistry.ROCK_BALL);
        if (itemStack.isEmpty()) return;
        float pullProgress = SlingItem.GetPullProgress(this.getMaxUseTime(stack) - remainingUseTicks, itemStack);
        if (pullProgress < 0.2) return;
        if (!world.isClient)
        {
            RockBallItem rockBallItem = (RockBallItem) (itemStack.getItem() instanceof RockBallItem ? itemStack.getItem() : ItemRegistry.ROCK_BALL);
            RockEntity projectile = rockBallItem.Create(world, itemStack, playerEntity);

            float spin = (((itemStack.getMaxUseTime() - user.getItemUseTimeLeft()) % 10.0f) / 10.0f) * 180f;
            float pitch =  playerEntity.getPitch();
            float yaw = playerEntity.getYaw();
            float xOffset = (float) (pitch + Math.cos(spin) * 1f);
            float yOffset = (float) (yaw + Math.sin(spin) * 1f);
            BetterShields.LOGGER.info(Math.cos(spin) * 1f + ", " + Math.sin(spin) * 1f);
            projectile.setVelocity(playerEntity, xOffset, yOffset, 0.0f, pullProgress * 3.0f, 1.0f);
            world.spawnEntity(projectile);
        }
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f);
        if (!isCreative)
        {
            itemStack.decrement(1);
            if (itemStack.isEmpty())
            {
                playerEntity.getInventory().removeOne(itemStack);
            }
        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks)
    {
        if (world.isClient)
        {

        }
    }

    private static float GetPullProgress(int useTicks, ItemStack stack) {
        float f = (float)useTicks / (float) 40f;
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
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

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean noAmmo = FindFirstItemStack(user, ItemRegistry.ROCK_BALL).isEmpty();
        if (user.getAbilities().creativeMode || noAmmo)
        {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public Predicate<ItemStack> getProjectiles()
    {
        return (itemStack) -> itemStack.isOf(ItemRegistry.ROCK_BALL);
    }

    @Override
    public int getRange()
    {
        return RANGE;
    }

    private static ItemStack FindFirstItemStack(PlayerEntity player, Item item)
    {
        var inv = player.getInventory();
        if (inv.isEmpty()) return ItemStack.EMPTY;
        for (int i = 0; i < inv.size(); ++i)
        {
            var rock = inv.getStack(i);
            if (rock.isEmpty()) continue;
            if (rock.isOf(item)) return rock;
        }
        return ItemStack.EMPTY;
    }

}
