package me.bscal.bettershields.bettershields.common.networking;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class ProjectilePacket
{

    private static final PacketByteBuf CREATE_BUFFER = new PacketByteBuf(Unpooled.buffer());

    public static Packet<?> Serialize(Entity e, Identifier packetId)
    {
        if (e.world.isClient) throw new AssertionError("Cannot create server packet on client!");

        CREATE_BUFFER.clear();
        CREATE_BUFFER.writeUuid(e.getUuid());
        CREATE_BUFFER.writeVarInt(Registry.ENTITY_TYPE.getRawId(e.getType()));
        CREATE_BUFFER.writeVarInt(e.getId());
        var pos = e.getPos();
        CREATE_BUFFER.writeFloat((float) pos.x);
        CREATE_BUFFER.writeFloat((float) pos.y);
        CREATE_BUFFER.writeFloat((float) pos.z);
        CREATE_BUFFER.writeByte(PackAngle(e.getPitch()));
        CREATE_BUFFER.writeByte(PackAngle(e.getYaw()));
        return ServerPlayNetworking.createS2CPacket(packetId, CREATE_BUFFER);
    }

    public static byte PackAngle(float angle)
    {
        return (byte) Math.floor(angle * 256 / 360);
    }

    public static float UnpackAngle(byte angleByte)
    {
        return (angleByte * 360) / 256f;
    }

    public static void Deserialize(MinecraftClient client, PacketByteBuf buf)
    {
        final UUID uuid = buf.readUuid();
        final EntityType<?> et = Registry.ENTITY_TYPE.get(buf.readVarInt());
        final int entityId = buf.readVarInt();
        final double x = buf.readFloat();
        final double y = buf.readFloat();
        final double z = buf.readFloat();
        final float pitch = UnpackAngle(buf.readByte());
        final float yaw = UnpackAngle(buf.readByte());
        client.execute(() -> {
            if (client.world == null) throw new IllegalStateException("Tried to spawn entity in a null world!");
            Entity e = et.create(client.world);
            if (e == null) throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
            e.updateTrackedPosition(x, y, z);
            e.setPos(x, y, z);
            e.setPitch(pitch);
            e.setYaw(yaw);
            e.setId(entityId);
            e.setUuid(uuid);
            client.world.addEntity(entityId, e);
        });
    }
}
