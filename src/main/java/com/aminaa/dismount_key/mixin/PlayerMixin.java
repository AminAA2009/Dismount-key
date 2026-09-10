package com.aminaa.dismount_key.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Prevents vanilla sneak input from automatically dismounting the player.
 *
 * <p>Dismount Key handles dismounting through its own key mapping instead,
 * allowing the player to use Sneak independently while riding.</p>
 */
@Mixin(Player.class)
public class PlayerMixin {

    /**
     * Disables the vanilla sneak-to-dismount check during {@code rideTick}.
     *
     * <p>The actual dismount action is handled separately by
     * {@link com.aminaa.dismount_key.DismountPacket}.</p>
     */
    @Redirect(
            method = "rideTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;wantsToStopRiding()Z"
            )
    )
    private boolean dismount_key$preventVanillaDismount(Player player) {
        return false;
    }
}
