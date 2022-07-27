package genandnic.walljump.mixin.client;

import genandnic.walljump.client.DoubleJumpLogic;
import genandnic.walljump.client.MiscLogic;
import genandnic.walljump.client.SpeedBoostLogic;
import genandnic.walljump.client.WallJumpLogic;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity {
    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void wallJumpTickMovement(CallbackInfo ci) { WallJumpLogic.doWallJump(); }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void speedBoostTickMovement(CallbackInfo ci) { SpeedBoostLogic.doSpeedBoost(); }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void doubleJumpTickMovement(CallbackInfo ci) { DoubleJumpLogic.doDoubleJump(); }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void miscellaneousTickMovement(CallbackInfo ci) {
        MiscLogic.stepAssist();
        MiscLogic.playFallSound();
    }
}
