package genandnic.walljump.logic;

import genandnic.walljump.registry.WallJumpReceiverRegistry;
import genandnic.walljump.util.IWallJumpAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class DoubleJumpLogic extends Logic implements IWallJumpAccessor {
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

        if(pl.isOnGround()
                || pl.level.containsAnyLiquid(box)
                || ticksWallClinged > 0
                || pl.isPassenger()
                || pl.getAbilities().flying
        ) {
            jumpCount = IWallJumpAccessor.getJumpCount();
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

                jumpCount--;

                WallJumpReceiverRegistry.sendFallDistanceMessage(pl.fallDistance);
                pl.resetFallDistance();
            }
            jumpKey = true;
        }
        else
        {
            jumpKey = false;
        }
    }
}
