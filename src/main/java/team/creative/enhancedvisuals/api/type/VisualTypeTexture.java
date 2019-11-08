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
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.common.visual.Visual;

public abstract class VisualTypeTexture extends VisualType {
	
	public int animationSpeed;
	
	public VisualTypeTexture(VisualCategory category, ResourceLocation name, int animationSpeed) {
		super(name, category);
		this.animationSpeed = animationSpeed;
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
			while ((resource = manager.getResource((location = new ResourceLocation(EnhancedVisuals.MODID, baseLocation + i + ".png")))) != null) {
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
		
		/* float red = visual.getColor().getRed() / 255.0F;
		 * float green = visual.getColor().getGreen() / 255.0F;
		 * float blue = visual.getColor().getBlue() / 255.0F; */
		float red = 1;
		float green = 1;
		float blue = 1;
		double z = -90;
		
		int width = cat.getWidth(visual.properties, screenWidth);
		int height = cat.getHeight(visual.properties, screenHeight);
		
		cat.translate(visual.properties);
		
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		renderer.pos(0.0D, height, z).tex(0.0D, 1.0D).color(red, green, blue, visual.properties.opacity).endVertex();
		renderer.pos(width, height, z).tex(1.0D, 1.0D).color(red, green, blue, visual.properties.opacity).endVertex();
		renderer.pos(width, 0.0D, z).tex(1.0D, 0.0D).color(red, green, blue, visual.properties.opacity).endVertex();
		renderer.pos(0.0D, 0.0D, z).tex(0.0D, 0.0D).color(red, green, blue, visual.properties.opacity).endVertex();
		tessellator.draw();
	}
}
