package genandnic.walljump.registry;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import static genandnic.walljump.WallJump.FALL_DISTANCE_PACKET_ID;

public class WallJumpReceiverRegistry {
    public static void registerServerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), FALL_DISTANCE_PACKET_ID, (buf, context) -> {
            Player pl = context.getPlayer();
            if(pl != null)
                pl.fallDistance = buf.readFloat();
        });
    }

    public static void sendFallDistanceMessage(float f) {
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeFloat(f);
        NetworkManager.sendToServer(FALL_DISTANCE_PACKET_ID, packet);
    }
}
