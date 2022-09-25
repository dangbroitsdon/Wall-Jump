package genandnic.walljump.enchantments;

import genandnic.walljump.config.WallJumpConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SpeedBoostEnchantment extends CustomEnchantment {
    public SpeedBoostEnchantment() {
        super(WallJumpConfig.getConfigEntries().speedBoostEnchantmentRarity,
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
        return 3;
    }

    @Override
    public int getMinCost(int level) {
        return 20;
    }

    @Override
    public int getMaxCost(int level) {
        return level * 30;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {  return stack.isEnchantable() || stack.getItem() instanceof ElytraItem;  }

    @Override
    public boolean enableEnchantment() {
        return WallJumpConfig.getConfigEntries().enableWallJumpEnchantment && !WallJumpConfig.getConfigEntries().enableWallJump;
    }

    @Override
    public String getName() {
        return "speedboost";
    }
}
