package me.bscal.bettershields.bettershields.common.items;

import me.bscal.bettershields.bettershields.BetterShields;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.minecraft.util.Identifier;

import static net.devtech.arrp.api.RuntimeResourcePack.id;
import static net.devtech.arrp.json.models.JModel.*;
public class ItemGeneration
{

    public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(BetterShields.MOD_ID);

    public static void Init()
    {
        RRPCallback.AFTER_VANILLA.register(a -> a.add(RESOURCE_PACK));
    }

}
