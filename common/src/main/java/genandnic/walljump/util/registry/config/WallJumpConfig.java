package genandnic.walljump.util.registry.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class WallJumpConfig {
    public static ConfigHolder<WallJumpConfigEntries> config;

    public static WallJumpConfigEntries getConfigEntries() {
        return config.getConfig();
    }

    public static void registerConfig() {
        AutoConfig.register(WallJumpConfigEntries.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(WallJumpConfigEntries.class);
    }
}
