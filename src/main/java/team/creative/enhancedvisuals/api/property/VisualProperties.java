package team.creative.enhancedvisuals.api.property;

public class VisualProperties {
	
	public float opacity;
	
	public VisualProperties() {
		this(1);
	}
	
	public VisualProperties(float opacity) {
		this.opacity = opacity;
	}
	
	public VisualProperties copy() {
		return new VisualProperties(opacity);
	}
	
}
