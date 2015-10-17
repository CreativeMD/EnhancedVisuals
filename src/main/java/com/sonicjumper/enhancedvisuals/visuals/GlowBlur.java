package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.sonicjumper.enhancedvisuals.filters.AbstractBufferedImageOp;
import com.sonicjumper.enhancedvisuals.filters.GlowFilter;
import com.sonicjumper.enhancedvisuals.render.BlurHelper;

import net.minecraft.client.Minecraft;

public class GlowBlur extends Blur {
	private float glowAmount;
	protected int blurRadius;
	
	public GlowBlur(VisualType type, int time, Color rgba, boolean resets, float scale, int radius, float amount) {
		super(type, time, rgba, resets, scale);
		glowAmount = amount;
		blurRadius = radius;
	}
	
	public void resetBufferedImage(float translucency) {
		if(resetsOnTick || image == null) {
			BufferedImage screen = BlurHelper.captureScreenAsImage(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
			BufferedImage scaledImage = BlurHelper.scaleImage(screen, blurScaling);
			((GlowFilter) filter).setRadius((int) (blurRadius * translucency));
			((GlowFilter) filter).setAmount(glowAmount * translucency);
			//((BoxGlowFilter) filter).setIterations(getIterations());
			long time = Minecraft.getSystemTime();
			BufferedImage filtered = filter.filter(scaledImage, null);
			System.out.println("Filter Time: " + (Minecraft.getSystemTime() - time));
			image = prepareImage(filtered);
		}
	}
	
	@Override
	public AbstractBufferedImageOp getFilter() {
		GlowFilter gf = new GlowFilter();
		gf.setRadius(getRadius());
		gf.setAmount(getGlowAmount());
		return gf;
	}
	
	protected int getRadius() {
		return blurRadius;
	}

	public float getGlowAmount() {
		return glowAmount;
	}
}
