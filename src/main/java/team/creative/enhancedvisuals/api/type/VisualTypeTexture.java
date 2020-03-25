package team.creative.enhancedvisuals.api.type;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;

public abstract class VisualTypeTexture extends VisualType {
	
	@CreativeConfig
	public float scale = 1F;
	
	@CreativeConfig
	public int animationSpeed;
	public String domain;
	
	public VisualTypeTexture(VisualCategory category, String name, String domain, int animationSpeed) {
		super(name, category);
		this.domain = domain;
		this.animationSpeed = animationSpeed;
	}
	
	public VisualTypeTexture(VisualCategory category, String name, String domain, int animationSpeed, float scale) {
		super(name, category);
		this.domain = domain;
		this.animationSpeed = animationSpeed;
		this.scale = scale;
	}
	
	public VisualTypeTexture(VisualCategory category, String name, int animationSpeed, float scale) {
		this(category, name, null, animationSpeed, scale);
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public ResourceLocation[] resources;
	@OnlyIn(value = Dist.CLIENT)
	public Dimension dimension;
	
	@Override
	@OnlyIn(value = Dist.CLIENT)
	public void loadResources(IResourceManager manager) {
		String baseLocation = "visuals/" + cat.name() + "/" + name + "/" + name;
		
		ArrayList<ResourceLocation> locations = new ArrayList<>();
		int i = 0;
		ResourceLocation location = null;
		IResource resource = null;
		try {
			while ((resource = manager.getResource((location = new ResourceLocation(domain == null ? EnhancedVisuals.MODID : domain, baseLocation + i + ".png")))) != null) {
				if (i == 0) {
					BufferedImage image = ImageIO.read(resource.getInputStream());
					dimension = new Dimension(image.getWidth(), image.getHeight());
				}
				locations.add(location);
				i++;
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		resources = locations.toArray(new ResourceLocation[0]);
		if (resources.length == 0) {
			EnhancedVisuals.LOGGER.warn("Could not find any resources for '" + name + "'!");
		}
	}
	
	@Override
	@OnlyIn(value = Dist.CLIENT)
	public boolean supportsColor() {
		return true;
	}
	
	@Override
	@OnlyIn(value = Dist.CLIENT)
	public int getVariantAmount() {
		return resources.length;
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public ResourceLocation getResource(Visual visual) {
		if (animationSpeed > 0) {
			long time = Math.abs(System.nanoTime() / 3000000 / animationSpeed);
			return resources[(int) (time % resources.length)];
		}
		return resources[visual.variant];
	}
	
	@Override
	@OnlyIn(value = Dist.CLIENT)
	public void render(Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
		manager.bindTexture(getResource(visual));
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		
		int red = visual.color != null ? visual.color.getRed() : 255;
		int green = visual.color != null ? visual.color.getGreen() : 255;
		int blue = visual.color != null ? visual.color.getBlue() : 255;
		double z = -90;
		
		int width = visual.getWidth(screenWidth);
		int height = visual.getHeight(screenHeight);
		
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		renderer.func_225582_a_(0.0D, height, z).func_225583_a_(0.0F, 1.0F).func_225586_a_(red, green, blue, (int) (visual.opacity * 255)).endVertex();
		renderer.func_225582_a_(width, height, z).func_225583_a_(1.0F, 1.0F).func_225586_a_(red, green, blue, (int) (visual.opacity * 255)).endVertex();
		renderer.func_225582_a_(width, 0.0D, z).func_225583_a_(1.0F, 0.0F).func_225586_a_(red, green, blue, (int) (visual.opacity * 255)).endVertex();
		renderer.func_225582_a_(0.0D, 0.0D, z).func_225583_a_(0.0F, 0.0F).func_225586_a_(red, green, blue, (int) (visual.opacity * 255)).endVertex();
		tessellator.draw();
	}
	
	@Override
	public int getWidth(int screenWidth) {
		return (int) (dimension.width * scale);
	}
	
	@Override
	public int getHeight(int screenHeight) {
		return (int) (dimension.height * scale);
	}
}
