package genandnic.walljump.registry;

import genandnic.walljump.WallJumpConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class WallJumpConfigRegistry {
    public static ConfigHolder<WallJumpConfig> config;

    public static WallJumpConfig getConfig() {
        return config.getConfig();
    }

    public static void registerConfig() {
        AutoConfig.register(WallJumpConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(WallJumpConfig.class);
    }
}
