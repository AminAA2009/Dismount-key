package com.aminaa.dismount_key.mixin;

import com.aminaa.dismount_key.DismountBelt;
import com.aminaa.dismount_key.DismountConfig;
import com.aminaa.dismount_key.DismountKeyMapping;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Customizes the vanilla dismount message shown when mounting a vehicle.
 *
 * <p>Vanilla displays a message containing the configured Sneak key.
 * Dismount Key replaces that key with its own Dismount key and, when
 * using two-key belt mode, replaces the entire message with instructions
 * for holding both required keys.</p>
 */
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    /**
     * Replaces the vanilla mount message with the appropriate Dismount Key
     * message when configured to do so.
     *
     * <p>In normal mode, only the key shown in the vanilla message is
     * replaced. In two-key mode, the complete message is replaced with
     * the dynamically generated Belt + Dismount instruction.</p>
     */
    @Redirect(
            method = "handleSetEntityPassengersPacket",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"
            )
    )
    private MutableComponent dismount_key$replaceMountMessage(
            String key,
            Object[] args
    ) {
        if ("mount.onboard".equals(key)
                && DismountConfig.SHOW_DISMOUNT_MESSAGE.get()) {

            if (DismountConfig.BELT_ENABLED.get()
                    && DismountConfig.BELT_MODE.get() == DismountConfig.BeltMode.TWO_KEY) {

                return DismountBelt.getTwoKeyMessage().copy();
            }

            if (args.length > 0) {
                args[0] =
                        DismountKeyMapping.DISMOUNT.getTranslatedKeyMessage();
            }
        }

        return Component.translatable(key, args);
    }

    /**
     * Controls whether the vanilla dismount overlay message is displayed.
     *
     * <p>This setting only controls the on-screen mount message. The actual
     * dismount action is handled separately by the Dismount Key input logic.</p>
     */
    @Redirect(
            method = "handleSetEntityPassengersPacket",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;setOverlayMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    private void dismount_key$controlDismountMessage(
            Gui gui,
            Component message,
            boolean animateColor
    ) {
        if (DismountConfig.SHOW_DISMOUNT_MESSAGE.get()) {
            gui.setOverlayMessage(message, animateColor);
        }
    }
}