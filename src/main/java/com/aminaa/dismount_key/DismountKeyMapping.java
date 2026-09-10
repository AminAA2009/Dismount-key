package com.aminaa.dismount_key;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * Defines and registers the key mappings used by Dismount Key.
 *
 * <p>The main Dismount key is handled every client tick, while
 * Dismount Belt handles its own state through {@link DismountBelt}.</p>
 */
@Mod.EventBusSubscriber(
        modid = Dismount_Key.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class DismountKeyMapping {

    public static final KeyMapping DISMOUNT = new KeyMapping(
            "key.dismount_key.dismount",
            GLFW.GLFW_KEY_LEFT_SHIFT,
            "key.categories.gameplay"
    );

    public static final KeyMapping DISMOUNT_BELT = new KeyMapping(
            "key.dismount_key.dismount_belt",
            GLFW.GLFW_KEY_B,
            "key.categories.gameplay"
    );

    /**
     * Registers the mod's key mappings with Minecraft.
     */
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(DISMOUNT);
        event.register(DISMOUNT_BELT);
    }

    /**
     * Handles client-side input and Dismount requests.
     */
    @Mod.EventBusSubscriber(
            modid = Dismount_Key.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE,
            value = Dist.CLIENT
    )
    public static class ClientEvents {

        /**
         * Updates Dismount Belt state and processes Dismount input.
         *
         * <p>When the belt is disabled, the Dismount key always works normally.
         * When the belt is enabled, normal mode checks whether the belt is fastened.
         * Two-key mode is handled entirely by {@link DismountBelt}.</p>
         */
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {

                DismountBelt.tick();

                /*
                 * If the belt is disabled, Dismount always works normally.
                 *
                 * If the belt is enabled and the mode is NORMAL,
                 * the fastened state is checked before dismounting.
                 *
                 * In TWO_KEY mode, DismountBelt handles the input itself.
                 */
                if (!DismountConfig.BELT_ENABLED.get()
                        || DismountConfig.BELT_MODE.get() == DismountConfig.BeltMode.NORMAL) {

                    while (DISMOUNT.consumeClick()) {

                        if (DismountConfig.BELT_ENABLED.get()
                                && DismountBelt.isFastened()) {

                            DismountBelt.showLockedMessage();
                            continue;
                        }

                        DismountPacket.send();
                    }
                }
            }
        }
    }
}