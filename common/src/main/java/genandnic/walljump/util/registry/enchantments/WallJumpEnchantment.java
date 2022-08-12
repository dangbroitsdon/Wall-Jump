package genandnic.walljump.util.registry.enchantments;

import genandnic.walljump.util.registry.config.WallJumpConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class WallJumpEnchantment extends Enchantment {

    public WallJumpEnchantment(Enchantment.Rarity weight, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(weight, category, slots);
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
        return 20;
    }

    @Override
    public int getMaxCost(int level) {
        return 60;
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack stack) {

        if(WallJumpConfig.getConfigEntries().enableWallJump) {
            return false;
        }

        return stack.isEnchantable();
    }
}
