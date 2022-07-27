package genandnic.walljump;

import genandnic.walljump.registry.WallJumpConfigRegistry;
import genandnic.walljump.registry.WallJumpEnchantmentRegistry;
import genandnic.walljump.registry.WallJumpPacketRegistry;
import net.fabricmc.api.ModInitializer;

import static genandnic.walljump.Constants.LOGGER;

public class WallJump implements ModInitializer {
	@Override
	public void onInitialize() {
		// Config Initialization
		WallJumpConfigRegistry.registerConfig();

		// Enchantments
		WallJumpEnchantmentRegistry.registerEnchantments();

		// Packets
		WallJumpPacketRegistry.registerPackets();

		LOGGER.info("[Wall Jump] initialized!");
	}
}