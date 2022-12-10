package genandnic.walljump.logic;

import dev.architectury.networking.NetworkManager;
import genandnic.walljump.util.IWallJumpHelper;
import genandnic.walljump.registry.WallJumpReceivers;
import genandnic.walljump.config.WallJumpConfig;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static genandnic.walljump.WallJump.DOUBLE_JUMP_PACKET_ID;

public class DoubleJumpLogic extends Logic {
    private static int jumpCount = 0;
    private static boolean jumpKey = false;

    public static void doDoubleJump() {
        LocalPlayer pl = Minecraft.getInstance().player;
        assert pl != null;

        Vec3 pos = pl.position();
        Vec3 motion = pl.getDeltaMovement();

        AABB box = new AABB(
                pos.x(),
                pos.y() + pl.getEyeHeight(pl.getPose()) * 0.8,
                pos.z(),
                pos.x(),
                pos.y() + pl.getBbHeight(),
                pos.z()
        );

        if(!WallJumpConfig.isModUsable(pl.level)) return;

        if(pl.isOnGround()
                || pl.level.containsAnyLiquid(box)
                || ticksWallClinged > 0
                || pl.isPassenger()
                || pl.getAbilities().flying
        ) {
            jumpCount = IWallJumpHelper.getJumpCount();
        }

        else if(pl.input.jumping)
        {
            if (!jumpKey
                    && jumpCount > 0
                    && motion.y() < 0.333
                    && ticksWallClinged < 1
                    && pl.getFoodData().getFoodLevel() > 0
            ){
                pl.jumpFromGround();

                FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
                packet.writeBoolean(true);
                NetworkManager.sendToServer(DOUBLE_JUMP_PACKET_ID, packet);

                jumpCount--;

                pl.fallDistance = 0.0F;

                WallJumpReceivers.sendFallDistanceMessage(pl.fallDistance);
            }
            jumpKey = true;
        }
        else
        {
            jumpKey = false;
        }
    }
}