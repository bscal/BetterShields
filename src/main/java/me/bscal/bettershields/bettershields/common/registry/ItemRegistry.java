package me.bscal.bettershields.bettershields.common.registry;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.entity.JavelinEntity;
import me.bscal.bettershields.bettershields.common.items.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry
{

    public static final Item WOODEN_DAGGER = RegisterItemHandheld(Id("wooden_dagger"),
            new DaggerItem(ToolMaterials.WOOD, 1, 2, new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final SlingItem SLING = (SlingItem) RegisterItem(Id("sling"),
            new SlingItem(new FabricItemSettings().group(ItemGroup.COMBAT)));

    public static final JavelinItem FLINT_JAVELIN = (JavelinItem) RegisterSpearItem(Id("flint_javelin"), new JavelinItem(JavelinEntity.TipMaterials.FLINT));
    public static final JavelinItem IRON_JAVELIN = (JavelinItem) RegisterSpearItem(Id("iron_javelin"), new JavelinItem(JavelinEntity.TipMaterials.IRON));
    public static final JavelinItem DIAMOND_JAVELIN = (JavelinItem) RegisterSpearItem(Id("diamond_javelin"), new JavelinItem(JavelinEntity.TipMaterials.DIAMOND));
    public static final JavelinItem IRON_PILUM = (JavelinItem) RegisterSpearItem(Id("iron_pilum"), new PilumItem(JavelinEntity.TipMaterials.IRON));
    public static final JavelinItem GOLD_PILUM = (JavelinItem) RegisterSpearItem(Id("gold_pilum"), new PilumItem(JavelinEntity.TipMaterials.GOLD));
    public static final JavelinItem NETHERITE_PILUM = (JavelinItem) RegisterSpearItem(Id("netherite_pilum"), new PilumItem(JavelinEntity.TipMaterials.NETHERITE));
    public static final Item ROCK_BALL = RegisterItemHandheld(Id("rock_ball"), new RockBallItem(SoundEvents.BLOCK_STONE_HIT));


    public static Identifier Id(String path)
    {
        return new Identifier(BetterShields.MOD_ID, path);
    }

    public static Item RegisterItem(Identifier id, Item item)
    {
        return Registry.register(Registry.ITEM, id, item);
    }

    public static Item RegisterSpearItem(Identifier id, Item item)
    {
        SpearDisplay.RegisterSpearModelPredicate(item);
        return RegisterItem(id, item);
    }

    public static Item RegisterItemHandheld(Identifier id, Item item)
    {
        var modelId = ItemGeneration.ToItemPathId(id);
        ItemGeneration.RESOURCE_PACK.addModel(ItemGeneration.GenerateModelHandheld(modelId.toString(), null), modelId);
        return RegisterItem(id, item);
    }

    public static void RegisterModelPredicateProviders()
    {
        ModelPredicateProviderRegistry.register(SLING, new Identifier("swing"), (itemStack, clientWorld, livingEntity, seed) ->
        {
            if (livingEntity == null)
            {
                return 0.0F;
            }
            return livingEntity.getActiveItem() != itemStack ? 0.0F : SlingItem.GetSlingCycle(itemStack.getMaxUseTime(), livingEntity.getItemUseTimeLeft());
        });

        ModelPredicateProviderRegistry.register(SLING, new Identifier("swinging"), (itemStack, clientWorld, livingEntity, seed) ->
        {
            if (livingEntity == null)
            {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
        });
    }

}
