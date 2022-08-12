package genandnic.walljump.util.registry.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SpeedBoostEnchantment extends Enchantment {
    public SpeedBoostEnchantment(Enchantment.Rarity weight, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(weight, category, slots);
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
        return level * 15;
    }

    @Override
    public int getMaxCost(int level) {
        return level * 60;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {  return stack.isEnchantable() || stack.getItem() instanceof ElytraItem;  }
}
