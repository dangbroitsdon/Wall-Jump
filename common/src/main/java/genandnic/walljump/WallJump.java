package genandnic.walljump;

import genandnic.walljump.util.registry.EnchantmentsRegistry;
import genandnic.walljump.util.registry.ReceiversRegistry;
import genandnic.walljump.util.registry.config.WallJumpConfig;

import static genandnic.walljump.util.Constants.LOGGER;

public class WallJump {

    public static void init() {
        WallJumpConfig.registerConfig();
        EnchantmentsRegistry.registerEnchantments();
        ReceiversRegistry.registerReceivers();
        LOGGER.info("[Wall-Jump! UNOFFICIAL] is initialized!");
    }
}
