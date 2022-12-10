package genandnic.walljump.logic;

import genandnic.walljump.util.IWallJumpHelper;
import genandnic.walljump.registry.WallJumpKeyMappings;
import genandnic.walljump.config.WallJumpConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class WallJumpLogic extends Logic implements IWallJumpHelper {
    private static double clingX, clingZ;
    private static int ticksKeyDown = 0;
    public static double lastJumpY = Double.MAX_VALUE;
    public static Set<Direction> walls = new HashSet<>();
    public static Set<Direction> staleWalls = new HashSet<>();

    public static void doWallJump() {
        LocalPlayer pl = Minecraft.getInstance().player;
        assert pl != null;

        if(!IWallJumpHelper.getWallJumpEligibility()) return;

        if(pl.isOnGround()
                || pl.getAbilities().flying
                || !pl.getLevel().getFluidState(pl.blockPosition()).isEmpty()
                || pl.isPassenger()
        ) {
            ticksWallClinged = 0;
            clingX = Double.NaN;
            clingZ = Double.NaN;
            lastJumpY = Double.MAX_VALUE;
            staleWalls.clear();

            return;
        }

        IWallJumpHelper.updateWalls();

        if(WallJumpConfig.getConfigEntries().enableClassicWallCling) {
            ticksKeyDown = pl.input.shiftKeyDown ? ticksKeyDown + 1 : 0;
        } else {
            ticksKeyDown = WallJumpKeyMappings.toggleWallJump ? ticksKeyDown + 1 : 0;
        }

        if(ticksWallClinged < 1) {

            //Wall Cling
            if (ticksKeyDown > 0 && ticksKeyDown < 4 && !walls.isEmpty() && !pl.isOnGround() && IWallJumpHelper.getWallClingEligibility()) {
                pl.animationSpeed = 2.5F;
                pl.animationSpeedOld = 2.5F;

                if (WallJumpConfig.getConfigEntries().enableAutoRotation) {
                    pl.setYRot(IWallJumpHelper.getWallClingDirection().getOpposite().toYRot());
                    pl.yRotO = pl.getYRot();
                }

                ticksWallClinged = 1;
                clingX = pl.position().x;
                clingZ = pl.position().z;

                IWallJumpHelper.playHitSound(IWallJumpHelper.getWallPos());
                IWallJumpHelper.spawnWallParticle(IWallJumpHelper.getWallPos());
            }

            return;
        }
        // Wall Jump
        if(IWallJumpHelper.getClassicWallJumpEligibility()
                || pl.isOnGround()
                || !pl.getLevel().getFluidState(pl.blockPosition()).isEmpty()
                || walls.isEmpty()
                || pl.getFoodData().getFoodLevel() < 1
        ) {

            ticksWallClinged = 0;

            if((pl.input.forwardImpulse != 0 || pl.input.leftImpulse != 0)
                    && !pl.isOnGround()
                    && !walls.isEmpty()
            ) {

                pl.resetFallDistance();

                doWallClingJump((float) WallJumpConfig.getConfigEntries().heightWallJump);
                staleWalls = new HashSet<>(walls);
            }

            return;
        }

        if(WallJumpConfig.getConfigEntries().enableAutoRotation) {
            pl.setYRot(IWallJumpHelper.getWallClingDirection().getOpposite().toYRot());
            pl.yRotO = pl.getYRot();
        }

        pl.setPos(clingX, pl.position().y, clingZ);

        double motionY = pl.getDeltaMovement().y;

        if(motionY > 0.0) {
            motionY = 0.0;
        } else if(motionY < -0.6) {
            motionY = motionY + 0.2;
            IWallJumpHelper.spawnWallParticle(IWallJumpHelper.getWallPos());
        } else if(++ticksWallClinged > WallJumpConfig.getConfigEntries().delayWallClingSlide) {
            motionY = -0.1;
            IWallJumpHelper.spawnWallParticle(IWallJumpHelper.getWallPos());
        } else {
            motionY = 0.0;
        }

        if(pl.fallDistance > 2) {
            pl.resetFallDistance();
        }

        pl.setDeltaMovement(0.0, motionY, 0.0);
    }

    private static void doWallClingJump(float up) {
        LocalPlayer pl = Minecraft.getInstance().player;
        assert pl != null;

        float strafe = Math.signum(pl.input.leftImpulse) * Mth.square(up);
        float forward = Math.signum(pl.input.forwardImpulse) * Mth.square(up);

        float f = 1.0F / Mth.sqrt(Mth.square(strafe) + Mth.square(up) + Mth.square(forward));
        strafe *= f;
        forward *= f;

        float f1 = Mth.sin(pl.getYHeadRot() * 0.017453292F) * 0.45F;
        float f2 = Mth.cos(pl.getYHeadRot() * 0.017453292F) * 0.45F;

        int jumpBoostLevel = 0;
        MobEffectInstance jumpBoostEffect = pl.getEffect(MobEffects.JUMP);
        if(jumpBoostEffect != null)
            jumpBoostLevel = jumpBoostEffect.getAmplifier() + 1;

        Vec3 motion = pl.getDeltaMovement();
        pl.setDeltaMovement(
                motion.x() + (strafe * f2 - forward * f1),
                up + (jumpBoostLevel * 0.125),
                motion.z() + (forward * f2 + strafe * f1)
        );

        lastJumpY = pl.position().y;
        IWallJumpHelper.playBreakSound(IWallJumpHelper.getWallPos());
        IWallJumpHelper.spawnWallParticle(IWallJumpHelper.getWallPos());
    }


}
