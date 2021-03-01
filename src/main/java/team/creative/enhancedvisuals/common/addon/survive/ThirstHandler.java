package team.creative.enhancedvisuals.common.addon.survive;

import javax.annotation.Nullable;

import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeShader;
import team.creative.enhancedvisuals.client.VisualManager;

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
        public void changeProperties(float intensity) {
            for (Shader mcShader : shaderGroup.getShaders()) {
                ShaderDefault shaderuniform = mcShader.getShaderManager().getShaderUniform("Radius");
                
                if (shaderuniform != null)
                    shaderuniform.set(intensity);
            }
        }
    };
    public Visual focusVisual;
    
    public double getThirst(PlayerEntity player) {
        return player.getPersistentData().getCompound("survive:PlayerData").getCompound("WaterStats").getInt("waterLevel");
    }
    
    @Override
    public void tick(@Nullable PlayerEntity player) {
        if (focusVisual == null) {
            focusVisual = new Visual(focus, 0);
            VisualManager.add(focusVisual);
        }
        
        double aimedSaturation = defaultIntensity;
        if (player != null) {
            double thirst = getThirst(player);
            if (thirst <= thirstLevel.max) {
                double leftFoodInSpan = thirst - thirstLevel.min;
                double spanLength = thirstLevel.spanLength();
                aimedSaturation = (1 - (leftFoodInSpan / spanLength)) * maxIntensity;
            }
            
            if (focusVisual.opacity < aimedSaturation)
                focusVisual.opacity = (float) Math.min(focusVisual.opacity + fadeFactor, aimedSaturation);
            else if (focusVisual.opacity > aimedSaturation)
                focusVisual.opacity = (float) Math.max(focusVisual.opacity - fadeFactor, aimedSaturation);
        }
    }
    
}
