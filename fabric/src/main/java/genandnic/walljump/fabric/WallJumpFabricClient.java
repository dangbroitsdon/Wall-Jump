package genandnic.walljump.fabric;

import genandnic.walljump.WallJumpClient;
import net.fabricmc.api.ClientModInitializer;

public class WallJumpFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WallJumpClient.init();
    }
}
