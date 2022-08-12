package genandnic.walljump.mixin.client;

import genandnic.walljump.logic.DoubleJumpLogic;
import genandnic.walljump.logic.MiscLogic;
import genandnic.walljump.logic.SpeedBoostLogic;
import genandnic.walljump.logic.WallJumpLogic;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {
    @Inject(method = "tick", at = @At("TAIL"))
    private void wju$tickLogic(CallbackInfo ci) {
        WallJumpLogic.doWallJump();
        SpeedBoostLogic.doSpeedBoost();
        DoubleJumpLogic.doDoubleJump();
        MiscLogic.doStepAssist();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickFallSound(CallbackInfo ci) {
        MiscLogic.playFallingSound();
    }
}
