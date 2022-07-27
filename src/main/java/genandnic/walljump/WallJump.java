package genandnic.walljump;

import genandnic.walljump.registry.WallJumpConfigRegistry;
import net.fabricmc.api.ModInitializer;
import genandnic.walljump.registry.WallJumpEnchantmentRegistry;
import genandnic.walljump.registry.WallJumpPacketRegistry;

import static genandnic.walljump.Constants.LOGGER;

public class WallJump implements ModInitializer {
	@Override
	public void onInitialize() {
		// Config Registry
		WallJumpConfigRegistry.registerConfig();

		// Enchantments Registry
		WallJumpEnchantmentRegistry.registerEnchantments();

		// Packets Registry
		WallJumpPacketRegistry.registerPackets();

		LOGGER.info("[Wall-Jump! UNOFFICIAL [FABRIC]] initialized!");
	}
}