package genandnic.walljump;

import genandnic.walljump.util.sound.WallJumpFallingSound;
import genandnic.walljump.registry.WallJumpEnchantments;
import genandnic.walljump.registry.WallJumpKeyMappings;
import genandnic.walljump.config.WallJumpConfig;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WallJump {
    public static final String MOD_ID = "walljump";
    public static final Logger LOGGER = LogManager.getLogger("WallJump");

    public static WallJumpFallingSound FALLING_SOUND;

    public static void initClient() {
        WallJumpKeyMappings.registerKeyMapping();
        WallJumpKeyMappings.registerClientTickEvent();
        FALLING_SOUND = new WallJumpFallingSound(Minecraft.getInstance().player);
        LOGGER.info("[Wall-Jump! UNOFFICIAL] Client is initialized!");
    }
    public static void initBase() {
        WallJumpConfig.registerConfig();
        WallJumpEnchantments.registerEnchantments();
        LOGGER.info("[Wall-Jump! UNOFFICIAL] is initialized!");
    }
}
