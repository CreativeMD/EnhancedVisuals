package com.sonicjumper.enhancedvisuals.visuals.types;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sonicjumper.enhancedvisuals.EnhancedVisuals;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public abstract class VisualTypeTexture extends VisualType {
	
	public int animationSpeed;

	public VisualTypeTexture(VisualCategory category, String name, int animationSpeed, boolean isAffectedByWater) {
		super(category, name, isAffectedByWater);
		this.animationSpeed = animationSpeed;
	}
	
	public ResourceLocation[] resources;
	public Dimension dimension;

	@Override
	public void loadTextures(IResourceManager manager) {
		String baseLocation = "visuals/" + category + "/" + name + "/" + name;
		
		ArrayList<ResourceLocation> locations = new ArrayList<>();
		int i = 0;
		ResourceLocation location = null;
		IResource resource = null;
		try {
			while((resource = manager.getResource((location = new ResourceLocation(EnhancedVisuals.modid, baseLocation + i + ".png")))) != null)
			{
				if(i == 0)
				{
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
		if(resources.length == 0)
		{
			EnhancedVisuals.log.warn("Could not find any resources for '" + name + "'!");
		}
	}
	
	@Override
	public boolean supportsColor() {
		return true;
	}
	
	@Override
	public boolean isEnabled()
	{
		return super.isEnabled() && resources.length > 0;
	}
	
	@Override
	public int getVariantAmount() {
		return resources.length;
	}
	
	public ResourceLocation getResource(Visual visual)
	{
		if(animationSpeed > 0)
		{
			long time = Math.abs(System.currentTimeMillis()/animationSpeed);
			return resources[(int) (time % resources.length)];
		}
		return resources[visual.variant];
	}
	
	@Override
	public void render(Visual visual, TextureManager manager, ScaledResolution resolution, float partialTicks, float intensity) {
		manager.bindTexture(getResource(visual));
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		
		float red = visual.getColor().getRed() / 255.0F;
		float green = visual.getColor().getGreen() / 255.0F;
		float blue = visual.getColor().getBlue() / 255.0F;
		double z = -90;
		
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();
		
		if(visual.properties != null)
		{
			width = visual.properties.width;
			height = visual.properties.height;
			GlStateManager.translate(visual.properties.posX + width / 2, visual.properties.posY + height / 2, 0);
			GlStateManager.rotate(visual.properties.rotation, 0, 0, 1);
		}
		
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		renderer.pos(0.0D, height, z).tex(0.0D, 1.0D).color(red, green, blue, intensity).endVertex();
		renderer.pos(width, height, z).tex(1.0D, 1.0D).color(red, green, blue, intensity).endVertex();
		renderer.pos(width, 0.0D, z).tex( 1.0D, 0.0D).color(red, green, blue, intensity).endVertex();
		renderer.pos(0.0D, 0.0D, z).tex( 0.0D, 0.0D).color(red, green, blue, intensity).endVertex();
		tessellator.draw();
	}
	
	@Override
	public boolean needsToBeRendered(float intensity) {
		return intensity > 0;
	}
}
