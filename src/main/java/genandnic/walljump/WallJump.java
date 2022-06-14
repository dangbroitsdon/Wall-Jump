package genandnic.walljump;

import genandnic.walljump.registry.WallJumpEnchantmentRegistry;
import genandnic.walljump.registry.WallJumpPacketRegistry;
import net.fabricmc.api.ModInitializer;

public class WallJump implements ModInitializer {
	@Override
	public void onInitialize() {
		// Config Initialization
		WallJumpConfig.registerConfig();

		// Enchantments
		WallJumpEnchantmentRegistry.registerEnchantments();

		// Packets
		WallJumpPacketRegistry.registerPackets();

		Constants.LOGGER.info("[Wall Jump] initialized!");
	}
}