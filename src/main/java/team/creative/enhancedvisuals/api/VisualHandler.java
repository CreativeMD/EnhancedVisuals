package team.creative.enhancedvisuals.api;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.creative.creativecore.Side;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.api.ICreativeConfig;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.client.sound.PositionedSound;

public class VisualHandler implements ICreativeConfig {
    
    @CreativeConfig
    public boolean enabled = true;
    
    @CreativeConfig
    @CreativeConfig.DecimalRange(max = 1, min = 0)
    public float opacity = 1;
    
    @Override
    public void configured(Side side) {}
    
    public void tick(@Nullable Player player) {}
    
    public boolean isEnabled(@Nullable Player player) {
        return enabled && opacity > 0;
    }
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public synchronized void playSound(ResourceLocation location) {
        playSound(location, null, 1.0F);
    }
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public synchronized void playSound(ResourceLocation location, BlockPos pos) {
        playSound(location, pos, 1.0F);
    }
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public synchronized void playSound(ResourceLocation location, float volume) {
        playSound(location, null, volume);
    }
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public synchronized void playSound(ResourceLocation location, BlockPos pos, float volume) {
        if (!EVClient.shouldRender())
            return;
        if (pos != null)
            Minecraft.getInstance().getSoundManager().play(new PositionedSound(location, SoundSource.MASTER, volume, 1, pos));
        else
            Minecraft.getInstance().getSoundManager().play(new PositionedSound(location, SoundSource.MASTER, volume, 1));
    }
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public synchronized void playSoundFadeOut(ResourceLocation location, BlockPos pos, DecimalCurve volume) {
        if (!EVClient.shouldRender())
            return;
        VisualManager.playTicking(location, pos, volume);
    }
    
}
