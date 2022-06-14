package genandnic.walljump;

import genandnic.walljump.registry.WallJumpKeyBindingRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;

@Environment(EnvType.CLIENT)
public class WallJumpClient implements ClientModInitializer {


	public static FallingSound FALLING_SOUND;

	@Override
	public void onInitializeClient() {
		//KeyBinding Setup
		WallJumpKeyBindingRegistry.registerKeyBinding();
		WallJumpKeyBindingRegistry.registerClientEndTickEvent();

		//Fall Sound lol
		FALLING_SOUND = new FallingSound(MinecraftClient.getInstance().player);

		Constants.LOGGER.info("[Wall Jump Client] initialized!");
	}
}
