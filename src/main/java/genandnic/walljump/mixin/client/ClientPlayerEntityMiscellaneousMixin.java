package genandnic.walljump.mixin.client;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static genandnic.walljump.client.MiscLogic.fallSound;
import static genandnic.walljump.client.MiscLogic.stepAssist;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMiscellaneousMixin{
    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void miscellaneousTickMovement(CallbackInfo ci) {
        stepAssist();
        fallSound();
    }
}
