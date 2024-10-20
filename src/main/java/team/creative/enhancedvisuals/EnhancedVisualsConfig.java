package team.creative.enhancedvisuals;

import team.creative.creativecore.Side;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.api.ICreativeConfig;

public class EnhancedVisualsConfig implements ICreativeConfig {
    
    @CreativeConfig
    public boolean doEffectsInCreative = false;
    
    @CreativeConfig
    public int waterSubstractFactor = 10;
    
    @CreativeConfig
    public boolean enableDamageDebug = false;
    
    @CreativeConfig
    public boolean fixBlurShader = true;
    
    @Override
    public void configured(Side side) {}
    
}
