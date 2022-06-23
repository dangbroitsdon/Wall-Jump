package genandnic.walljump.registry;

import genandnic.walljump.WallJumpConfig;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import static genandnic.walljump.Constants.FALL_DISTANCE_PACKET_ID;
import static genandnic.walljump.Constants.WALL_JUMP_PACKET_ID;

public class WallJumpPacketRegistry {
    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(FALL_DISTANCE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            float fallDistance = buf.readFloat();

            server.execute(() -> {
                player.fallDistance = fallDistance;
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(WALL_JUMP_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            boolean didWallJump = buf.readBoolean();

            server.execute(() -> {
                if(didWallJump)
                    player.addExhaustion((float) WallJumpConfig.getConfig().exhaustionWallJump);
            });
        });
    }
}
