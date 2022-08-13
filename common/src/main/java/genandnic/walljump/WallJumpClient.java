package genandnic.walljump;

import genandnic.walljump.util.registry.KeyBindingsRegistry;
import genandnic.walljump.util.FallingSound;
import genandnic.walljump.util.registry.ReceiversRegistry;
import net.minecraft.client.Minecraft;

import static genandnic.walljump.util.Constants.LOGGER;

public class WallJumpClient {
    public static FallingSound FALLING_SOUND;

    public static void init() {
        KeyBindingsRegistry.registerKeyMapping();
        KeyBindingsRegistry.registerClientTickEvent();
        ReceiversRegistry.registerClientReceivers();
        FALLING_SOUND = new FallingSound(Minecraft.getInstance().player);
        LOGGER.info("[Wall-Jump! UNOFFICIAL] Client is initialized!");
    }
}
