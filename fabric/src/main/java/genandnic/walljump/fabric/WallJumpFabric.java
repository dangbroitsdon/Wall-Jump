package genandnic.walljump.fabric;

import genandnic.walljump.WallJump;
import net.fabricmc.api.ModInitializer;

public class WallJumpFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WallJump.init();
    }
}
