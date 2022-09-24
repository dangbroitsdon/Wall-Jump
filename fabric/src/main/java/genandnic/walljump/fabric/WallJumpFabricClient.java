package genandnic.walljump.fabric;

import genandnic.walljump.WallJump;
import net.fabricmc.api.ClientModInitializer;

public class WallJumpFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WallJump.initClient();
    }
}
