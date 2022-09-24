package genandnic.walljump.util.registry.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

public class DoubleJumpEnchantment extends Enchantment {

    public DoubleJumpEnchantment() {
        super(Enchantment.Rarity.RARE,
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
        return level * 60;
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        if(enchantment instanceof ProtectionEnchantment) {
            ProtectionEnchantment protection = (ProtectionEnchantment) enchantment;
            return protection.type != ProtectionEnchantment.Type.FALL;
        }

        return this != enchantment;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {  return stack.isEnchantable();   }
}
