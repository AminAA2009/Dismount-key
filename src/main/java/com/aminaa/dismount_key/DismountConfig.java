package com.aminaa.dismount_key;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Defines the client-side configuration for Dismount Key.
 *
 * <p>The configuration is divided into two sections:
 * {@code dismount} contains the main dismount settings, while
 * {@code belt} contains the optional Dismount Belt settings.</p>
 */
public final class DismountConfig {

    public enum BeltMode {
        NORMAL,
        TWO_KEY
    }

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ForgeConfigSpec.BooleanValue SHOW_DISMOUNT_MESSAGE;
    public static final ForgeConfigSpec.BooleanValue BELT_ENABLED;
    public static final ForgeConfigSpec.EnumValue<BeltMode> BELT_MODE;
    public static final ForgeConfigSpec.BooleanValue AUTO_BELT_ON_MOUNT;
    public static final ForgeConfigSpec.BooleanValue SHOW_BELT_MESSAGES;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        // Main dismount settings.
        builder.push("dismount");

        SHOW_DISMOUNT_MESSAGE = builder
                .comment("Whether to show the 'Press <key> to Dismount' message when mounting a vehicle.")
                .define("showDismountMessage", true);

        builder.pop();

        // Optional Dismount Belt settings.
        builder.push("belt");

        BELT_ENABLED = builder
                .comment("Enables the Dismount Belt feature and its keybind.")
                .define("enabled", true);

        BELT_MODE = builder
                .comment(
                        "Determines how the Dismount Belt works.",
                        "NORMAL = press Dismount Belt to fasten/unfasten the belt.",
                        "TWO_KEY = hold Dismount Belt and Dismount together to dismount."
                )
                .defineEnum("mode", BeltMode.NORMAL);

        AUTO_BELT_ON_MOUNT = builder
                .comment("Automatically fastens the Dismount Belt when mounting a vehicle. Only applies to normal mode.")
                .define("autoBeltOnMount", true);

        SHOW_BELT_MESSAGES = builder
                .comment("Whether to show Dismount Belt status and instruction messages.")
                .define("showBeltMessages", true);

        builder.pop();

        CLIENT_SPEC = builder.build();
    }

    private DismountConfig() {
        // Utility class; not meant to be instantiated.
    }
}