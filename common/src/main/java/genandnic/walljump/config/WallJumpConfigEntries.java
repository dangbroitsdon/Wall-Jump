package genandnic.walljump.config;

import genandnic.walljump.WallJump;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

@Config(name = WallJump.MOD_ID)
public class WallJumpConfigEntries implements ConfigData {
    @Comment("Allows you to wall cling and wall jump.")
    public boolean enableWallJump = true;

    @Comment("If you disable Wall Jump, it enables the enchantment automagically, this option disables the enchantment.")
    @ConfigEntry.Gui.RequiresRestart
    public boolean enableWallJumpEnchantment = false;

    @Comment("The rarity for Wall-Jump! Enchant, i.e. how often it appears in enchanting table, etc.")
    @ConfigEntry.Gui.RequiresRestart
    public Enchantment.Rarity wallJumpEnchantmentRarity = Enchantment.Rarity.UNCOMMON;

    @Comment("Blacklists blocks inputted; can't Wall Jump off it, format is '(mod name or minecraft).(name)', use underscores as spaces")
    public List<String> blockBlacklist = List.of();

    @Comment("Enables Elytra Wall Cling: Clinging to the Wall with Elytra Deployed.")
    public boolean enableElytraWallCling = false;

    @Comment("Enables Invisible Wall Cling: Clinging to the Wall whilst Invisible.")
    public boolean enableInvisibleWallCling = false;

    @Comment("Classic Wall Cling which allows Crouch, the reason this can't be keybinded is because Fabric doesn't support Multi Mapping.")
    @ConfigEntry.Gui.RequiresRestart
    public boolean enableClassicWallCling = false;

    @Comment("Allows you to climb up without alternating walls.")
    public boolean enableReclinging = false;

    @Comment("Automagically turn the player when wall clinging.")
    public boolean enableAutoRotation = false;

    @Comment("Height of Wall Jumps")
    public double heightWallJump = 0.55;

    @Comment("Ticks wall clinged before starting wall slide.")
    public int delayWallClingSlide = 35;

    @Comment("Exhaustion gained per wall jump.")
    public double exhaustionWallJump = 0.8;

    @Comment("Allows you to jump in mid-air")
    public boolean enableDoubleJump = true;

    @Comment("The rarity for Double Jump Enchant, i.e. how often it appears in enchanting table, etc.")
    @ConfigEntry.Gui.RequiresRestart
    public boolean enableDoubleJumpEnchantment = false;

    @Comment("If you disable Wall Jump, it enables the enchantment automagically, this option disables the enchantment.")
    @ConfigEntry.Gui.RequiresRestart
    public Enchantment.Rarity doubleJumpEnchantmentRarity = Enchantment.Rarity.RARE;

    @Comment("Changes the Jump Count for Double Jump so you can instead have a Triple Jump or even a Quadruple Jump.")
    @ConfigEntry.Gui.RequiresRestart
    public int countDoubleJump = 1;

    @Comment("Exhaustion gained per jump of doublejump.")
    public double exhaustionDoubleJump = 1.2;

    @Comment("Multiplier for Exhaustion gained per jump of doublejump (ONLY IF YOU WANNA BE FREAKY).")
    public double exhaustionDoubleJumpMultiplier = 1;
    @Comment("Play a rush of wind as you fall to your doom.")
    public boolean playFallingSound = true;

    @Comment("Minimum distance for fall damage sound to play; set to 3.0 to disable.")
    public double minFallDistance = 7.5;

    @Comment("Elytra speed boost; set to 0.0 to disable.")
    public double elytraSpeedBoost = 0.0;

    @Comment("Sprint speed boost; set to 0.0 to disable.")
    public double sprintSpeedBoost = 0.0;

    @Comment("If you disable Speed Boost, it enables the enchantment automagically, this option disables the enchantment.")
    @ConfigEntry.Gui.RequiresRestart
    public boolean enableSpeedBoostEnchantment = false;

    @Comment("The rarity for Speedboost Enchant, i.e. how often it appears in enchanting table, etc.")
    @ConfigEntry.Gui.RequiresRestart
    public Enchantment.Rarity speedBoostEnchantmentRarity = Enchantment.Rarity.RARE;

    @Comment("Walk up steps even while airborne, also jump over fences.")
    public boolean enableStepAssist = true;

    @Comment("Alternative way of Wall-Jumping using Spacebar which requires Pressing Space while Clinged. (might be wonky on anything but Space)")
    public boolean spaceWallJumpAlt = true;
}
