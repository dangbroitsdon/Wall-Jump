package genandnic.walljump.registry;

import dev.architectury.networking.NetworkManager;
import genandnic.walljump.config.WallJumpConfig;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;

import static genandnic.walljump.WallJump.*;

public class WallJumpServerReceivers {
    public static boolean serverConfigSynced;
    public static void registerServerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), WALL_JUMP_PACKET_ID, (buf, context) -> {
            Player pl = context.getPlayer();
            boolean didWallJump = buf.readBoolean();

            if(didWallJump)
                pl.causeFoodExhaustion((float) WallJumpConfig.getConfigEntries().exhaustionWallJump);
        });

        NetworkManager.registerReceiver(NetworkManager.c2s(), DOUBLE_JUMP_PACKET_ID, (buf, context) -> {
            Player pl = context.getPlayer();
            boolean didDoubleJump = buf.readBoolean();

            if(didDoubleJump)
                pl.causeFoodExhaustion((float) WallJumpConfig.getConfigEntries().exhaustionDoubleJump * (float) WallJumpConfig.getConfigEntries().exhaustionDoubleJumpMultiplier);
        });

        NetworkManager.registerReceiver(NetworkManager.c2s(), FALL_DISTANCE_PACKET_ID, (buf, context) -> {
            Player pl = context.getPlayer();
            if(pl != null)
                pl.fallDistance = buf.readFloat();
        });
    }

    public static void registerClientReceivers() {
        IntFunction<List<String>> i = (x) -> Collections.singletonList(Integer.toString(x));

        NetworkManager.registerReceiver(NetworkManager.s2c(), SERVER_CONFIG_PACKET_ID, (buf, context) -> {
            WallJumpConfig.getConfigEntries().enableWallJump = buf.readBoolean();
            if(!WallJumpConfig.getConfigEntries().blockBlacklist.isEmpty()) {
                WallJumpConfig.getConfigEntries().blockBlacklist = buf.readCollection(i, FriendlyByteBuf::readUtf);
            }
            WallJumpConfig.getConfigEntries().enableElytraWallCling = buf.readBoolean();
            WallJumpConfig.getConfigEntries().enableClassicWallCling = buf.readBoolean();
            WallJumpConfig.getConfigEntries().enableReclinging = buf.readBoolean();
            WallJumpConfig.getConfigEntries().enableAutoRotation = buf.readBoolean();
            WallJumpConfig.getConfigEntries().heightWallJump = buf.readDouble();
            WallJumpConfig.getConfigEntries().delayWallClingSlide = buf.readInt();
            WallJumpConfig.getConfigEntries().exhaustionWallJump = buf.readDouble();
            WallJumpConfig.getConfigEntries().enableDoubleJump = buf.readBoolean();
            WallJumpConfig.getConfigEntries().countDoubleJump = buf.readInt();
            WallJumpConfig.getConfigEntries().exhaustionDoubleJump = buf.readDouble();
            WallJumpConfig.getConfigEntries().playFallingSound = buf.readBoolean();
            WallJumpConfig.getConfigEntries().minFallDistance = buf.readDouble();
            WallJumpConfig.getConfigEntries().elytraSpeedBoost = buf.readDouble();
            WallJumpConfig.getConfigEntries().sprintSpeedBoost = buf.readDouble();
            WallJumpConfig.getConfigEntries().enableStepAssist = buf.readBoolean();
            System.out.println("[Wall-Jump! UNOFFICIAL] Server Config has been received and synced on Client!");
            serverConfigSynced = true;
        });
    }

    public static void sendFallDistanceMessage(float f) {
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeFloat(f);
        NetworkManager.sendToServer(FALL_DISTANCE_PACKET_ID, packet);
    }
}
