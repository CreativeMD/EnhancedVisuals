package team.creative.enhancedvisuals.common.addon.survive;

import org.jetbrains.annotations.Nullable;

import com.stereowalker.survive.api.needs.PlayerNeeds;

import net.minecraft.world.entity.player.Player;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeFocus;

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
    public VisualType focus = new VisualTypeFocus("focus");
    
    public Visual focusVisual;
    
    public double getThirst(Player player) {
        return PlayerNeeds.api().getWater(player).getWaterLevel();
    }
    
    @Override
    public void tick(@Nullable Player player) {
        if (focusVisual == null) {
            focusVisual = new Visual(focus, this, 0);
            add(focusVisual);
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
