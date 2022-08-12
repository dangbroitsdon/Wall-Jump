package genandnic.walljump.logic;

import genandnic.walljump.util.IWallJumpAccessor;
import genandnic.walljump.util.registry.KeyBindingsRegistry;
import genandnic.walljump.util.registry.ReceiversRegistry;
import genandnic.walljump.util.registry.config.WallJumpConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class WallJumpLogic implements IWallJumpAccessor {
    private static int ticksWallClinged;
    private static double clingX, clingZ;
    private static double lastJumpY = Double.MAX_VALUE;
    private static Set<Direction> walls = new HashSet<>();
    private static Set<Direction> staleWalls = new HashSet<>();

    public static void doWallJump() {
        LocalPlayer pl = Minecraft.getInstance().player;
        assert pl != null;

        int ticksKeyDown = 0;

        if(WallJumpConfig.getConfigEntries().enableClassicWallCling) {
            ticksKeyDown = pl.input.shiftKeyDown ? ticksKeyDown + 1 : 0;
        } else if (!WallJumpConfig.getConfigEntries().enableClassicWallCling) {
            ticksKeyDown = KeyBindingsRegistry.toggleWallJump ? ticksKeyDown + 1 : 0;
        }

        if(!IWallJumpAccessor.getWallJumpEligibility() || pl.level.isClientSide()) return;

        if(pl.isOnGround()
                || pl.abilities.flying
                || !pl.level.getFluidState(pl.blockPosition()).isEmpty()
                || pl.isPassenger()
        ) {
            ticksWallClinged = 0;
            clingX = Double.NaN;
            clingZ = Double.NaN;
            lastJumpY = Double.MAX_VALUE;
            staleWalls.clear();

            return;
        }

        walls = IWallJumpAccessor.getWalls();

        if(ticksWallClinged < 1) {

            //Wall Cling
            if (ticksKeyDown > 0 && ticksKeyDown < 4 && !walls.isEmpty() && !pl.isOnGround() && IWallJumpAccessor.getWallClingEligibility(lastJumpY, staleWalls, walls)) {
                pl.animationSpeed = 2.5F;
                pl.animationSpeedOld = 2.5F;

                if (WallJumpConfig.getConfigEntries().enableAutoRotation) {
                    pl.yRot = (IWallJumpAccessor.getWallClingDirection(walls).getOpposite().toYRot());
                    pl.yRotO = pl.yRot;
                }

                ticksWallClinged = 1;
                clingX = pl.position().x;
                clingZ = pl.position().z;

                IWallJumpAccessor.playHitSound(IWallJumpAccessor.getWallPos(walls));
                IWallJumpAccessor.spawnWallParticle(IWallJumpAccessor.getWallPos(walls), walls);
            }

            return;
        }
        // Wall Jump
        if(IWallJumpAccessor.getClassicWallJumpEligibility()
                || pl.isOnGround()
                || !pl.level.getFluidState(pl.blockPosition()).isEmpty()
                || walls.isEmpty()
                || pl.getFoodData().getFoodLevel() < 1
        ) {

            ticksWallClinged = 0;

            if((pl.input.forwardImpulse != 0 || pl.input.leftImpulse != 0)
                    && !pl.isOnGround()
                    && !walls.isEmpty()
            ) {

                pl.fallDistance = 0.0F;

                ReceiversRegistry.sendWallJumpMessage();

                doWallClingJump((float) WallJumpConfig.getConfigEntries().heightWallJump);
                staleWalls = new HashSet<>(walls);
            }

            return;
        }

        if(WallJumpConfig.getConfigEntries().enableAutoRotation) {
            pl.yRot = IWallJumpAccessor.getWallClingDirection(walls).getOpposite().toYRot();
            pl.yRotO = pl.yRot;
        }

        pl.setPos(clingX, pl.position().y, clingZ);

        double motionY = pl.getDeltaMovement().y();

        if(motionY > 0.0) {
            motionY = 0.0;
        } else if(motionY < -0.6) {
            motionY = motionY + 0.2;
            IWallJumpAccessor.spawnWallParticle(IWallJumpAccessor.getWallPos(walls), walls);
        } else if(ticksWallClinged++ > WallJumpConfig.getConfigEntries().delayWallClingSlide) {
            motionY = -0.1;
            IWallJumpAccessor.spawnWallParticle(IWallJumpAccessor.getWallPos(walls), walls);
        } else {
            motionY = 0.0;
        }

        if(pl.fallDistance > 2) {
            pl.fallDistance = 0;
            ReceiversRegistry.sendFallDistanceMessage(pl.fallDistance);
        }

        pl.setDeltaMovement(0.0, motionY, 0.0);
    }

    private static void doWallClingJump(float up) {
        LocalPlayer pl = Minecraft.getInstance().player;
        assert pl != null;

        float strafe = Math.signum(pl.input.leftImpulse) * up * up;
        float forward = Math.signum(pl.input.forwardImpulse) * up * up;

        float f = 1.0F / Mth.sqrt(strafe * strafe + up * up + forward * forward);
        strafe = strafe * f;
        forward = forward * f;

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
        IWallJumpAccessor.playBreakSound(IWallJumpAccessor.getWallPos(walls));
        IWallJumpAccessor.spawnWallParticle(IWallJumpAccessor.getWallPos(walls), walls);
    }


}
