package genandnic.walljump;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import static genandnic.walljump.registry.WallJumpKeyBindingRegistry.registerClientEndTickEvents;
import static genandnic.walljump.registry.WallJumpKeyBindingRegistry.registerKeyBinding;
import static genandnic.walljump.Constants.LOGGER;

@Environment(EnvType.CLIENT)
public class WallJumpClient implements ClientModInitializer {
	public static FallingSound FALLING_SOUND;

	@Override
	public void onInitializeClient() {
		//KeyBinding Setup
		registerKeyBinding();
		registerClientEndTickEvents();

		//Fall Sound lol
		FALLING_SOUND = new FallingSound(MinecraftClient.getInstance().player);

		LOGGER.info("[Wall Jump Client] initialized!");
	}
}
