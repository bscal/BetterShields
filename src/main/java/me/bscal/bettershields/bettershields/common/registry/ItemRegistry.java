package me.bscal.bettershields.bettershields.common.registry;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.entity.JavelinEntity;
import me.bscal.bettershields.bettershields.common.items.*;
import net.devtech.arrp.json.models.*;
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
    public static final SlingItem SLING = (SlingItem) RegisterSlingItem(Id("sling"),
            new SlingItem(new FabricItemSettings().group(ItemGroup.COMBAT)));

    public static final JavelinItem FLINT_JAVELIN = (JavelinItem) RegisterItemHandheld(Id("flint_javelin"), new JavelinItem(JavelinEntity.TipMaterials.FLINT));
    public static final JavelinItem IRON_JAVELIN = (JavelinItem) RegisterItemHandheld(Id("iron_javelin"), new JavelinItem(JavelinEntity.TipMaterials.IRON));
    public static final JavelinItem DIAMOND_JAVELIN = (JavelinItem) RegisterItemHandheld(Id("diamond_javelin"), new JavelinItem(JavelinEntity.TipMaterials.DIAMOND));
    public static final JavelinItem IRON_PILUM = (JavelinItem) RegisterItemHandheld(Id("iron_pilum"), new PilumItem(JavelinEntity.TipMaterials.IRON));
    public static final JavelinItem GOLD_PILUM = (JavelinItem) RegisterItemHandheld(Id("gold_pilum"), new PilumItem(JavelinEntity.TipMaterials.GOLD));
    public static final JavelinItem NETHERITE_PILUM = (JavelinItem) RegisterItemHandheld(Id("netherite_pilum"), new PilumItem(JavelinEntity.TipMaterials.NETHERITE));

    public static final Item ROCK_BALL = RegisterItemHandheld(Id("rock_ball"), new RockBallItem(SoundEvents.BLOCK_STONE_HIT));

    public static void Init()
    {
        RegisterModelPredicateProviders();
    }

    public static Identifier Id(String name)
    {
        return new Identifier(BetterShields.MOD_ID, name);
    }

    public static Item RegisterItemHandheld(Identifier id, Item item)
    {
        Registry.register(Registry.ITEM, id, item);
        var modelId = new Identifier(id.getNamespace(), "item/" + id.getPath());
        ItemGeneration.RESOURCE_PACK.addModel(JModel.model("minecraft:item/handheld").textures(JModel.textures().layer0(modelId.toString())), modelId);
        return item;
    }

    public static Item RegisterSlingItem(Identifier id, Item item)
    {
        Registry.register(Registry.ITEM, id, item);
        var modelId = new Identifier(id.getNamespace(), "item/" + id.getPath());
        var model = JModel.model("item/generated");
        model.textures(JModel.textures().layer0(modelId.toString()));

        model.addOverride(SlingOverride(modelId, 0, .0f));
        model.addOverride(SlingOverride(modelId, 1, .25f));
        model.addOverride(SlingOverride(modelId, 2, .5f));
        model.addOverride(SlingOverride(modelId, 3, .75f));

        //var launchId = new Identifier(id.getNamespace(), id.getPath() + "_launch");
        //model.addOverride(JModel.override(JModel.condition().parameter("launching", 1), launchId));

        ItemGeneration.RESOURCE_PACK.addModel(model, modelId);

        return item;
    }

    private static JOverride SlingOverride(Identifier mainId, int index, float swingPercentage)
    {
        var id = new Identifier(mainId.getNamespace(), mainId.getPath() + "_" + index);
        ItemGeneration.RESOURCE_PACK.addModel(JModel.model("minecraft:item/handheld").textures(JModel.textures().layer0(id.toString())), id);
        var condition = JModel.condition().parameter("swinging", 1).parameter("swing", swingPercentage);
        return JModel.override(condition, id);
    }

    public static JModel GenerateSpearJson(Identifier id, JPosition fplh, JPosition fprh, JPosition tplh, JPosition tprh, JPosition gui, JPosition fixed, JPosition ground)
    {
        var model = JModel.model("builtin/entity");

        var display = new JDisplay();
        display.setFirstperson_lefthand(fplh);
        display.setFirstperson_righthand(fprh);
        display.setThirdperson_lefthand(tplh);
        display.setThirdperson_righthand(tprh);
        display.setGui(gui);
        display.setFixed(fixed);
        display.setFixed(ground);
        model.display(display);

        var condition = JModel.condition().parameter("throwing", 1);
        model.addOverride(JModel.override(condition, id));

        return model;
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
