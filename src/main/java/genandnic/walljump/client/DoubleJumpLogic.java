package genandnic.walljump.client;

import genandnic.walljump.registry.WallJumpConfigRegistry;
import genandnic.walljump.registry.WallJumpEnchantmentRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Map;

import static genandnic.walljump.Constants.*;

public class DoubleJumpLogic implements ClientPlayerEntityWallJumpInterface {
    private static int jumpCount = 0;
    private static boolean jumpKey = false;

    public static void doDoubleJump() {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        Vec3d pos = pl.getPos();
        Vec3d motion = pl.getVelocity();

        Box box = new Box(
                pos.getX(),
                pos.getY() + pl.getEyeHeight(pl.getPose()) * 0.8,
                pos.getZ(),
                pos.getX(),
                pos.getY() + pl.getHeight(),
                pos.getZ()
        );

        if(pl.isOnGround()
                || pl.world.containsFluid(box)
                || ticksWallClinged > 0
                || pl.isRiding()
                || pl.getAbilities().allowFlying
        ) {
            jumpCount = getJumpCount();
        }

        else if(pl.input.jumping)
        {
            if (!jumpKey
                    && jumpCount > 0
                    && motion.getY() < 0.333
                    && ticksWallClinged < 1
                    && pl.getHungerManager().getFoodLevel() > 0
            ){
                pl.jump();

                PacketByteBuf djBuf = new PacketByteBuf(Unpooled.buffer());
                djBuf.writeBoolean(true);
                ClientPlayNetworking.send(DOUBLE_JUMP_PACKET_ID, djBuf);

                jumpCount--;

                pl.fallDistance = 0.0F;

                PacketByteBuf fdBuf = new PacketByteBuf(Unpooled.buffer());
                fdBuf.writeFloat(pl.fallDistance);
                ClientPlayNetworking.send(FALL_DISTANCE_PACKET_ID, fdBuf);
            }
            jumpKey = true;
        }
        else
        {
            jumpKey = false;
        }
    }

    private static int getJumpCount() {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        int jumpCount = 0;
        if(WallJumpConfigRegistry.getConfig().useDoubleJump)
            jumpCount += 1;

        ItemStack stack = pl.getEquippedStack(EquipmentSlot.FEET);
        if(!stack.isEmpty()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
            if(enchantments.containsKey(WallJumpEnchantmentRegistry.DOUBLEJUMP_ENCHANTMENT))
                jumpCount += enchantments.get(WallJumpEnchantmentRegistry.DOUBLEJUMP_ENCHANTMENT);
        }
        return jumpCount;
    }
}
