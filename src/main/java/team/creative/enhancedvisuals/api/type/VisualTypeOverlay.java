package team.creative.enhancedvisuals.api.type;

import net.minecraft.util.ResourceLocation;
import team.creative.enhancedvisuals.api.VisualCategory;

public class VisualTypeOverlay extends VisualTypeTexture {
	
	public VisualTypeOverlay(ResourceLocation name, int animationSpeed) {
		super(VisualCategory.overlay, name, animationSpeed);
	}
	
	public VisualTypeOverlay(ResourceLocation name) {
		this(name, 0);
	}
}
