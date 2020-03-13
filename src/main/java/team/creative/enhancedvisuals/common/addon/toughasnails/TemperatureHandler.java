package team.creative.enhancedvisuals.common.addon.toughasnails;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.premade.curve.DecimalCurve;

import net.minecraft.entity.player.EntityPlayer;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeOverlay;
import team.creative.enhancedvisuals.client.VisualManager;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale.TemperatureRange;

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
	
	private static Temperature defaultTemperature = new Temperature(12);
	
	@Override
	public void tick(@Nullable EntityPlayer player) {
		if (freezeVisual == null) {
			freezeVisual = new Visual(freeze, new DecimalCurve(0, 1, 1, 1), 0);
			VisualManager.add(freezeVisual);
			
			heatVisual = new Visual(heat, new DecimalCurve(0, 1, 1, 1), 0);
			VisualManager.add(heatVisual);
		}
		
		if (freezeVisual != null && heatVisual != null) {
			double aimedHeat = defaultIntensity;
			double aimedFreeze = defaultIntensity;
			Temperature temp = null;
			
			if (player != null)
				temp = ((ITemperature) player.getCapability(TANCapabilities.TEMPERATURE, null)).getTemperature();
			
			double fadeFactor = this.fadeFactor;
			if (temp == null)
				temp = defaultTemperature;
			
			TemperatureRange range = temp.getRange();
			switch (range) {
			case ICY:
				aimedHeat = 0;
				aimedFreeze = maxIntensity;
				break;
			case COOL:
				aimedHeat = 0;
				aimedFreeze = mediumIntensity * temp.getRangeDelta(true);
				
				break;
			case MILD:
				aimedHeat = defaultIntensity;
				aimedFreeze = defaultIntensity;
				break;
			case WARM:
				aimedHeat = mediumIntensity * temp.getRangeDelta(false);
				aimedFreeze = 0;
				break;
			case HOT:
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
	}
	
}
