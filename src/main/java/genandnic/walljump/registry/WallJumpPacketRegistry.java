package genandnic.walljump.registry;

import genandnic.walljump.Constants;
import genandnic.walljump.WallJumpConfig;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class WallJumpPacketRegistry {
    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(Constants.FALL_DISTANCE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            float fallDistance = buf.readFloat();
            server.execute(() -> {
                player.fallDistance = fallDistance;
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(Constants.WALL_JUMP_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            boolean didWallJump = buf.readBoolean();

            server.execute(() -> {
                if(didWallJump)
                    player.addExhaustion((float) WallJumpConfig.getConfig().exhaustionWallJump);
            });
        });
    }
}
