package team.creative.enhancedvisuals.common.visual;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.animation.IVisualAnimation;
import team.creative.enhancedvisuals.api.property.VisualProperties;
import team.creative.enhancedvisuals.api.type.VisualType;

public class Visual<T extends VisualProperties> {
	
	public final VisualType type;
	
	public final T properties;
	public final T inital;
	
	public final IVisualAnimation animation;
	
	private boolean displayed = false;
	private int tick = 0;
	
	public int variant;
	
	public Visual(VisualType type, T properties, IVisualAnimation animation) {
		this.type = type;
		this.inital = properties;
		this.properties = (T) properties.copy();
		this.animation = animation;
		
		if (!getCategory().supportsProperties(properties))
			throw new RuntimeException("Invalid properties for category");
	}
	
	public boolean displayed() {
		return displayed;
	}
	
	public void addToDisplay() {
		displayed = true;
	}
	
	public void removeFromDisplay() {
		displayed = false;
	}
	
	public VisualCategory getCategory() {
		return type.cat;
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public void render(TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
		type.render(this, manager, screenWidth, screenHeight, partialTicks);
	}
	
	public boolean isVisible() {
		return animation.visible(properties);
	}
	
	public boolean tick() {
		return animation.apply(inital, properties, tick);
	}
	
}
