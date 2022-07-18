package me.bscal.bettershields.bettershields.common.util;

public class TipMaterial
{

    public byte MaterialId;
    public byte Piercing;
    public float DamageBonus;
    public float AccuracyBonus;
    public float VelocityBonus;

    public TipMaterial(byte materialId, byte piercing, float dmgBonus, float accBonus, float velBonus)
    {
        MaterialId = materialId;
        Piercing = piercing;
        DamageBonus = dmgBonus;
        AccuracyBonus = accBonus;
        VelocityBonus = velBonus;
    }

}
