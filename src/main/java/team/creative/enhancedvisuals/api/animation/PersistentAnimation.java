package team.creative.enhancedvisuals.api.animation;

import team.creative.enhancedvisuals.api.property.VisualProperties;

public class PersistentAnimation implements IVisualAnimation {
	
	public float opacity;
	
	public PersistentAnimation(float opacity) {
		this.opacity = opacity;
	}
	
	@Override
	public boolean apply(VisualProperties inital, VisualProperties current, int tick) {
		current.opacity = opacity;
		return true;
	}
	
}
