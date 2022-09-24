package genandnic.walljump.util.registry;

import dev.architectury.registry.registries.DeferredRegister;
import genandnic.walljump.WallJump;
import genandnic.walljump.util.registry.config.WallJumpConfig;
import genandnic.walljump.util.registry.enchantments.DoubleJumpEnchantment;
import genandnic.walljump.util.registry.enchantments.SpeedBoostEnchantment;
import genandnic.walljump.util.registry.enchantments.WallJumpEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class WallJumpEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(WallJump.MOD_ID, Registry.ENCHANTMENT_REGISTRY);
    public static Enchantment WALLJUMP_ENCHANTMENT = new WallJumpEnchantment();
    public static Enchantment DOUBLEJUMP_ENCHANTMENT = new DoubleJumpEnchantment();
    public static Enchantment SPEEDBOOST_ENCHANTMENT = new SpeedBoostEnchantment();

    public static void registerEnchantments() {
        if (!WallJumpConfig.getConfigEntries().enableDoubleJump && WallJumpConfig.getConfigEntries().enableDoubleJumpEnchantment) {
            ENCHANTMENTS.register(new ResourceLocation("walljump", "doublejump"), () -> DOUBLEJUMP_ENCHANTMENT);
        }
        if (WallJumpConfig.getConfigEntries().sprintSpeedBoost == 0.0 && WallJumpConfig.getConfigEntries().enableSpeedBoostEnchantment) {
            ENCHANTMENTS.register(new ResourceLocation("walljump", "speedboost"), () -> SPEEDBOOST_ENCHANTMENT);
        }
        if (!WallJumpConfig.getConfigEntries().enableWallJump && WallJumpConfig.getConfigEntries().enableWallJumpEnchantment) {
            ENCHANTMENTS.register(new ResourceLocation("walljump", "walljump"), () -> WALLJUMP_ENCHANTMENT);
        }

        ENCHANTMENTS.register();

    }
}
