package team.creative.enhancedvisuals;

import team.creative.creativecore.common.config.CreativeConfig;
import team.creative.creativecore.common.config.CreativeConfigBase;

public class EnhancedVisualsConfig extends CreativeConfigBase {
	
	@CreativeConfig
	public boolean doEffectsInCreative = false;
	
	@Override
	public void configured() {
		
	}
	
}
