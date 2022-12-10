package genandnic.walljump.registry;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import genandnic.walljump.config.WallJumpConfig;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class WallJumpKeyMappings {
    public static boolean toggleWallJump;
    public static KeyMapping KEY_WALLJUMP = new KeyMapping(
            "key.walljump.walljump",
            GLFW.GLFW_KEY_LEFT_SHIFT,
            "key.categories.walljump"
    );

    public static void registerKeyMapping() {
        if (WallJumpConfig.getConfigEntries().enableWallJump && !WallJumpConfig.getConfigEntries().enableClassicWallCling) {
            KeyMappingRegistry.register(KEY_WALLJUMP);
        }
    }

    public static void registerClientTickEvent() {
        if (KEY_WALLJUMP != null) {
            ClientTickEvent.CLIENT_POST.register(m -> {
                if(!WallJumpConfig.getConfigEntries().spaceWallJumpAlt) {
                    toggleWallJump = KEY_WALLJUMP.isDown();
                } else if(WallJumpConfig.getConfigEntries().spaceWallJumpAlt && KEY_WALLJUMP.isDown()) {
                    toggleWallJump = !KEY_WALLJUMP.consumeClick();
                }
            });
        }
    }
}
