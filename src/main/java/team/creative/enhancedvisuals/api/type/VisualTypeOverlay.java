package team.creative.enhancedvisuals.api.type;

import team.creative.enhancedvisuals.api.VisualCategory;

public class VisualTypeOverlay extends VisualTypeTexture {
	
	public VisualTypeOverlay(String name, int animationSpeed) {
		super(VisualCategory.overlay, name, animationSpeed, 1F);
	}
	
	public VisualTypeOverlay(String name) {
		this(name, 0);
	}
}
