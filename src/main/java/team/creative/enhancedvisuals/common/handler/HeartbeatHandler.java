package team.creative.enhancedvisuals.common.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeBlur;
import team.creative.enhancedvisuals.api.type.VisualTypeOverlay;
import team.creative.enhancedvisuals.client.VisualManager;

public class HeartbeatHandler extends VisualHandler {
    
    @CreativeConfig
    public boolean useHealthPercentage = false;
    @CreativeConfig
    public int maxHealth = 6;
    @CreativeConfig
    public float maxHealthPercentage = 0.3F;
    
    @CreativeConfig
    public float heartbeatOverlayIntensity = 0.5F;
    @CreativeConfig
    public int heartbeatOverlayDuration = 20;
    @CreativeConfig
    public float heartbeatBlurIntensity = 50.0F;
    @CreativeConfig
    public int heartbeatBlurDuration = 5;
    
    @CreativeConfig
    public int minHeartbeatLength = 15;
    @CreativeConfig
    public float heartbeatTimeFactor = 100;
    
    @CreativeConfig
    public float heartbeatVolume = 1F;
    
    @CreativeConfig
    public VisualType lowhealth = new VisualTypeOverlay("lowhealth");
    @CreativeConfig
    public VisualType blur = new VisualTypeBlur("blur");
    
    public int effectBufferTicks;
    
    @Override
    public void tick(@Nullable Player player) {
        if (shouldHeartbeatTrigger(player)) {
            if (this.effectBufferTicks <= 0) {
                float intensity = getIntensity(player);
                
                resetBufferTicks(player);
                
                VisualManager.addVisualFadeOut(lowhealth, this, new DecimalCurve(0, Math.min(0.7F, intensity) * heartbeatOverlayIntensity, heartbeatOverlayDuration, 0));
                VisualManager.addVisualFadeOut(blur, this, new DecimalCurve(0, Math.min(0.7F, intensity) * heartbeatBlurIntensity, heartbeatBlurDuration, 0));
                playSound(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "heartbeatout"), heartbeatVolume);
                
            } else if (this.effectBufferTicks == 5) {
                float intensity = getIntensity(player);
                
                playSound(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "heartbeatin"), heartbeatVolume);
                VisualManager.addVisualFadeOut(blur, this, new DecimalCurve(0, Math.min(0.7F, intensity) * heartbeatBlurIntensity, heartbeatBlurDuration, 0));
            }
        }
        this.effectBufferTicks -= 1;
    }
    
    private void resetBufferTicks(@NotNull Player player) {
        float percentHealthLeft = (player.getHealth() / player.getMaxHealth());
        this.effectBufferTicks = (int) (percentHealthLeft * heartbeatTimeFactor + minHeartbeatLength);
    }
    
    private float getIntensity(@NotNull Player player) {
        if (useHealthPercentage)
            return (maxHealthPercentage - (player.getHealth() / player.getMaxHealth()));
        return player.getHealth() / maxHealth;
    }
    
    private boolean shouldHeartbeatTrigger(@Nullable Player player) {
        if (player != null && !Minecraft.getInstance().isPaused() && player.isAlive()) {
            if (useHealthPercentage) {
                float percentageHealth = player.getHealth() / player.getMaxHealth();
                return percentageHealth < maxHealthPercentage;
            } else {
                return player.getHealth() < maxHealth;
            }
        }
        return false;
    }
    
}
