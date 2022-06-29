package me.bscal.bettershields.bettershields.common.registry;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.items.SlingItem;
import net.devtech.arrp.json.models.JDisplay;
import net.devtech.arrp.json.models.JPosition;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SpearDisplay
{

    private static final Identifier THROWING = new Identifier("throwing");

    public JPosition FirstPersonLeftHand;
    public JPosition FirstPersonRightHand;
    public JPosition ThirdPersonLeftHand;
    public JPosition ThirdPersonRightHand;
    public JPosition Gui;
    public JPosition Fixed;
    public JPosition Ground;
    public JPosition Head;

    public JDisplay CreateDisplay()
    {
        var display = new JDisplay();
        display.setFirstperson_lefthand(FirstPersonLeftHand);
        display.setFirstperson_righthand(FirstPersonRightHand);
        display.setThirdperson_lefthand(ThirdPersonLeftHand);
        display.setThirdperson_righthand(ThirdPersonRightHand);
        display.setGui(Gui);
        display.setFixed(Fixed);
        display.setGround(Ground);
        display.setHead(Head);
        return display;
    }

    public static JDisplay GetDefaultInHand()
    {
        SpearDisplay display = new SpearDisplay();
        return display.CreateDisplay();
    }

    public static JDisplay GetDefaultThrowing()
    {
        SpearDisplay display = new SpearDisplay();
        display.FirstPersonLeftHand     = new JPosition().rotation(-5f, 0, 45f).translation(.75f,0,0).scale(1f, 1f, 1f);
        display.FirstPersonRightHand    = new JPosition().rotation(-5f, 0, 45f).translation(.75f,0,0).scale(1f, 1f, 1f);
        display.ThirdPersonLeftHand     = new JPosition().rotation(180f, 0, 45f).translation(0,0,0).scale(1f, 1f, 1f);
        display.ThirdPersonRightHand    = new JPosition().rotation(180f, 0, 45f).translation(0,0,0).scale(1f, 1f, 1f);
        return display.CreateDisplay();
    }

    public static void RegisterSpearModelPredicate(Item item)
    {
        ModelPredicateProviderRegistry.register(item, THROWING, SpearDisplay::ModelPredicateProvider);
    }

    private static float ModelPredicateProvider(ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int seed)
    {
        return (livingEntity != null && livingEntity.getActiveItem() == itemStack) ? 1.0f : 0.0f;
    }

}
