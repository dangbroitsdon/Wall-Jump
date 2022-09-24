package genandnic.walljump.util;

import genandnic.walljump.logic.WallJumpLogic;
import genandnic.walljump.util.registry.WallJumpEnchantments;
import genandnic.walljump.util.registry.WallJumpKeyBindings;
import genandnic.walljump.util.registry.config.WallJumpConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Map;

public interface IWallJumpAccessor {
    int ticksWallClinged = 0;

    // Wall Jump
    static boolean getClassicWallJumpEligibility() {
        LocalPlayer pl = Minecraft.getInstance().player;

        assert pl != null;
        return WallJumpConfig.getConfigEntries().enableClassicWallCling ? !pl.input.shiftKeyDown : !WallJumpKeyBindings.toggleWallJump;
    }

    static boolean getWallJumpEligibility() {
        LocalPlayer pl = Minecraft.getInstance().player;
        assert pl != null;

        if (WallJumpConfig.getConfigEntries().enableWallJump) return true;

        ItemStack stack = pl.getItemBySlot(EquipmentSlot.FEET);
        if(!stack.isEmpty()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
            return enchantments.containsKey(WallJumpEnchantments.WALLJUMP_ENCHANTMENT);
        }
        return false;
    }

    static boolean getWallClingEligibility() {
        LocalPlayer pl = Minecraft.getInstance().player;

        assert pl != null;
        BlockState blockState = pl.level.getBlockState(getWallPos());

        // No Wall Clinging on these Blocks!
        for (String block : WallJumpConfig.getConfigEntries().blockBlacklist) {
            if (blockState.getBlock().getDescriptionId().contains(block.toLowerCase())) {
                return false;
            }
        }

        // If on ladder, Y Velocity greater than .1 or NO FOOD, etc. then no WALLCLINGING
        if(pl.onClimbable()
                || pl.getDeltaMovement().y > 0.1
                || pl.getFoodData().getFoodLevel() < 1
                || (pl.isFallFlying() && !WallJumpConfig.getConfigEntries().enableElytraWallCling)
                || pl.isInvisible()
        ) {
            return false;
        }

        // Remove Wall Clinging whilst Block is under player
        if(!pl.level.noCollision(pl.getBoundingBox().move(0, -0.8, 0))) {
            return false;
        }

        // Allow ReClinging
        if(WallJumpConfig.getConfigEntries().enableReclinging || pl.position().y < WallJumpLogic.lastJumpY - 1) {
            return true;
        }

        // When the Original Walls Don't Contain the New Walls!
        return !WallJumpLogic.staleWalls.containsAll(WallJumpLogic.walls);
    }

    static Direction getWallClingDirection() {
        return WallJumpLogic.walls.isEmpty() ? Direction.UP : WallJumpLogic.walls.iterator().next();
    }

    static BlockPos getWallPos() {
        LocalPlayer pl = Minecraft.getInstance().player;

        assert pl != null;
        BlockPos clingPos = pl.blockPosition().relative(getWallClingDirection());
        return pl.level.getBlockState(clingPos).getMaterial().isSolid() ? clingPos : clingPos.relative(Direction.UP);
    }

    static void updateWalls() {
        LocalPlayer pl = Minecraft.getInstance().player;

        assert pl != null;
        AABB box = new AABB(
                pl.getX() - 0.001,
                pl.getY(),
                pl.getZ() - 0.001,
                pl.getX() + 0.001,
                pl.getY() + pl.getEyeHeight(),
                pl.getZ() + 0.001
        );

        double dist = (pl.getBbWidth() / 2) + (WallJumpLogic.ticksWallClinged > 0 ? 0.1 : 0.06);

        AABB[] axes = {
                box.expandTowards(0, 0, dist),
                box.expandTowards(-dist, 0, 0),
                box.expandTowards(0, 0, -dist),
                box.expandTowards(dist, 0, 0)
        };

        int i = 0;
        Direction direction;
        WallJumpLogic.walls = new HashSet<>();

        for (AABB axis : axes) {
            direction = Direction.from2DDataValue(i++);

            if (!pl.level.noCollision(axis)) {
                WallJumpLogic.walls.add(direction);
                pl.horizontalCollision = true;
            }
        }
    }

    static void spawnWallParticle(BlockPos blockPos) {
        LocalPlayer pl = Minecraft.getInstance().player;

        assert pl != null;
        BlockState blockState = pl.level.getBlockState(blockPos);

        // Not air blocks
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            Vec3 pos = pl.position();
            Vec3i motion = getWallClingDirection().getNormal();
            pl.level.addParticle(
                    new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                    pos.x,
                    pos.y,
                    pos.z,
                    motion.getX() * -1.0D,
                    -1.0D,
                    motion.getZ() * -1.0D
            );
        }
    }

    static void playBreakSound(BlockPos blockPos) {
        LocalPlayer pl = Minecraft.getInstance().player;

        assert pl != null;
        BlockState blockState = pl.level.getBlockState(blockPos);
        SoundType soundType = blockState.getBlock().getSoundType(blockState);
        pl.playSound(soundType.getFallSound(), soundType.getVolume() * 0.5F, soundType.getPitch());
    }

    static void playHitSound(BlockPos blockPos) {
        LocalPlayer pl = Minecraft.getInstance().player;

        assert pl != null;
        BlockState blockState = pl.level.getBlockState(blockPos);
        SoundType soundType = blockState.getBlock().getSoundType(blockState);
        pl.playSound(soundType.getHitSound(), soundType.getVolume() * 0.25F, soundType.getPitch());
    }

    // Speed Boost
    static int getEquipmentBoost(EquipmentSlot slot) {
        LocalPlayer pl = Minecraft.getInstance().player;

        assert pl != null;
        ItemStack stack = pl.getItemBySlot(slot);

        if (!stack.isEmpty()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
            if (enchantments.containsKey(WallJumpEnchantments.SPEEDBOOST_ENCHANTMENT))
                return enchantments.get(WallJumpEnchantments.SPEEDBOOST_ENCHANTMENT);
        }
        return 0;
    }

    // Double Jump
    static int getJumpCount() {
        LocalPlayer pl = Minecraft.getInstance().player;

        int jumpCount = 0;

        if(WallJumpConfig.getConfigEntries().enableDoubleJump)
            jumpCount += WallJumpConfig.getConfigEntries().countDoubleJump;

        assert pl != null;
        ItemStack stack = pl.getItemBySlot(EquipmentSlot.FEET);
        if(!stack.isEmpty()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
            if(enchantments.containsKey(WallJumpEnchantments.DOUBLEJUMP_ENCHANTMENT))
                jumpCount += enchantments.get(WallJumpEnchantments.DOUBLEJUMP_ENCHANTMENT);
        }
        return jumpCount;
    }
}