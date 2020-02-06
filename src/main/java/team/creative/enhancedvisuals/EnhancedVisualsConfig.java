package team.creative.enhancedvisuals;

import team.creative.creativecore.common.config.CreativeConfig;
import team.creative.creativecore.common.config.ICreativeConfig;

public class EnhancedVisualsConfig implements ICreativeConfig {
	
	@CreativeConfig
	public boolean doEffectsInCreative = false;
	
	@CreativeConfig
	public int waterSubstractFactor = 10;
	
	@Override
	public void configured() {
		
	}
	
}
