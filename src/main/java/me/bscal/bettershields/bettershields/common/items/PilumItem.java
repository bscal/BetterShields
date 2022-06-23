package me.bscal.bettershields.bettershields.common.items;

import me.bscal.bettershields.bettershields.common.entity.JavelinEntity;
import me.bscal.bettershields.bettershields.common.entity.PilumEntity;
import me.bscal.bettershields.bettershields.common.registry.ItemRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class PilumItem extends JavelinItem
{

    public PilumItem(byte tipMaterial)
    {
        super(tipMaterial, new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(2));
    }

    public static ItemStack GetStackForTip(byte tipMaterial)
    {
        if (tipMaterial == JavelinEntity.TipMaterials.GOLD)
            return new ItemStack(ItemRegistry.GOLD_PILUM);
        if (tipMaterial == JavelinEntity.TipMaterials.NETHERITE)
            return new ItemStack(ItemRegistry.NETHERITE_PILUM);
        return new ItemStack(ItemRegistry.IRON_PILUM);
    }

    private PilumEntity CreatePilum(World world, ItemStack stack, LivingEntity shooter)
    {
        PilumEntity pilum = new PilumEntity(world, shooter);
        pilum.SetTipMaterial(TipMaterial);
        return pilum;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks)
    {
        if (!(user instanceof PlayerEntity playerEntity) || !(stack.getItem() instanceof PilumItem) || stack != playerEntity.getMainHandStack())
            return;
        float pullProgress = JavelinItem.GetPullProgress(this.getMaxUseTime(stack) - remainingUseTicks, stack);
        if (pullProgress < 0.1) return;
        if (!world.isClient)
        {
            PilumEntity pilum = CreatePilum(world, stack, playerEntity);
            float pitch = playerEntity.getPitch() + 0.5f;
            float yaw = playerEntity.getYaw();
            pilum.setVelocity(playerEntity, pitch, yaw, 0.0f, pullProgress * 1.5f, 2.0f);
            world.spawnEntity(pilum);
        }
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f);
        if (!playerEntity.isCreative())
            stack.decrement(1);
    }

}
