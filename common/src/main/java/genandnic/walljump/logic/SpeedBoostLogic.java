package genandnic.walljump.logic;

import genandnic.walljump.util.IWallJumpAccessor;
import genandnic.walljump.config.WallJumpConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;

public class SpeedBoostLogic implements IWallJumpAccessor {
    public static void doSpeedBoost() {
        LocalPlayer pl = Minecraft.getInstance().player;

        assert pl != null;
        MobEffectInstance jumpBoostEffect = pl.getEffect(MobEffects.JUMP);

        int jumpBoostLevel = 0;

        if(!WallJumpConfig.isModUsable(pl.getLevel()) || WallJumpConfig.getConfigEntries().sprintSpeedBoost == 0.0 && WallJumpConfig.getConfigEntries().elytraSpeedBoost == 0.0) return;

        if(jumpBoostEffect != null)
            jumpBoostLevel = jumpBoostEffect.getAmplifier() + 1;

        Vec3 pos = pl.position();
        Vec3 look = pl.getLookAngle();
        Vec3 motion = pl.getDeltaMovement();

        // NOTE: might cause issues
        pl.flyingSpeed = (float) (pl.getSpeed() * (pl.isSprinting() ? 1 : 1.3) / 5) * (jumpBoostLevel * 0.5F + 1);

        if (pl.isFallFlying()) {
            if (pl.isCrouching()) {
                if (pl.getXRot() < 30F)
                    pl.setDeltaMovement(motion.subtract(motion.multiply(0.05, 0.05, 0.05)));
            } else if (pl.isSprinting()) {
                float elytraSpeedBoost = (float) WallJumpConfig.getConfigEntries().elytraSpeedBoost + (IWallJumpAccessor.getEquipmentBoost(EquipmentSlot.CHEST) * 0.75F);
                Vec3 boost = new Vec3(look.x, look.y + 0.5, look.z).normalize().scale(elytraSpeedBoost);
                if(motion.length() <= boost.length())
                    pl.setDeltaMovement(motion.add(boost.multiply(0.05, 0.05, 0.05)));

                if(boost.length() > 0.5)
                    pl.level.addParticle(ParticleTypes.FIREWORK, pos.x, pos.y, pos.z, 0, 0, 0);

            }

        } else if(pl.isSprinting()) {
            float sprintSpeedBoost = (float) WallJumpConfig.getConfigEntries().sprintSpeedBoost + (IWallJumpAccessor.getEquipmentBoost(EquipmentSlot.FEET) * 0.375F);
            if(!pl.isOnGround())
                sprintSpeedBoost /= 3.125;

            Vec3 boost = new Vec3(look.x, 0.0, look.z).scale(sprintSpeedBoost * 0.125F);
            pl.setDeltaMovement(motion.add(boost));
        }
    }
}