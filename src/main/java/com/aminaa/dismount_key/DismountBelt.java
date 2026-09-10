package com.aminaa.dismount_key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

/**
 * Manages the Dismount Belt feature on the client.
 *
 * <p>The belt has two modes:
 * <ul>
 *     <li>{@code NORMAL}: the belt can be fastened or unfastened with its key.</li>
 *     <li>{@code TWO_KEY}: both the Belt and Dismount keys must be held to dismount.</li>
 * </ul>
 *
 * <p>This class only manages the client-side belt state and input.
 * Actual dismounting is requested through {@link DismountPacket}.</p>
 */
public class DismountBelt {

    private static boolean fastened = false;
    private static boolean wasRiding = false;

    /*
     * Previous key states are used to detect a new key press in two-key mode.
     */
    private static boolean wasBeltDown = false;
    private static boolean wasDismountDown = false;

    /*
     * Prevents a held two-key combination from sending multiple
     * dismount requests.
     */
    private static boolean comboHandled = false;

    /**
     * Updates the belt state and handles belt-related input.
     *
     * <p>This method is called once per client tick.</p>
     */
    public static void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null) {
            reset();
            return;
        }

        if (!DismountConfig.BELT_ENABLED.get()) {
            reset();
            clearBeltClicks();
            return;
        }

        boolean riding = player.isPassenger();

        if (!riding) {
            reset();
            clearBeltClicks();
            return;
        }

        /*
         * Detect the transition from not riding to riding.
         * This is where the belt's initial state is established.
         */
        if (!wasRiding) {
            fastened = false;

            if (DismountConfig.BELT_MODE.get() == DismountConfig.BeltMode.NORMAL
                    && DismountConfig.AUTO_BELT_ON_MOUNT.get()) {
                fastened = true;
                showMessage("message.dismount_key.belt_fastened");
            }

            wasRiding = true;

            wasBeltDown = false;
            wasDismountDown = false;
            comboHandled = false;
        }

        if (DismountConfig.BELT_MODE.get() == DismountConfig.BeltMode.NORMAL) {
            tickNormalMode();
        } else if (DismountConfig.BELT_MODE.get() == DismountConfig.BeltMode.TWO_KEY) {
            tickTwoKeyMode();
        }

        /*
         * Store the current key state for the next tick.
         * These values are only relevant to two-key mode.
         */
        wasBeltDown = DismountKeyMapping.DISMOUNT_BELT.isDown();
        wasDismountDown = DismountKeyMapping.DISMOUNT.isDown();
    }

    /**
     * Handles the normal belt mode.
     *
     * <p>Pressing the Belt key toggles the belt between fastened
     * and unfastened states.</p>
     */
    private static void tickNormalMode() {
        while (DismountKeyMapping.DISMOUNT_BELT.consumeClick()) {
            fastened = !fastened;

            if (fastened) {
                showMessage("message.dismount_key.belt_fastened");
            } else {
                showMessage("message.dismount_key.belt_unfastened");
            }
        }

        // Key state tracking is not needed in normal mode.
        wasBeltDown = false;
        wasDismountDown = false;
        comboHandled = false;
    }

    /**
     * Handles the two-key belt mode.
     *
     * <p>The player must hold both the Belt and Dismount keys.
     * Pressing either key alone displays the same instruction message.</p>
     */
    private static void tickTwoKeyMode() {
        boolean beltDown = DismountKeyMapping.DISMOUNT_BELT.isDown();
        boolean dismountDown = DismountKeyMapping.DISMOUNT.isDown();

        boolean beltPressed =
                beltDown && !wasBeltDown;

        boolean dismountPressed =
                dismountDown && !wasDismountDown;

        boolean combo = beltDown && dismountDown;

        // Send only one dismount request while both keys remain held.
        if (combo && !comboHandled) {
            comboHandled = true;

            // Prevent the individual key presses from being processed again.
            clearBeltClicks();
            clearDismountClicks();

            DismountPacket.send();
            return;
        }

        // Re-enable the combination after both keys have been released.
        if (!beltDown && !dismountDown) {
            comboHandled = false;
        }

        // Either key pressed by itself.
        if ((beltPressed && !dismountDown)
                || (dismountPressed && !beltDown)) {
            showTwoKeyMessage();
        }

        // Two-key mode handles these inputs itself.
        clearBeltClicks();
        clearDismountClicks();
    }

    /**
     * Clears pending Belt key clicks so they are not processed elsewhere.
     */
    private static void clearBeltClicks() {
        while (DismountKeyMapping.DISMOUNT_BELT.consumeClick()) {
            // Intentionally ignored.
        }
    }

    /**
     * Clears pending Dismount key clicks so they are not processed elsewhere.
     */
    private static void clearDismountClicks() {
        while (DismountKeyMapping.DISMOUNT.consumeClick()) {
            // Intentionally ignored.
        }
    }

    /**
     * Displays a localized belt message when belt messages are enabled.
     */
    private static void showMessage(String translationKey) {
        if (DismountConfig.SHOW_BELT_MESSAGES.get()) {
            Minecraft.getInstance().gui.setOverlayMessage(
                    Component.translatable(translationKey),
                    false
            );
        }
    }

    /**
     * Creates the instruction message using the user's current key bindings.
     *
     * <p>The key names are supplied as key components rather than hardcoded
     * text, so the message automatically reflects key remapping.</p>
     */
    public static Component getTwoKeyMessage() {
        return Component.translatable(
                "message.dismount_key.two_key_required",
                DismountKeyMapping.DISMOUNT_BELT.getTranslatedKeyMessage(),
                DismountKeyMapping.DISMOUNT.getTranslatedKeyMessage()
        );
    }

    /**
     * Displays the two-key instruction message when belt messages are enabled.
     */
    public static void showTwoKeyMessage() {
        if (DismountConfig.SHOW_BELT_MESSAGES.get()) {
            Minecraft.getInstance().gui.setOverlayMessage(
                    getTwoKeyMessage(),
                    false
            );
        }
    }

    /**
     * Displays the message shown when dismounting is blocked by a fastened belt.
     */
    public static void showLockedMessage() {
        showMessage("message.dismount_key.belt_locked");
    }

    /**
     * Resets all client-side belt state.
     */
    private static void reset() {
        fastened = false;
        wasRiding = false;
        wasBeltDown = false;
        wasDismountDown = false;
        comboHandled = false;
    }

    /**
     * Returns whether the Dismount Belt is currently fastened.
     */
    public static boolean isFastened() {
        return fastened;
    }
}