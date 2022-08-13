package genandnic.walljump.util.registry;

import dev.architectury.networking.NetworkManager;
import genandnic.walljump.util.registry.config.WallJumpConfig;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;

import static genandnic.walljump.util.Constants.*;

public class ReceiversRegistry {
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
                pl.causeFoodExhaustion((float) WallJumpConfig.getConfigEntries().exhaustionDoubleJump);
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
            WallJumpConfig.getConfigEntries().enableWallJumpEnchantment = buf.readBoolean();
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
            WallJumpConfig.getConfigEntries().enableDoubleJumpEnchantment = buf.readBoolean();
            WallJumpConfig.getConfigEntries().countDoubleJump = buf.readInt();
            WallJumpConfig.getConfigEntries().exhaustionDoubleJump = buf.readDouble();
            WallJumpConfig.getConfigEntries().playFallingSound = buf.readBoolean();
            WallJumpConfig.getConfigEntries().minFallDistance = buf.readDouble();
            WallJumpConfig.getConfigEntries().elytraSpeedBoost = buf.readDouble();
            WallJumpConfig.getConfigEntries().sprintSpeedBoost = buf.readDouble();
            WallJumpConfig.getConfigEntries().enableSpeedBoostEnchantment = buf.readBoolean();
            WallJumpConfig.getConfigEntries().enableStepAssist = buf.readBoolean();
            System.out.println("[Wall-Jump! UNOFFICIAL] Server Config has been received and synced on Client!");
            serverConfigSynced = true;
        });
    }

    public static void sendWallJumpMessage() {
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBoolean(true);
        NetworkManager.sendToServer(WALL_JUMP_PACKET_ID, packet);
    }

    public static void sendDoubleJumpMessage() {
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBoolean(true);
        NetworkManager.sendToServer(DOUBLE_JUMP_PACKET_ID, packet);
    }

    public static void sendFallDistanceMessage(float f) {
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeFloat(f);
        NetworkManager.sendToServer(FALL_DISTANCE_PACKET_ID, packet);
    }

    public static void sendServerConfigMessage(ServerPlayer pl) {
        System.out.println("[Wall-Jump! UNOFFICIAL] Syncing Server Config");
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableWallJump);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableWallJumpEnchantment);
        if(!WallJumpConfig.getConfigEntries().blockBlacklist.isEmpty()) {
            packet.writeCollection(WallJumpConfig.getConfigEntries().blockBlacklist, FriendlyByteBuf::writeUtf);
        }
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableElytraWallCling);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableClassicWallCling);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableReclinging);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableAutoRotation);
        packet.writeDouble(WallJumpConfig.getConfigEntries().heightWallJump);
        packet.writeInt(WallJumpConfig.getConfigEntries().delayWallClingSlide);
        packet.writeDouble(WallJumpConfig.getConfigEntries().exhaustionWallJump);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableDoubleJump);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableDoubleJumpEnchantment);
        packet.writeInt(WallJumpConfig.getConfigEntries().countDoubleJump);
        packet.writeDouble(WallJumpConfig.getConfigEntries().exhaustionDoubleJump);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().playFallingSound);
        packet.writeDouble(WallJumpConfig.getConfigEntries().minFallDistance);
        packet.writeDouble(WallJumpConfig.getConfigEntries().elytraSpeedBoost);
        packet.writeDouble(WallJumpConfig.getConfigEntries().sprintSpeedBoost);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableSpeedBoostEnchantment);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableStepAssist);
        NetworkManager.sendToPlayer(pl, SERVER_CONFIG_PACKET_ID, packet);
        System.out.println("[Wall-Jump! UNOFFICIAL] Synced Server Config");
    }
}
