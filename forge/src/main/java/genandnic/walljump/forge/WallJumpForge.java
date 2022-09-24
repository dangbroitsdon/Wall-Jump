package genandnic.walljump.forge;

import dev.architectury.platform.forge.EventBuses;
import genandnic.walljump.WallJump;
import genandnic.walljump.util.registry.config.WallJumpConfigEntries;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler;

@Mod(WallJump.MOD_ID)
public class WallJumpForge {
    public WallJumpForge() {
        EventBuses.registerModEventBus(WallJump.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        EventBuses.getModEventBus(WallJump.MOD_ID).get().addListener(this::onClientSetup);
        registerConfigScreen();
        WallJump.initBase();
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        WallJump.initClient();
    }

    private void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory(
                        (client, parent) -> AutoConfig.getConfigScreen(WallJumpConfigEntries.class, parent).get()));
    }
}
