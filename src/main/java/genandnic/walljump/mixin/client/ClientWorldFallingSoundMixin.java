package genandnic.walljump.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static genandnic.walljump.client.MiscLogic.fallSound;


@Mixin(ClientWorld.class)
public class ClientWorldFallingSoundMixin {

    @Inject(method = "addPlayer", at = @At(value = "TAIL"))
    private void addPlayerFallingSound(int id, AbstractClientPlayerEntity player, CallbackInfo ci) {
        if(player == MinecraftClient.getInstance().player) {
            fallSound();
        }
    }
}
