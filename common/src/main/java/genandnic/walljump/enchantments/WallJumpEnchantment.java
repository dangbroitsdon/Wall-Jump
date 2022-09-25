package genandnic.walljump.enchantments;

import genandnic.walljump.config.WallJumpConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class WallJumpEnchantment extends CustomEnchantment {

    public WallJumpEnchantment() {
        super(WallJumpConfig.getConfigEntries().wallJumpEnchantmentRarity,
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
        return 1;
    }

    @Override
    public int getMinCost(int level) {
        return 15;
    }

    @Override
    public int getMaxCost(int level) {
        return 15;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack stack) {

        if(WallJumpConfig.getConfigEntries().enableWallJump) {
            return false;
        }

        return stack.isEnchantable();
    }

    @Override
    public boolean enableEnchantment() {
        return WallJumpConfig.getConfigEntries().enableWallJumpEnchantment && !WallJumpConfig.getConfigEntries().enableWallJump;
    }

    @Override
    public String getName() {
        return "walljump";
    }
}
