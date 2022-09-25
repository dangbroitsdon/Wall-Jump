package genandnic.walljump.mixin.client;

import genandnic.walljump.registry.WallJumpServerReceivers;
import genandnic.walljump.config.WallJumpConfig;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {
    @Inject(method = "onDisconnected", at = @At("TAIL"))
    private void wju$resetConfigBackToClient(CallbackInfo ci) {
        WallJumpServerReceivers.serverConfigSynced = false;
        WallJumpConfig.config.load();
    }
}
