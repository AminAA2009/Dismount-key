package com.aminaa.dismount_key.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {

    @Shadow private float eyeHeight;
    @Shadow private float eyeHeightOld;

    private int dismountkey$debugTicks = 0;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void dismountkey$keepEyeHeightWhileRiding(CallbackInfo ci) {
        if (!(((Camera) (Object) this).getEntity() instanceof Player player) || !player.isPassenger()) {
            return;
        }

        float standing = player.getEyeHeight(Pose.STANDING);
        float target = player.getEyeHeight();

        if (dismountkey$debugTicks++ % 20 == 0) {
            System.out.println("[DK-DEBUG] pose=" + player.getPose()
                    + " isCrouching=" + player.isCrouching()
                    + " shift=" + player.isShiftKeyDown()
                    + " camEye=" + this.eyeHeight
                    + " entityEye=" + target
                    + " standing=" + standing);
        }

        if (target < standing) {
            this.eyeHeightOld = this.eyeHeight;
            if (this.eyeHeight < standing) {
                this.eyeHeight += (standing - this.eyeHeight) * 0.5F;
            }
            ci.cancel();
        }
    }
}