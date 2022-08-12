package genandnic.walljump.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import genandnic.walljump.util.registry.config.WallJumpConfigEntries;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@SuppressWarnings({"unused", "overrides", "deprecated"})
@Environment(EnvType.CLIENT)
public class WallJumpModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(WallJumpConfigEntries.class, parent).get();
    }
}

