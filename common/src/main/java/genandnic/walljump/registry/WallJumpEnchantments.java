package genandnic.walljump.registry;

import dev.architectury.registry.registries.DeferredRegister;
import genandnic.walljump.WallJump;
import genandnic.walljump.enchantments.SpeedBoostEnchantment;
import genandnic.walljump.enchantments.WallJumpEnchantment;
import genandnic.walljump.enchantments.CustomEnchantment;
import genandnic.walljump.enchantments.DoubleJumpEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.HashSet;
import java.util.Set;

public class WallJumpEnchantments {
    private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(WallJump.MOD_ID, Registry.ENCHANTMENT_REGISTRY);
    private static final Set<CustomEnchantment> enchantmentsToRegister = new HashSet<>();

    public static CustomEnchantment WALLJUMP_ENCHANTMENT = addEnchantmentToRegistrySet(new WallJumpEnchantment());
    public static CustomEnchantment DOUBLEJUMP_ENCHANTMENT = addEnchantmentToRegistrySet(new DoubleJumpEnchantment());
    public static CustomEnchantment SPEEDBOOST_ENCHANTMENT = addEnchantmentToRegistrySet(new SpeedBoostEnchantment());

    private static <T extends CustomEnchantment> T addEnchantmentToRegistrySet(T enchantment) {
        if (enchantment.enableEnchantment()) {
            enchantmentsToRegister.add(enchantment);
        }

        return enchantment;
    }

    public static void registerEnchantments() {
        for (CustomEnchantment enchantment : enchantmentsToRegister) {
            ENCHANTMENTS.register(enchantment.getName(), () -> enchantment);
        }

        ENCHANTMENTS.register();
    }
}