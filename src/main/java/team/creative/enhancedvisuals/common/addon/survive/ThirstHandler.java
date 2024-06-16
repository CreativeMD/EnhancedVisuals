package team.creative.enhancedvisuals.common.addon.survive;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.shaders.Uniform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.creativecore.common.util.mc.PlayerUtils;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeShader;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.mixin.PostChainAccessor;

public class ThirstHandler extends VisualHandler {
    
    @CreativeConfig
    public double defaultIntensity = 0F;
    
    @CreativeConfig
    public double maxIntensity = 5;
    
    @CreativeConfig
    public double fadeFactor = 0.05F;
    
    @CreativeConfig
    public IntMinMax thirstLevel = new IntMinMax(2, 8);
    
    @CreativeConfig
    public VisualType focus = new VisualTypeShader("focus", ResourceLocation.withDefaultNamespace("shaders/post/blobs2.json")) {
        
        @Override
        @Environment(EnvType.CLIENT)
        @OnlyIn(Dist.CLIENT)
        public void changeProperties(float intensity) {
            for (PostPass pass : ((PostChainAccessor) postChain).getPasses()) {
                Uniform shaderuniform = pass.getEffect().getUniform("Radius");
                
                if (shaderuniform != null)
                    shaderuniform.set(intensity);
            }
        }
    };
    
    public Visual focusVisual;
    
    public double getThirst(Player player) {
        return PlayerUtils.getPersistentData(player).getCompound("survive:PlayerData").getCompound("WaterStats").getInt("waterLevel");
    }
    
    @Override
    public void tick(@Nullable Player player) {
        if (focusVisual == null) {
            focusVisual = new Visual(focus, this, 0);
            VisualManager.add(focusVisual);
        }
        
        double aimedSaturation = defaultIntensity;
        if (player != null && player.isAlive()) {
            double thirst = getThirst(player);
            if (thirst <= thirstLevel.max) {
                double leftFoodInSpan = thirst - thirstLevel.min;
                double spanLength = thirstLevel.spanLength();
                aimedSaturation = (1 - (leftFoodInSpan / spanLength)) * maxIntensity;
            }
            
            if (focusVisual.getOpacityInternal() < aimedSaturation)
                focusVisual.setOpacityInternal((float) Math.min(focusVisual.getOpacityInternal() + fadeFactor, aimedSaturation));
            else if (focusVisual.getOpacityInternal() > aimedSaturation)
                focusVisual.setOpacityInternal((float) Math.max(focusVisual.getOpacityInternal() - fadeFactor, aimedSaturation));
        } else
            focusVisual.setOpacityInternal((float) defaultIntensity);
    }
    
}
