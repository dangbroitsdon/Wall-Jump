package genandnic.walljump.enchantments;

import genandnic.walljump.config.WallJumpConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.jetbrains.annotations.NotNull;

public class DoubleJumpEnchantment extends CustomEnchantment {

    public DoubleJumpEnchantment() {
        super(WallJumpConfig.getConfigEntries().doubleJumpEnchantmentRarity,
                EnchantmentCategory.ARMOR_FEET,
                new EquipmentSlot[] {
                        EquipmentSlot.FEET
                });
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int getMinCost(int level) {
        return level * 20;
    }

    @Override
    public int getMaxCost(int level) {
        return level * 30;
    }

    @Override
    public boolean checkCompatibility(@NotNull Enchantment enchantment) {
        if(enchantment instanceof ProtectionEnchantment protection) {
            return protection.type != ProtectionEnchantment.Type.FALL;
        }

        return this != enchantment;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {  return stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.FEET;   }

    @Override
    public boolean enableEnchantment() {
        return WallJumpConfig.getConfigEntries().enableDoubleJumpEnchantment;
    }

    @Override
    public String getName() {
        return "doublejump";
    }
}