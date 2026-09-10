package com.aminaa.dismount_key;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

/**
 * Network packet used to request a dismount from the server.
 *
 * <p>The dismount action is performed server-side so that it is properly
 * synchronized and works in multiplayer environments.</p>
 */
public class DismountPacket {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Dismount_Key.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    /**
     * Registers the packet with the network channel.
     */
    public static void register() {
        CHANNEL.registerMessage(
                packetId++,
                DismountPacket.class,
                DismountPacket::encode,
                DismountPacket::decode,
                DismountPacket::handle
        );
    }

    public DismountPacket() {
    }

    /**
     * Encodes the packet data.
     *
     * <p>This packet currently carries no additional data because the
     * sender is already identified by the server network context.</p>
     */
    private static void encode(
            DismountPacket packet,
            FriendlyByteBuf buffer
    ) {
    }

    /**
     * Decodes a dismount request received from the client.
     */
    private static DismountPacket decode(FriendlyByteBuf buffer) {
        return new DismountPacket();
    }

    /**
     * Handles a dismount request on the server thread.
     *
     * <p>The server obtains the player from the network context and stops
     * the player's current riding relationship.</p>
     */
    private static void handle(
            DismountPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null) {
                player.stopRiding();
            }
        });

        context.setPacketHandled(true);
    }

    /**
     * Sends a dismount request from the client to the server.
     */
    public static void send() {
        CHANNEL.sendToServer(new DismountPacket());
    }
}