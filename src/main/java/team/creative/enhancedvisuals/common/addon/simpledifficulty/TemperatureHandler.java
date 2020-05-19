package team.creative.enhancedvisuals.common.addon.simpledifficulty;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.config.api.CreativeConfig;

import net.minecraft.entity.player.EntityPlayer;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeOverlay;
import team.creative.enhancedvisuals.client.VisualManager;
import com.charles445.simpledifficulty.api.SDCapabilities;
import com.charles445.simpledifficulty.api.temperature.ITemperatureCapability;
import com.charles445.simpledifficulty.api.temperature.TemperatureEnum;
import com.charles445.simpledifficulty.api.temperature.TemperatureUtil;

public class TemperatureHandler extends VisualHandler {

	@CreativeConfig
	public double defaultIntensity = 0;
	@CreativeConfig
	public double mediumIntensity = 0.2;
	@CreativeConfig
	public double maxIntensity = 0.4;
	@CreativeConfig
	public double fadeFactor = 0.005;
	
	@CreativeConfig
	public VisualType freeze = new VisualTypeOverlay("freeze");
	public Visual freezeVisual;
	
	@CreativeConfig
	public VisualType heat = new VisualTypeOverlay("heat");
	public Visual heatVisual;
	
	@Override
	public void tick(@Nullable EntityPlayer player) {
		if (freezeVisual == null) {
			freezeVisual = new Visual(freeze, 0);
			freezeVisual.opacity = 0;
			VisualManager.add(freezeVisual);
			
			heatVisual = new Visual(heat, 0);
			heatVisual.opacity = 0;
			VisualManager.add(heatVisual);
		}
		
		double aimedHeat = defaultIntensity;
		double aimedFreeze = defaultIntensity;
		int temp = 12;
		
		if (player != null)
			temp = ((ITemperatureCapability) player.getCapability(SDCapabilities.TEMPERATURE, null)).getTemperatureLevel();
		
		TemperatureEnum enumRange = TemperatureUtil.getTemperatureEnum(temp);
		switch (enumRange) {
		case FREEZING:
			aimedHeat = 0;
			aimedFreeze = maxIntensity;
			break;
		case COLD:
			aimedHeat = 0;
			aimedFreeze = mediumIntensity * getRangeModifier(temp,enumRange,true);
			
			break;
		case NORMAL:
			aimedHeat = defaultIntensity;
			aimedFreeze = defaultIntensity;
			break;
		case HOT:
			aimedHeat = mediumIntensity * getRangeModifier(temp,enumRange,false);
			aimedFreeze = 0;
			break;
		case BURNING:
			aimedHeat = maxIntensity;
			aimedFreeze = 0;
			break;
		}
		
		if (freezeVisual.opacity < aimedFreeze)
			freezeVisual.opacity = (float) Math.min(freezeVisual.opacity + fadeFactor, aimedFreeze);
		else if (freezeVisual.opacity > aimedFreeze)
			freezeVisual.opacity = (float) Math.max(freezeVisual.opacity - fadeFactor, aimedFreeze);
		
		if (heatVisual.opacity < aimedHeat)
			heatVisual.opacity = (float) Math.min(heatVisual.opacity + fadeFactor, aimedHeat);
		else if (heatVisual.opacity > aimedHeat)
			heatVisual.opacity = (float) Math.max(heatVisual.opacity - fadeFactor, aimedHeat);
	}
	
	private static float getRangeModifier(int temp, TemperatureEnum enumRange, boolean cold)
	{
		int lowerBound = enumRange.getLowerBound();
		int upperBound = enumRange.getUpperBound();
		int range = upperBound-lowerBound;
		
		if(cold)
		{
			if(temp==lowerBound) return 1.0f;
			if(temp==upperBound) return 0.0f;
			
			float returnable = (1-((temp-lowerBound)/(range)));
			return returnable;
		}
		else
		{
			if(temp==lowerBound) return 0.0f;
			if(temp==upperBound) return 1.0f;
			
			float returnable = ((temp-lowerBound)/(range));
			return returnable;
		}

	}
}
