package genandnic.walljump.quilt;

import genandnic.walljump.WallJump;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class WallJumpQuiltClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        WallJump.initClient();
    }
}
