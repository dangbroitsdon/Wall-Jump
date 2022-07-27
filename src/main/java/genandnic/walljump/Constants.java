package genandnic.walljump;

import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Constants {
    public static final Logger LOGGER = LogManager.getLogger("WallJumpClient");
    public static final String MOD_ID = "walljump";
    public static final Identifier FALL_DISTANCE_PACKET_ID = new Identifier("walljump", "falldistance");
    public static final Identifier WALL_JUMP_PACKET_ID = new Identifier("walljump", "walljump");
    public static final Identifier DOUBLE_JUMP_PACKET_ID = new Identifier("walljump", "doublejump");
}
