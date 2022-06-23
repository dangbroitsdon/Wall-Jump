package genandnic.walljump;

import net.fabricmc.api.ModInitializer;

import static genandnic.walljump.WallJumpConfig.registerConfig;
import static genandnic.walljump.registry.WallJumpEnchantmentRegistry.registerEnchantments;
import static genandnic.walljump.registry.WallJumpPacketRegistry.registerPackets;
import static genandnic.walljump.Constants.LOGGER;
public class WallJump implements ModInitializer {
	@Override
	public void onInitialize() {
		// Config Initialization
		registerConfig();

		// Enchantments
		registerEnchantments();

		// Packets
		registerPackets();

		LOGGER.info("[Wall Jump] initialized!");
	}
}