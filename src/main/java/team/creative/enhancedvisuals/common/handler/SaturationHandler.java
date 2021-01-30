package team.creative.enhancedvisuals.common.handler;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeSaturation;
import team.creative.enhancedvisuals.client.VisualManager;

public class SaturationHandler extends VisualHandler {
	
	@CreativeConfig
	public double fadeFactor = 0.0005;
	
	@CreativeConfig
	public DecimalCurve saturation = new DecimalCurve(2, 0, 8, 1);
	
	@CreativeConfig
	public VisualType desaturate = new VisualTypeSaturation("desaturate");
	
	public Visual saturationVisual;
	
	@Override
	public void tick(@Nullable PlayerEntity player) {
		if (saturationVisual == null) {
			saturationVisual = new Visual(desaturate, 0);
			saturationVisual.opacity = 1;
			VisualManager.add(saturationVisual);
		}
		
		double aimedSaturation = saturation.maxValue;
		
		if (player != null)
			aimedSaturation = saturation.valueAt(player.getFoodStats().getFoodLevel());
		
		if (saturationVisual.opacity < aimedSaturation)
			saturationVisual.opacity = (float) Math.min(saturationVisual.opacity + fadeFactor, aimedSaturation);
		else if (saturationVisual.opacity > aimedSaturation)
			saturationVisual.opacity = (float) Math.max(saturationVisual.opacity - fadeFactor, aimedSaturation);
	}
	
}
