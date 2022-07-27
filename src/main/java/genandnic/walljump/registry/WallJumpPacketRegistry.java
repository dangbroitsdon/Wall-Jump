package genandnic.walljump.registry;

import genandnic.walljump.Constants;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import static genandnic.walljump.Constants.DOUBLE_JUMP_PACKET_ID;

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
                if (didWallJump)
                    player.addExhaustion((float) WallJumpConfigRegistry.getConfig().exhaustionWallJump);
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(DOUBLE_JUMP_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            boolean didDoubleJump = buf.readBoolean();

            server.execute(() ->{
                if(didDoubleJump)
                    player.addExhaustion((float) WallJumpConfigRegistry.getConfig().exhaustionDoubleJump);
            });
        });
    }}
