package me.bscal.bettershields.bettershields.common.combat;

import me.bscal.bettershields.bettershields.common.mixin.entity.DamageSourceInvoker;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;

public class DamageSystem
{


    public static DamageSource GetPiercingSrc(LivingEntity attacker)
    {
        var damageSource = new EntityDamageSource("piercing", attacker);
        ((DamageSourceInvoker) damageSource).invokeSetBypassesArmor();
        return damageSource;
    }

    public static DamageSource GetBluntSrc(LivingEntity attacker)
    {
        var damageSource = new EntityDamageSource("blunt", attacker);
        ((DamageSourceInvoker) damageSource).invokeSetBypassesArmor();
        return damageSource;
    }

    public static void DisableBlocking(LivingEntity entity, int durationTicks)
    {
        if (!entity.world.isClient && entity instanceof PlayerEntity playerEntity && entity.isBlocking())
        {
            var shieldItem = playerEntity.getOffHandStack().getItem();
            playerEntity.getItemCooldownManager().set(shieldItem, durationTicks);
            playerEntity.clearActiveItem();
            playerEntity.world.sendEntityStatus(entity, EntityStatuses.BREAK_SHIELD);
        }
    }

    public static boolean ProcessDamage(LivingEntity attacker, LivingEntity entity, float damage, float piercing, float blunt)
    {
        if (!entity.world.isClient)
        {
            assert piercing + blunt <= 1.0f: "Piercing and Blunt damage cannot be greater then 1.0";

            if (piercing > 0)
            {
                var piercingDmg = damage * piercing;
                damage -= piercingDmg;
                entity.damage(GetPiercingSrc(attacker), piercingDmg);
            }
            if (blunt > 0)
            {
                var bluntDmg = damage * blunt;
                damage -= bluntDmg;
                entity.damage(GetBluntSrc(attacker), bluntDmg);
            }

            var src = (attacker instanceof PlayerEntity playerEntity) ? DamageSource.player(playerEntity) : DamageSource.mob(entity);
            return entity.damage(src, damage);
        }
        return false;
    }

}
