package genandnic.walljump;

import genandnic.walljump.util.WallJumpFallingSound;
import genandnic.walljump.registry.WallJumpEnchantments;
import genandnic.walljump.registry.WallJumpKeyMappings;
import genandnic.walljump.registry.WallJumpServerReceivers;
import genandnic.walljump.config.WallJumpConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WallJump {
    public static final String MOD_ID = "walljump";
    public static final Logger LOGGER = LogManager.getLogger("WallJump");
    public static final ResourceLocation WALL_JUMP_PACKET_ID = new ResourceLocation(MOD_ID, "walljump_packet");
    public static final ResourceLocation DOUBLE_JUMP_PACKET_ID = new ResourceLocation(MOD_ID, "doublejump_packet");
    public static final ResourceLocation FALL_DISTANCE_PACKET_ID = new ResourceLocation(MOD_ID, "falldistance_packet");
    public static final ResourceLocation SERVER_CONFIG_PACKET_ID = new ResourceLocation(MOD_ID, "serverconfig_packet");

    public static WallJumpFallingSound FALLING_SOUND;

    public static void initClient() {
        WallJumpKeyMappings.registerKeyMapping();
        WallJumpKeyMappings.registerClientTickEvent();
        WallJumpServerReceivers.registerClientReceivers();
        FALLING_SOUND = new WallJumpFallingSound(Minecraft.getInstance().player);
        LOGGER.info("[Wall-Jump! UNOFFICIAL] Client is initialized!");
    }
    public static void initBase() {
        WallJumpConfig.registerConfig();
        WallJumpServerReceivers.registerServerReceivers();
        WallJumpEnchantments.registerEnchantments();
        LOGGER.info("[Wall-Jump! UNOFFICIAL] is initialized!");
    }
}
