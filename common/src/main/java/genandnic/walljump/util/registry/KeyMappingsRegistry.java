package genandnic.walljump.util.registry;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import genandnic.walljump.util.registry.config.WallJumpConfig;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyMappingsRegistry {
    public static boolean toggleWallJump;
    public static KeyMapping KEY_WALLJUMP;

    public static void registerKeyMapping() {
        if (WallJumpConfig.getConfigEntries().enableWallJump && !WallJumpConfig.getConfigEntries().enableClassicWallCling) {
            KEY_WALLJUMP = new KeyMapping(
                    "key.walljump.walljump",
                    GLFW.GLFW_KEY_LEFT_SHIFT,
                    "key.categories.walljump"
            );

            KeyMappingRegistry.register(KEY_WALLJUMP);
        }
    }

    public static void registerClientTickEvent() {
        ClientTickEvent.CLIENT_POST.register(m -> {
            toggleWallJump = KEY_WALLJUMP.isDown();
        });
    }
}
