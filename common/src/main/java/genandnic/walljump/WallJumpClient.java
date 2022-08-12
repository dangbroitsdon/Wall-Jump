package genandnic.walljump;

import genandnic.walljump.util.registry.KeyMappingsRegistry;
import genandnic.walljump.util.FallingSound;
import net.minecraft.client.Minecraft;

import static genandnic.walljump.util.Constants.LOGGER;

public class WallJumpClient {
    public static FallingSound FALLING_SOUND;

    public static void init() {
        KeyMappingsRegistry.registerKeyMapping();
        KeyMappingsRegistry.registerClientTickEvent();
        FALLING_SOUND = new FallingSound(Minecraft.getInstance().player);
        LOGGER.info("[Wall-Jump! UNOFFICIAL] Client is initialized!");
    }
}
