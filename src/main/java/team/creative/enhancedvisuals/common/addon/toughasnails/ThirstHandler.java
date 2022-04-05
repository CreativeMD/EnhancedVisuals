package team.creative.enhancedvisuals.common.addon.toughasnails;

import javax.annotation.Nullable;

import com.mojang.blaze3d.shaders.Uniform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeShader;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.mixin.PostChainAccessor;
import toughasnails.api.thirst.ThirstHelper;

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
    public VisualType focus = new VisualTypeShader("focus", new ResourceLocation("shaders/post/blobs2.json")) {
        
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
    
    @Override
    public void tick(@Nullable Player player) {
        if (focusVisual == null) {
            focusVisual = new Visual(focus, this, 0);
            VisualManager.add(focusVisual);
        }
        
        double aimedSaturation = defaultIntensity;
        if (player != null && player.isAlive() && ThirstHelper.isThirstEnabled()) {
            int thirst = ThirstHelper.getThirst(player).getThirst();
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
