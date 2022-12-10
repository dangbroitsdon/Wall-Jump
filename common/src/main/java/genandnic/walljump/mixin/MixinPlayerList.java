package genandnic.walljump.mixin;

import dev.architectury.networking.NetworkManager;
import genandnic.walljump.config.WallJumpConfig;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static genandnic.walljump.WallJump.SERVER_CONFIG_PACKET_ID;

@Mixin(PlayerList.class)
public class MixinPlayerList {
    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void wju$sendServerConfigSyncPacket(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        System.out.println("[Wall-Jump! UNOFFICIAL] Syncing Server Config");
        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());

        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableWallJump);
        packet.writeCollection(WallJumpConfig.getConfigEntries().blockBlacklist, FriendlyByteBuf::writeUtf);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableElytraWallCling);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableInvisibleWallCling);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableClassicWallCling);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableReclinging);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableAutoRotation);
        packet.writeDouble(WallJumpConfig.getConfigEntries().heightWallJump);
        packet.writeInt(WallJumpConfig.getConfigEntries().delayWallClingSlide);
        packet.writeDouble(WallJumpConfig.getConfigEntries().exhaustionWallJump);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableDoubleJump);
        packet.writeInt(WallJumpConfig.getConfigEntries().countDoubleJump);
        packet.writeDouble(WallJumpConfig.getConfigEntries().exhaustionDoubleJump);
        packet.writeDouble(WallJumpConfig.getConfigEntries().exhaustionDoubleJumpMultiplier);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().playFallingSound);
        packet.writeDouble(WallJumpConfig.getConfigEntries().minFallDistance);
        packet.writeDouble(WallJumpConfig.getConfigEntries().elytraSpeedBoost);
        packet.writeDouble(WallJumpConfig.getConfigEntries().sprintSpeedBoost);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().enableStepAssist);
        packet.writeBoolean(WallJumpConfig.getConfigEntries().spaceWallJumpAlt);

        NetworkManager.sendToPlayer(serverPlayer, SERVER_CONFIG_PACKET_ID, packet);
        System.out.println("[Wall-Jump! UNOFFICIAL] Synced Server Config");
    }
}