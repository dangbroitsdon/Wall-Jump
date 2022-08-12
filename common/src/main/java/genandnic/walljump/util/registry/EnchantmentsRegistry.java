package genandnic.walljump.util.registry;

import genandnic.walljump.util.registry.config.WallJumpConfig;
import genandnic.walljump.util.registry.enchantments.DoubleJumpEnchantment;
import genandnic.walljump.util.registry.enchantments.SpeedBoostEnchantment;
import genandnic.walljump.util.registry.enchantments.WallJumpEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentsRegistry {
    public static Enchantment WALLJUMP_ENCHANTMENT;
    public static Enchantment DOUBLEJUMP_ENCHANTMENT;
    public static Enchantment SPEEDBOOST_ENCHANTMENT;
    
    public static void registerEnchantments() {
        if (!WallJumpConfig.getConfigEntries().enableDoubleJump && WallJumpConfig.getConfigEntries().enableDoubleJumpEnchantment) {
            DOUBLEJUMP_ENCHANTMENT = Registry.register(
                    Registry.ENCHANTMENT,
                    new ResourceLocation("walljump", "doublejump"),
                    new DoubleJumpEnchantment(
                            Enchantment.Rarity.RARE,
                            EnchantmentCategory.ARMOR_FEET,
                            new EquipmentSlot[] {
                                    EquipmentSlot.FEET
                            }
                    )
            );
        }
        if (WallJumpConfig.getConfigEntries().sprintSpeedBoost == 0.0 && WallJumpConfig.getConfigEntries().enableSpeedBoostEnchantment) {
            SPEEDBOOST_ENCHANTMENT = Registry.register(
                    Registry.ENCHANTMENT,
                    new ResourceLocation("walljump", "speedboost"),
                    new SpeedBoostEnchantment(
                            Enchantment.Rarity.RARE,
                            EnchantmentCategory.ARMOR_FEET,
                            new EquipmentSlot[] {
                                    EquipmentSlot.FEET
                            }
                    )
            );
        }
        if (!WallJumpConfig.getConfigEntries().enableWallJump && WallJumpConfig.getConfigEntries().enableWallJumpEnchantment) {
            WALLJUMP_ENCHANTMENT = Registry.register(
                    Registry.ENCHANTMENT,
                    new ResourceLocation("walljump", "walljump"),
                    new WallJumpEnchantment(
                            Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET,
                            new EquipmentSlot[] {
                                    EquipmentSlot.FEET
                            }
                    )
            );
        }

    }
}
