package genandnic.walljump.client;

import genandnic.walljump.FallingSound;
import genandnic.walljump.WallJumpClient;
import genandnic.walljump.registry.WallJumpConfigRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;


public class MiscLogic {
    public static void stepAssist() {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        if(pl.horizontalCollision
                && WallJumpConfigRegistry.getConfig().stepAssist
                && pl.getVelocity().getY() > -0.2
                && pl.getVelocity().getY() < 0.01
        ) {

            if(pl.world.isSpaceEmpty(pl.getBoundingBox().expand(0.01, -pl.stepHeight + 0.02, 0.01))) {

                pl.setOnGround(true);

            }
        }
    }

    public static void playFallSound() {
        ClientPlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;

        if(pl.fallDistance > 1.5 && !pl.isFallFlying()) {

            if(WallJumpConfigRegistry.getConfig().playFallSound && WallJumpClient.FALLING_SOUND.isDone()) {

                WallJumpClient.FALLING_SOUND = new FallingSound(pl);
                MinecraftClient.getInstance().getSoundManager().play(WallJumpClient.FALLING_SOUND);

            }
        }
    }

    public static void fallSound() {
        WallJumpClient.FALLING_SOUND = new FallingSound(MinecraftClient.getInstance().player);
        MinecraftClient.getInstance().getSoundManager().play(WallJumpClient.FALLING_SOUND);
    }
}
