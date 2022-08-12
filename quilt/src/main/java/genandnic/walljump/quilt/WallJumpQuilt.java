package genandnic.walljump.quilt;

import genandnic.walljump.WallJump;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class WallJumpQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        WallJump.init();
    }
}
