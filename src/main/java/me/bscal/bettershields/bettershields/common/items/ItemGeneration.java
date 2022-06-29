package me.bscal.bettershields.bettershields.common.items;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.registry.ItemRegistry;
import me.bscal.bettershields.bettershields.common.registry.SpearDisplay;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JDisplay;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JOverride;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemGeneration
{

    public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(BetterShields.MOD_ID);

    public static void Init()
    {
        RRPCallback.AFTER_VANILLA.register(resourcePacks ->
        {
            GenerateResourcePack();
            resourcePacks.add(RESOURCE_PACK);
        });
    }

    public static void GenerateResourcePack()
    {
        GenerateSling(GetId(ItemRegistry.SLING));

        GenerateSpear(GetId(ItemRegistry.IRON_PILUM));
        GenerateSpear(GetId(ItemRegistry.GOLD_PILUM));
        GenerateSpear(GetId(ItemRegistry.NETHERITE_PILUM));
        GenerateSpear(GetId(ItemRegistry.FLINT_JAVELIN));
        GenerateSpear(GetId(ItemRegistry.IRON_JAVELIN));
        GenerateSpear(GetId(ItemRegistry.DIAMOND_JAVELIN));
    }

    private static Identifier GetId(Item item)
    {
        return Registry.ITEM.getId(item);
    }

    public static Identifier ToItemPathId(Identifier id)
    {
        return new Identifier(id.getNamespace(), "item/" + id.getPath());
    }

    public static JModel GenerateModelHandheld(String modelIdTextureStr, JDisplay display)
    {
        var model = JModel.model("minecraft:item/handheld").textures(JModel.textures().layer0(modelIdTextureStr));
        if (display != null) model.display(display);
        return model;
    }

    public static void GenerateSpear(Identifier id)
    {
        var modelId = ToItemPathId(id);
        var modelIdTextureStr = modelId.toString();
        var model = GenerateModelHandheld(modelIdTextureStr, SpearDisplay.GetDefaultInHand());

        var condition = JModel.condition().parameter("throwing", 1f);
        var modelThrowingId = new Identifier(id.getNamespace(), modelId.getPath() + "_throwing");
        model.addOverride(JModel.override(condition, modelThrowingId));

        var throwingModel = GenerateModelHandheld(modelIdTextureStr, SpearDisplay.GetDefaultThrowing());

        ItemGeneration.RESOURCE_PACK.addModel(model, modelId);
        ItemGeneration.RESOURCE_PACK.addModel(throwingModel, modelThrowingId);
    }

    public static void GenerateSling(Identifier id)
    {
        var modelId = ToItemPathId(id);
        var model = JModel.model("item/generated");
        model.textures(JModel.textures().layer0(modelId.toString()));
        model.addOverride(SlingOverride(modelId, 0, .0f));
        model.addOverride(SlingOverride(modelId, 1, .25f));
        model.addOverride(SlingOverride(modelId, 2, .5f));
        model.addOverride(SlingOverride(modelId, 3, .75f));
        ItemGeneration.RESOURCE_PACK.addModel(model, modelId);
    }

    private static JOverride SlingOverride(Identifier mainId, int index, float swingPercentage)
    {
        var id = new Identifier(mainId.getNamespace(), mainId.getPath() + "_" + index);
        ItemGeneration.RESOURCE_PACK.addModel(JModel.model("minecraft:item/handheld").textures(JModel.textures().layer0(id.toString())), id);
        var condition = JModel.condition().parameter("swinging", 1).parameter("swing", swingPercentage);
        return JModel.override(condition, id);
    }

}
