package me.bscal.bettershields.bettershields.common.items;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.client.BetterShieldsClient;
import me.bscal.bettershields.bettershields.common.entity.RockBallEntity;
import me.bscal.bettershields.bettershields.common.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.Vanishable;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class SlingItem extends RangedWeaponItem implements Vanishable
{
    public static final int RANGE = 15;
    private static final float BASE_SPEED = 16.0f;
    private static final float BASE_SPEED2 = BASE_SPEED * 2;
    private static final int MAX_SPIN_TICKS = 40;

    public SlingItem(Item.Settings settings)
    {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean noAmmo = FindFirstItemStack(user, ItemRegistry.ROCK_BALL).isEmpty();
        if (user.getAbilities().creativeMode || !noAmmo)
        {
            if (ShouldRenderUI(world)) BetterShieldsClient.SlingTargetUi.Enabled = true;
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks)
    {
        if (world.isClient) BetterShieldsClient.SlingTargetUi.Enabled = false;
        if (!(user instanceof PlayerEntity playerEntity) || !(stack.getItem() instanceof RangedWeaponItem)) return;
        boolean isCreative = playerEntity.isCreative();
        ItemStack itemStack = isCreative ? new ItemStack(ItemRegistry.ROCK_BALL) : FindFirstItemStack(playerEntity, ItemRegistry.ROCK_BALL);
        if (itemStack.isEmpty()) return;
        float pullProgress = SlingItem.GetPullProgress(this.getMaxUseTime(stack) - remainingUseTicks, itemStack);
        if (pullProgress < 0.2) return;
        if (!world.isClient)
        {
            BetterShields.LOGGER.info("ran");
            RockBallItem rockBallItem = (RockBallItem) (itemStack.getItem() instanceof RockBallItem ? itemStack.getItem() : ItemRegistry.ROCK_BALL);
            RockBallEntity projectile = rockBallItem.Create(world, itemStack, playerEntity);

            float spin = GetSlingCycle(stack.getMaxUseTime(), user.getItemUseTimeLeft());
            var offset = GetSlingAngleOffset(spin, 1.5f);
            float pitch =  playerEntity.getPitch();
            float yaw = playerEntity.getYaw();
            projectile.setVelocity(playerEntity, offset.y + pitch, offset.x + yaw, 0.0f, pullProgress * 4f, 1.0f);
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
        if (ShouldRenderUI(world))
        {
            float spin = GetSlingCycle(stack.getMaxUseTime(), user.getItemUseTimeLeft());
            var offset = GetSlingAngleOffset(spin, 12f);
            BetterShieldsClient.SlingTargetUi.X = (int) offset.x;
            BetterShieldsClient.SlingTargetUi.Y = (int) offset.y;
        }
    }

    public static Vec2f GetSlingAngleOffset(float cycle, float radius)
    {
        float radians = (float) Math.toRadians(cycle * 360f);
        return new Vec2f((float) (Math.cos(radians) * radius), (float) (Math.sin(radians) * radius));
    }

    public static float GetSlingCycle(int maxUseTime, int itemUseTimeLeft)
    {
        int diff = maxUseTime - itemUseTimeLeft;
        float speed = (diff < MAX_SPIN_TICKS) ? BASE_SPEED2 - diff / (float)MAX_SPIN_TICKS * BASE_SPEED: BASE_SPEED;
        // Idk but I wonder if this is optimized into 1 instruction?
        return diff % speed / speed;
    }

    public static boolean ShouldRenderUI(World world)
    {
        if (!world.isClient) return false;
        var client = MinecraftClient.getInstance();
        return client.options.getPerspective().isFirstPerson() && !client.options.hudHidden;
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
    public Predicate<ItemStack> getProjectiles()
    {
        return (itemStack) -> itemStack.isOf(ItemRegistry.ROCK_BALL);
    }

    @Override
    public int getRange()
    {
        return RANGE;
    }

    private static float GetPullProgress(int useTicks, ItemStack stack) {
        float f = (float)useTicks / (float) 40f;
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
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
