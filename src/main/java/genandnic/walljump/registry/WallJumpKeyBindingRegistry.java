package genandnic.walljump.registry;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class WallJumpKeyBindingRegistry {
    public static KeyBinding WallJumpKeyBinding;
    public static boolean toggleWallJump;

    public static void registerKeyBinding() {
        if (WallJumpConfigRegistry.getConfig().useWallJump && !WallJumpConfigRegistry.getConfig().classicWallJump) {
            WallJumpKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.walljump.walljump",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_SHIFT,
                    "category.walljump.walljump"
            ));
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                toggleWallJump = WallJumpKeyBindingRegistry.WallJumpKeyBinding.isPressed();
            });
        }
    }
    public static KeyBinding getWallJumpKeybind() {
        return WallJumpKeyBinding;
    }
}
