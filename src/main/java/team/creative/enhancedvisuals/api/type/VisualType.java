package team.creative.enhancedvisuals.api.type;

import java.util.Collection;
import java.util.LinkedHashMap;

import javax.vecmath.Color3b;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.creativecore.common.config.CreativeConfig;
import team.creative.creativecore.common.config.CreativeConfig.FloatRange;
import team.creative.creativecore.common.config.CreativeConfigBase;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.common.visual.Visual;

public abstract class VisualType extends CreativeConfigBase {
	
	private static LinkedHashMap<ResourceLocation, VisualType> types = new LinkedHashMap<>();
	
	public static Collection<VisualType> getTypes() {
		return types.values();
	}
	
	@CreativeConfig
	public boolean enabled;
	
	@CreativeConfig
	@FloatRange(max = 1, min = 0)
	public float opacity = 1;
	
	public final ResourceLocation name;
	public final VisualCategory cat;
	
	public VisualType(ResourceLocation name, VisualCategory cat) {
		this.name = name;
		this.cat = cat;
		
		types.put(name, this);
	}
	
	public boolean isAffectedByWater() {
		return cat.isAffectedByWater();
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public abstract void loadResources(IResourceManager manager);
	
	@OnlyIn(value = Dist.CLIENT)
	public abstract void render(Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks);
	
	@Override
	public void configured() {
		
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public int getVariantAmount() {
		return 1;
	}
	
	public boolean supportsColor() {
		return false;
	}
	
	public Color3b getColor() {
		return null;
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public void resize(Framebuffer buffer) {
		
	}
	
}
