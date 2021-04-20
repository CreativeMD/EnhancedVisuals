package team.creative.enhancedvisuals.common.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
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
    public float heartbeatIntensity = 50.0F;
    @CreativeConfig
    public int heartbeatDuration = 5;
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
    public void tick(@Nullable PlayerEntity player) {
        if (shouldHeartbeatTrigger(player)) {
            if (this.effectBufferTicks <= 0) {
                float intensity = getIntensity(player);
                
                resetBufferTicks(player);
                
                VisualManager.addVisualFadeOut(lowhealth, this, new DecimalCurve(0, Math.min(0.7F, intensity), effectBufferTicks, 0));
                VisualManager.addVisualFadeOut(blur, this, new DecimalCurve(0, Math.min(0.7F, intensity) * heartbeatIntensity, heartbeatDuration, 0));
                playSound(new ResourceLocation(EnhancedVisuals.MODID, "heartbeatout"), heartbeatVolume);
                
            } else if (this.effectBufferTicks == 5) {
                float intensity = getIntensity(player);
                
                playSound(new ResourceLocation(EnhancedVisuals.MODID, "heartbeatin"), heartbeatVolume);
                VisualManager.addVisualFadeOut(blur, this, new DecimalCurve(0, Math.min(0.7F, intensity) * heartbeatIntensity, heartbeatDuration, 0));
            }
        }
        this.effectBufferTicks -= 1;
    }
    
    private void resetBufferTicks(@Nonnull PlayerEntity player) {
        float percentHealthLeft = (player.getHealth() / player.getMaxHealth());
        this.effectBufferTicks = (int) (percentHealthLeft * heartbeatTimeFactor + minHeartbeatLength);
    }
    
    private float getIntensity(@Nonnull PlayerEntity player) {
        float percentHealthLeft = (player.getHealth() / player.getMaxHealth());
        if (useHealthPercentage) {
            return (maxHealthPercentage - percentHealthLeft) * 2.0F;
        } else {
            return ((maxHealth - player.getHealth()) / player.getMaxHealth()) * 2.0F;
        }
    }
    
    private boolean shouldHeartbeatTrigger(@Nullable PlayerEntity player) {
        if (player != null && !Minecraft.getInstance().isPaused()) {
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
