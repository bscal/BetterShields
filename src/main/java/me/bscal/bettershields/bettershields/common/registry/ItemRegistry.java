package me.bscal.bettershields.bettershields.common.registry;

import me.bscal.bettershields.bettershields.BetterShields;
import me.bscal.bettershields.bettershields.common.items.DaggerItem;
import me.bscal.bettershields.bettershields.common.items.ItemGeneration;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry
{

    public static Item WOODEN_DAGGER = RegisterItem(Id("wooden_dagger"),
            new DaggerItem(ToolMaterials.WOOD, 1, 2, new FabricItemSettings().group(ItemGroup.COMBAT)));

    public static void Init()
    {
        //WOODEN_DAGGER
    }

    public static Identifier Id(String name)
    {
        return new Identifier(BetterShields.MOD_ID, name);
    }

    public static Item RegisterItem(Identifier id, Item item)
    {
        Registry.register(Registry.ITEM, id, item);
        var modelId = new Identifier(id.getNamespace(),  "item/" + id.getPath());
        ItemGeneration.RESOURCE_PACK.addModel(JModel.model("item/generated").textures(JModel.textures().layer0(modelId.toString())), modelId);
        return item;
    }

}
