package team.creative.enhancedvisuals.api.property;

public class ParticleProperties extends VisualProperties {
	
	public int x;
	public int y;
	
	public int width;
	public int height;
	
	public float rotation;
	
	public ParticleProperties(int x, int y, int width, int height) {
		this(x, y, width, height, 0);
	}
	
	public ParticleProperties(int x, int y, int width, int height, float rotation) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
	}
	
	public ParticleProperties(int x, int y, int width, int height, float rotation, float opacity) {
		super(opacity);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
	}
	
	@Override
	public ParticleProperties copy() {
		return new ParticleProperties(x, y, width, height, rotation, opacity);
	}
}
