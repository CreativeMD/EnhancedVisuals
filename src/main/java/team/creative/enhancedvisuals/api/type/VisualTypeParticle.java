package team.creative.enhancedvisuals.api.type;

import net.minecraft.util.ResourceLocation;
import team.creative.creativecore.common.config.CreativeConfig;
import team.creative.enhancedvisuals.api.VisualCategory;

public class VisualTypeParticle extends VisualTypeTexture {
	
	public VisualTypeParticle(ResourceLocation name, int animationSpeed) {
		super(VisualCategory.particle, name, animationSpeed);
	}
	
	public VisualTypeParticle(ResourceLocation name) {
		this(name, 0);
	}
	
	@CreativeConfig
	public float scaleFactor = 1F;
	
}
