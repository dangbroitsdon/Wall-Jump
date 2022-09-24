package genandnic.walljump.util.registry.config;

import genandnic.walljump.util.registry.WallJumpServerReceivers;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class WallJumpConfig {
    public static ConfigHolder<WallJumpConfigEntries> config;

    public static WallJumpConfigEntries getConfigEntries() {
        return config.getConfig();
    }

    public static void registerConfig() {
        AutoConfig.register(WallJumpConfigEntries.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(WallJumpConfigEntries.class);
    }

    public static boolean isModUsable(Level level) {
        if(level == null) return false;

        if(level.isClientSide) {
            if(Minecraft.getInstance().hasSingleplayerServer()) {
                return true;
            } else {
                return WallJumpServerReceivers.serverConfigSynced;
            }
        } else {
            return true;
        }
    }
}
