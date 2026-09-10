package com.aminaa.dismount_key;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Main entry point for the Dismount Key mod.
 *
 * <p>Registers the client configuration and initializes
 * the network channel used for dismount requests.</p>
 */
@Mod(Dismount_Key.MODID)
public class Dismount_Key {

    public static final String MODID = "dismount_key";

    public Dismount_Key() {
        // Register client-side configuration.
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.CLIENT,
                DismountConfig.CLIENT_SPEC
        );

        // Initialize the client-server dismount network channel.
        DismountPacket.register();
    }
}