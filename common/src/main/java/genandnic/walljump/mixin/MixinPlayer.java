package genandnic.walljump.mixin;

import genandnic.walljump.config.WallJumpConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Player.class)
public abstract class MixinPlayer {
    @Shadow
    public abstract void playSound(SoundEvent soundEvent, float f, float g);

    @ModifyArg(method = "causeFallDamage", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.causeFallDamage (FF)Z"), index = 0)
    private float wju$adjustFallDistance(float g) {
        if (g > 3 && g <= WallJumpConfig.getConfigEntries().minFallDistance) {
            this.playSound(SoundEvents.GENERIC_SMALL_FALL, 0.5F, 1.0F);
            return 3.0F;
        }
        return g;
    }
}
