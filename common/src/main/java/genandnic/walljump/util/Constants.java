package genandnic.walljump.util;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {
    public static final Logger LOGGER = LogManager.getLogger("WallJump");
    public static final String MOD_ID = "walljump";
    public static final ResourceLocation WALL_JUMP_PACKET_ID = new ResourceLocation(MOD_ID, "walljump_packet");
    public static final ResourceLocation DOUBLE_JUMP_PACKET_ID = new ResourceLocation(MOD_ID, "doublejump_packet");
    public static final ResourceLocation FALL_DISTANCE_PACKET_ID = new ResourceLocation(MOD_ID, "falldistance_packet");
}