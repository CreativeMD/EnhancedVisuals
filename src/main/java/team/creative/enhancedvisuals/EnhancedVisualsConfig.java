package team.creative.enhancedvisuals;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.api.ICreativeConfig;

public class EnhancedVisualsConfig implements ICreativeConfig {
    
    @CreativeConfig
    public boolean doEffectsInCreative = false;
    
    @CreativeConfig
    public int waterSubstractFactor = 10;
    
    @CreativeConfig
    public boolean enableDamageDebug = false;
    
    @Override
    public void configured() {
        
    }
    
}
