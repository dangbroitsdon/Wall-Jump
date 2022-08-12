package genandnic.walljump.util.registry;

import dev.architectury.networking.NetworkManager;
import genandnic.walljump.util.registry.config.WallJumpConfig;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import genandnic.walljump.util.Constants;


public class ReceiversRegistry {
    public static void registerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), Constants.WALL_JUMP_PACKET_ID, (buf, context) -> {
            Player pl = context.getPlayer();
            boolean didWallJump = buf.readBoolean();

            if(didWallJump)
                pl.causeFoodExhaustion((float) WallJumpConfig.getConfigEntries().exhaustionWallJump);
        });
        NetworkManager.registerReceiver(NetworkManager.c2s(), Constants.DOUBLE_JUMP_PACKET_ID, (buf, context) -> {
            Player pl = context.getPlayer();
            boolean didDoubleJump = buf.readBoolean();

            if(didDoubleJump)
                pl.causeFoodExhaustion((float) WallJumpConfig.getConfigEntries().exhaustionDoubleJump);
        });
        NetworkManager.registerReceiver(NetworkManager.c2s(), Constants.FALL_DISTANCE_PACKET_ID, (buf, context) -> {
            Player pl = context.getPlayer();
            if(pl != null)
                pl.fallDistance = buf.readFloat();
        });
    }

    public static void sendWallJumpMessage() {
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBoolean(true);
        NetworkManager.sendToServer(Constants.WALL_JUMP_PACKET_ID, packet);
    }

    public static void sendDoubleJumpMessage() {
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBoolean(true);
        NetworkManager.sendToServer(Constants.DOUBLE_JUMP_PACKET_ID, packet);
    }

    public static void sendFallDistanceMessage(float f) {
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeFloat(f);
        NetworkManager.sendToServer(Constants.FALL_DISTANCE_PACKET_ID, packet);
    }
}
