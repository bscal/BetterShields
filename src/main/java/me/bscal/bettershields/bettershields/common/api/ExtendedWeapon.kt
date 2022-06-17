package me.bscal.bettershields.bettershields.common.api

interface ExtendedWeapon
{

    fun ProcessAltAttack();

    fun ProcessSpecialAttack();

    fun HasAltAttack() : Boolean;

    fun HasSpecialAttack() : Boolean;

}