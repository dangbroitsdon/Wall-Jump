package genandnic.walljump.mixin.client;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static genandnic.walljump.client.SpeedBoostLogic.doSpeedBoost;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntitySpeedBoostMixin{
    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void miscellaneousTickMovement(CallbackInfo ci) {doSpeedBoost();}
}
