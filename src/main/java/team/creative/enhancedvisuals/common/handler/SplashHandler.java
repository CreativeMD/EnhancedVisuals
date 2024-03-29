package team.creative.enhancedvisuals.common.handler;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.DecimalMinMax;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeBlur;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.common.event.EVEvents;

public class SplashHandler extends VisualHandler {
    
    @CreativeConfig
    public IntMinMax duration = new IntMinMax(10, 10);
    
    @CreativeConfig
    public DecimalMinMax intensity = new DecimalMinMax(5, 10);
    
    @CreativeConfig
    public VisualType blur = new VisualTypeBlur("blur");
    
    public boolean wasInWater = false;
    
    @Override
    public void tick(@Nullable Player player) {
        if (player != null) {
            boolean isInWater = EVEvents.areEyesInWater(player);
            if (isInWater != wasInWater)
                VisualManager.addVisualFadeOut(blur, this, new DecimalCurve(VisualManager.RANDOM, duration, intensity));
            wasInWater = isInWater;
            
        }
    }
    
}
