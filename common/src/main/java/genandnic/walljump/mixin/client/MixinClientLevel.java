package genandnic.walljump.mixin.client;

import genandnic.walljump.logic.MiscLogic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
    @Inject(method = "addPlayer", at = @At(value = "TAIL"))
    private void wju$addPlayerFallingSound(int id, AbstractClientPlayer player, CallbackInfo ci) {
        if(player == Minecraft.getInstance().player) {
            MiscLogic.addFallingSound();
        }
    }
}
