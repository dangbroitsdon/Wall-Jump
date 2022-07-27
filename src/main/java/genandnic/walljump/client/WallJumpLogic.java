package genandnic.walljump.client;

import genandnic.walljump.registry.WallJumpConfigRegistry;
import genandnic.walljump.registry.WallJumpEnchantmentRegistry;
import genandnic.walljump.registry.WallJumpKeyBindingRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.MutableText;
import net.minecraft.text.TextContent;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.*;

import static genandnic.walljump.Constants.*;

public class WallJumpLogic implements ClientPlayerEntityWallJumpInterface {
    private static int ticksWallClinged;
    private static double clingX;
    private static double clingZ;
    private static Set<Direction> walls = new HashSet<>();
    private static Set<Direction> staleWalls = new HashSet<>();
    private static double lastJumpY = Double.MAX_VALUE;

    public static void doWallJump() {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        int ticksKeyDown = 0;

        if(!canWallJump()) return;

        if(WallJumpConfigRegistry.getConfig().classicWallJump) {
            ticksKeyDown = pl.input.sneaking ? ticksKeyDown + 1 : 0;
        } else if (!WallJumpConfigRegistry.getConfig().classicWallJump) {
            ticksKeyDown = WallJumpKeyBindingRegistry.toggleWallJump ? ticksKeyDown + 1 : 0;
        }

        if(pl.isOnGround()
                || pl.getAbilities().flying
                || !pl.world.getFluidState(pl.getBlockPos()).isEmpty()
                || pl.isRiding()
        ) {
            ticksWallClinged = 0;
            clingX = Double.NaN;
            clingZ = Double.NaN;
            lastJumpY = Double.MAX_VALUE;
            staleWalls.clear();

            return;
        }

        updateWalls();

        if(ticksWallClinged < 1) {

            if (ticksKeyDown > 0 && !walls.isEmpty() && canWallCling()
            ) {

                pl.limbDistance = 2.5F;
                pl.lastLimbDistance = 2.5F;

                if (WallJumpConfigRegistry.getConfig().autoRotation) {
                    pl.setYaw(getClingDirection().getOpposite().asRotation());
                    pl.prevYaw = pl.getYaw();
                }

                ticksWallClinged = 1;
                clingX = pl.getX();
                clingZ = pl.getZ();

                playHitSound(getWallPos());
                spawnWallParticle(getWallPos());
            }

            return;
        }

        if(getClassicWallJump()
                || pl.isOnGround()
                || !pl.world.getFluidState(pl.getBlockPos()).isEmpty()
                || walls.isEmpty()
                || pl.getHungerManager().getFoodLevel() < 1
        ) {

            ticksWallClinged = 0;

            if((pl.forwardSpeed != 0 || pl.sidewaysSpeed != 0)
                    && !pl.isOnGround()
                    && !walls.isEmpty()
            ) {

                pl.fallDistance = 0.0F;

                PacketByteBuf wjBuf = new PacketByteBuf(Unpooled.buffer());
                wjBuf.writeBoolean(true);
                ClientPlayNetworking.send(WALL_JUMP_PACKET_ID, wjBuf);

                setupWallJump((float) WallJumpConfigRegistry.getConfig().wallJumpHeight);
                staleWalls = new HashSet<>(walls);
            }

            return;
        }

        if(WallJumpConfigRegistry.getConfig().autoRotation) {
            pl.setYaw(getClingDirection().getOpposite().asRotation());
            pl.prevYaw = pl.getYaw();
        }

        pl.setPos(clingX, pl.getY(), clingZ);

        double motionY = pl.getVelocity().getY();

        if(motionY > 0.0) {

            motionY = 0.0;

        } else if(motionY < -0.6) {

            motionY = motionY + 0.2;
            spawnWallParticle(getWallPos());

        } else if(ticksWallClinged++ > WallJumpConfigRegistry.getConfig().wallSlideDelay) {

            motionY = -0.1;
            spawnWallParticle(getWallPos());

        } else {

            motionY = 0.0;
        }

        if(pl.fallDistance > 2) {

            pl.fallDistance = 0;

            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeFloat((float) (motionY * motionY * 8));
            ClientPlayNetworking.send(FALL_DISTANCE_PACKET_ID, passedData);
        }

        pl.setVelocity(0.0, motionY, 0.0);
    }

    private static void updateWalls() {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        Box box = new Box(
                pl.getX() - 0.001,
                pl.getY(),
                pl.getZ() - 0.001,
                pl.getX() + 0.001,
                pl.getY() + pl.getStandingEyeHeight(),
                pl.getZ() + 0.001
        );

        double dist = (pl.getWidth() / 2) + (ticksWallClinged > 0 ? 0.1 : 0.06);

        Box[] axes = {
                box.stretch(0, 0, dist),
                box.stretch(-dist, 0, 0),
                box.stretch(0, 0, -dist),
                box.stretch(dist, 0, 0)
        };

        int i = 0;
        Direction direction;
        walls = new HashSet<>();

        for (Box axis : axes) {
            direction = Direction.fromHorizontal(i++);

            if(!pl.world.isSpaceEmpty(axis)) {
                walls.add(direction);
                pl.horizontalCollision = true;
            }
        }
    }

    private static boolean canWallCling() {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        for (String block : WallJumpConfigRegistry.getConfig().blockBlacklist) {
            BlockState blockState = pl.world.getBlockState(getWallPos());
            if (blockState.getBlock().getTranslationKey().contains(block.toLowerCase())) {
                return false;
           }
        }

        if(pl.isClimbing() || pl.getVelocity().getY() > 0.1 || pl.getHungerManager().getFoodLevel() < 1)
            return false;

        if(pl.isFallFlying() && WallJumpConfigRegistry.getConfig().enableElytraWallCling)
            return true;

        if(pl.isInvisible() || pl.hasStatusEffect(StatusEffects.INVISIBILITY))
            return false;

        if(!pl.world.isSpaceEmpty(pl.getBoundingBox().offset(0, -0.8, 0)))
            return false;

        if(WallJumpConfigRegistry.getConfig().allowReclinging || pl.getY() < lastJumpY - 1)
            return true;

        return !staleWalls.containsAll(walls);
    }

    private static void spawnWallParticle(BlockPos blockPos) {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        BlockState blockState = pl.world.getBlockState(blockPos);
        if(!blockState.isAir()) {

            Vec3d pos = pl.getPos();
            Vec3i motion = getClingDirection().getVector();
            pl.world.addImportantParticle(
                    new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState),
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    motion.getX() * -1.0D,
                    -1.0D,
                    motion.getZ() * -1.0D
            );
        }
    }

    private static Direction getClingDirection() {

        return walls.isEmpty() ? Direction.UP : walls.iterator().next();
    }
    private static BlockPos getWallPos() {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        BlockPos clingPos = pl.getBlockPos().offset(getClingDirection());
        return pl.world.getBlockState(clingPos).getMaterial().isSolid() ? clingPos : clingPos.offset(Direction.UP);
    }
    private static void playBreakSound(BlockPos blockPos) {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        BlockState blockState = pl.world.getBlockState(blockPos);
        BlockSoundGroup soundType = blockState.getBlock().getSoundGroup(blockState);
        pl.playSound(soundType.getFallSound(), soundType.getVolume() * 0.5F, soundType.getPitch());
    }
    private static void playHitSound(BlockPos blockPos) {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        BlockState blockState = pl.world.getBlockState(blockPos);
        BlockSoundGroup soundType = blockState.getBlock().getSoundGroup(blockState);
        pl.playSound(soundType.getHitSound(), soundType.getVolume() * 0.25F, soundType.getPitch());
    }


    private static void setupWallJump(float up) {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        float strafe = Math.signum(pl.sidewaysSpeed) * up * up;
        float forward = Math.signum(pl.forwardSpeed) * up * up;

        float f = 1.0F / MathHelper.sqrt(strafe * strafe + up * up + forward * forward);
        strafe = strafe * f;
        forward = forward * f;

        float f1 = MathHelper.sin(pl.getHeadYaw() * 0.017453292F) * 0.45F;
        float f2 = MathHelper.cos(pl.getHeadYaw() * 0.017453292F) * 0.45F;

        int jumpBoostLevel = 0;
        StatusEffectInstance jumpBoostEffect = pl.getStatusEffect(StatusEffects.JUMP_BOOST);
        if(jumpBoostEffect != null) jumpBoostLevel = jumpBoostEffect.getAmplifier() + 1;

        Vec3d motion = pl.getVelocity();
        pl.setVelocity(
                motion.getX() + (strafe * f2 - forward * f1),
                up + (jumpBoostLevel * 0.125),
                motion.getZ() + (forward * f2 + strafe * f1)
        );

        lastJumpY = pl.getY();
        playBreakSound(getWallPos());
        spawnWallParticle(getWallPos());
    }

    private static boolean canWallJump() {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        if (WallJumpConfigRegistry.getConfig().useWallJump) return true;

        ItemStack stack = pl.getEquippedStack(EquipmentSlot.FEET);
        if(!stack.isEmpty()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
            return enchantments.containsKey(WallJumpEnchantmentRegistry.WALLJUMP_ENCHANTMENT);
        }
        return false;
    }

    private static boolean getClassicWallJump() {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        if (WallJumpConfigRegistry.getConfig().classicWallJump) {
            assert pl != null;
            return !pl.input.sneaking;
        } else {
            return !WallJumpKeyBindingRegistry.toggleWallJump;
        }
    }

















}
