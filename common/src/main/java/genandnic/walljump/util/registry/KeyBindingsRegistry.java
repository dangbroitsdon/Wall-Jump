package genandnic.walljump.util.registry;

import me.shedaniel.architectury.event.events.client.ClientTickEvent;
import me.shedaniel.architectury.registry.KeyBindings;
import genandnic.walljump.util.registry.config.WallJumpConfig;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBindingsRegistry {
    public static boolean toggleWallJump;
    public static KeyMapping KEY_WALLJUMP;

    public static void registerKeyMapping() {
        if (WallJumpConfig.getConfigEntries().enableWallJump && !WallJumpConfig.getConfigEntries().enableClassicWallCling) {
            KEY_WALLJUMP = new KeyMapping(
                    "key.walljump.walljump",
                    GLFW.GLFW_KEY_LEFT_SHIFT,
                    "key.categories.walljump"
            );

            KeyBindings.registerKeyBinding(KEY_WALLJUMP);
        };
    }

    public static void registerClientTickEvent() {
        if (KEY_WALLJUMP != null) {
            ClientTickEvent.CLIENT_POST.register(m -> {
                toggleWallJump = KEY_WALLJUMP.isDown();
            });
        }
    }
}
