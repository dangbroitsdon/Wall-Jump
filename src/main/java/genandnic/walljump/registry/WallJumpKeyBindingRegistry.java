package genandnic.walljump.registry;

import genandnic.walljump.WallJumpConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class WallJumpKeyBindingRegistry {
    public static KeyBinding WallJumpKeyBinding;
    public static boolean toggleWallJump;

    public static void registerKeyBinding() {
        if (WallJumpConfig.getConfig().useWallJump && !WallJumpConfig.getConfig().classicWallJump) {
            WallJumpKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.walljump.walljump",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_SHIFT,
                    "category.walljump.walljump"
            ));
        }
    }

    public static void registerClientEndTickEvents() {
        if (WallJumpConfig.getConfig().useWallJump && !WallJumpConfig.getConfig().classicWallJump) {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                toggleWallJump = WallJumpKeyBinding.isPressed();
            });
        }
    }

    public static KeyBinding getWallJumpKeybind() {
        return WallJumpKeyBinding;
    }
}
