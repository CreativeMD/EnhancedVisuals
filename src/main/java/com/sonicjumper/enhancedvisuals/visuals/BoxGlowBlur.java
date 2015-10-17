package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.sonicjumper.enhancedvisuals.filters.AbstractBufferedImageOp;
import com.sonicjumper.enhancedvisuals.filters.BoxGlowFilter;
import com.sonicjumper.enhancedvisuals.render.BlurHelper;

import net.minecraft.client.Minecraft;

public class BoxGlowBlur extends BoxBlur {
	private float glowAmount;
	
	public BoxGlowBlur(VisualType type, int time, Color rgba, boolean resets, float scale, int radius, int iterations, float amount) {
		super(type, time, rgba, resets, scale, radius, iterations);
		glowAmount = amount;
	}
	
	public void resetBufferedImage(float translucency) {
		if(resetsOnTick || image == null) {
			BufferedImage screen = BlurHelper.captureScreenAsImage(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
			BufferedImage scaledImage = BlurHelper.scaleImage(screen, blurScaling);
			((BoxGlowFilter) filter).setRadius((int) (blurRadius * translucency));
			((BoxGlowFilter) filter).setAmount(glowAmount * translucency);
			//((BoxGlowFilter) filter).setIterations(getIterations());
			long time = Minecraft.getSystemTime();
			BufferedImage filtered = filter.filter(scaledImage, null);
			System.out.println("Filter Time: " + (Minecraft.getSystemTime() - time));
			image = prepareImage(filtered);
		}
	}
	
	@Override
	public AbstractBufferedImageOp getFilter() {
		BoxGlowFilter gf = new BoxGlowFilter();
		gf.setRadius(getRadius());
		gf.setAmount(getGlowAmount());
		gf.setIterations(getIterations());
		return gf;
	}
	
	public float getGlowAmount() {
		return glowAmount;
	}
}
