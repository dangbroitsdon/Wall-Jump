package genandnic.walljump.mixin.client;

import genandnic.walljump.client.MiscLogic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class MixinClientWorld {

    @Inject(method = "addPlayer", at = @At(value = "TAIL"))
    private void addPlayerFallingSound(int id, AbstractClientPlayerEntity player, CallbackInfo ci) {
        if(player == MinecraftClient.getInstance().player) {
            MiscLogic.fallSound();
        }
    }
}
