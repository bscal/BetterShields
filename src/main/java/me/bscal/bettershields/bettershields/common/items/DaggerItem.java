package me.bscal.bettershields.bettershields.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import me.bscal.bettershields.bettershields.common.api.OffhandWeapon;
import me.bscal.bettershields.bettershields.common.mixin_accessors.PlayerEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DaggerItem extends ToolItem implements Vanishable, OffhandWeapon
{
    private final float AttackDamage;
    private final Multimap<EntityAttribute, EntityAttributeModifier> AttributeModifiers;

    public DaggerItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item.Settings settings)
    {
        super(toolMaterial, settings);
        AttackDamage = (float)attackDamage + toolMaterial.getAttackDamage();
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID,
                "Weapon modifier", AttackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID,
                "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
        AttributeModifiers = builder.build();
    }

    @Override
    public boolean IsOffhandWeapon()
    {
        return true;
    }

    @Override
    public float GetOffhandDamageReduction()
    {
        return 1f;
    }

    @Override
    public void AttackOffhand(World world, PlayerEntity user, Hand hand, LivingEntity target)
    {
        PlayerEntityAccessor pAccessor = (PlayerEntityAccessor)user;
        pAccessor.AttackOffhand(target, user.getStackInHand(Hand.OFF_HAND), this);
    }
}
