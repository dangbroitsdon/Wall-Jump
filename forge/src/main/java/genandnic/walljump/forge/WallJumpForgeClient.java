package genandnic.walljump.forge;

import genandnic.walljump.util.Constants;
import genandnic.walljump.util.registry.KeyMappingsRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class WallJumpForgeClient {
    @SubscribeEvent
    public static void registerKeyMapping(final RegisterKeyMappingsEvent event) {
       event.register(KeyMappingsRegistry.KEY_WALLJUMP);
    }
}
