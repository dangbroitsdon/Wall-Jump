package genandnic.walljump.logic;

import genandnic.walljump.WallJump;
import genandnic.walljump.util.WallJumpFallingSound;
import genandnic.walljump.config.WallJumpConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class MiscLogic {
    public static void doStepAssist() {
        LocalPlayer pl = Minecraft.getInstance().player;
        assert pl != null;

        if(!WallJumpConfig.isModUsable(pl.getLevel())) return;

        if(pl.horizontalCollision
                && WallJumpConfig.getConfigEntries().enableStepAssist
                && pl.getDeltaMovement().y() > -0.2
                && pl.getDeltaMovement().y() < 0.01
        ) {
            if(pl.level.noCollision(pl.getBoundingBox().inflate(0.01, -pl.maxUpStep + 0.02, 0.01)))
                pl.setOnGround(true);
        }
    }

    public static void playFallingSound() {
        LocalPlayer pl = Minecraft.getInstance().player;
        assert pl != null;

        if(pl.fallDistance > 1.5 && !pl.isFallFlying()) {
            if(WallJumpConfig.getConfigEntries().playFallingSound && WallJump.FALLING_SOUND.isStopped()) {
                WallJump.FALLING_SOUND = new WallJumpFallingSound(pl);
                Minecraft.getInstance().getSoundManager().play(WallJump.FALLING_SOUND);
            }
        }
    }

    public static void addFallingSound() {
        WallJump.FALLING_SOUND = new WallJumpFallingSound(Minecraft.getInstance().player);
        Minecraft.getInstance().getSoundManager().play(WallJump.FALLING_SOUND);
    }
}
