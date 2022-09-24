package genandnic.walljump.forge;

import genandnic.walljump.WallJump;
import genandnic.walljump.util.registry.config.WallJumpConfigEntries;
import me.shedaniel.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> (Screen) AutoConfig.getConfigScreen(WallJumpConfigEntries.class, screen)
        );
    }
}
