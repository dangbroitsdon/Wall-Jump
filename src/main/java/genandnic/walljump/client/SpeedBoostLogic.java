package genandnic.walljump.client;

import genandnic.walljump.WallJumpConfig;
import genandnic.walljump.registry.WallJumpEnchantmentRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import java.util.Map;

public class SpeedBoostLogic {
    public static void doSpeedBoost() {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        StatusEffectInstance jumpBoostEffect = pl.getStatusEffect(StatusEffects.JUMP_BOOST);

        int jumpBoostLevel = 0;
        if(jumpBoostEffect != null)
            jumpBoostLevel = jumpBoostEffect.getAmplifier() + 1;

        Vec3d pos = pl.getPos();
        Vec3d look = pl.getRotationVector();
        Vec3d motion = pl.getVelocity();

        pl.getAbilities().setFlySpeed((float) (pl.getMovementSpeed() * (pl.isSprinting() ? 1 : 1.3) / 5) * (jumpBoostLevel * 0.5F + 1));

        if (pl.isFallFlying()) {

            if (pl.isSneaking()) {

                if (pl.getPitch() < 30F)
                    pl.setVelocity(motion.subtract(motion.multiply(0.05)));

            } else if (pl.isSprinting()) {

                float elytraSpeedBoost = (float) WallJumpConfig.getConfig().elytraSpeedBoost + (getEquipmentBoost(EquipmentSlot.CHEST) * 0.75F);
                Vec3d boost = new Vec3d(look.getX(), look.getY() + 0.5, look.getZ()).normalize().multiply(elytraSpeedBoost);
                if(motion.length() <= boost.length())
                    pl.setVelocity(motion.add(boost.multiply(0.05)));

                if(boost.length() > 0.5)
                    pl.world.addParticle(ParticleTypes.FIREWORK, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);

            }

        } else if(pl.isSprinting()) {

            float sprintSpeedBoost = (float) WallJumpConfig.getConfig().sprintSpeedBoost + (getEquipmentBoost(EquipmentSlot.FEET) * 0.375F);
            if(!pl.isOnGround())
                sprintSpeedBoost /= 3.125;

            Vec3d boost = new Vec3d(look.getX(), 0.0, look.getZ()).multiply(sprintSpeedBoost * 0.125F);
            pl.setVelocity(motion.add(boost));
        }
    }
    private static int getEquipmentBoost(EquipmentSlot slot) {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        ItemStack stack = pl.getEquippedStack(slot);
        if (!stack.isEmpty()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
            if (enchantments.containsKey(WallJumpEnchantmentRegistry.SPEEDBOOST_ENCHANTMENT))
                return enchantments.get(WallJumpEnchantmentRegistry.SPEEDBOOST_ENCHANTMENT);
        }

        return 0;
    }
}
