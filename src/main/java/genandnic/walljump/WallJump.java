package genandnic.walljump;

import net.fabricmc.api.ModInitializer;
import genandnic.walljump.registry.WallJumpEnchantmentRegistry;
import genandnic.walljump.registry.WallJumpPacketRegistry;

import static genandnic.walljump.Constants.LOGGER;

public class WallJump implements ModInitializer {
	@Override
	public void onInitialize() {
		// Config Initialization
		WallJumpConfig.registerConfig();

		// Enchantments
		WallJumpEnchantmentRegistry.registerEnchantments();

		// Packets
		WallJumpPacketRegistry.registerPackets();

		LOGGER.info("[Wall Jump] initialized!");
	}
}