package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import com.sonicjumper.enhancedvisuals.filters.AbstractBufferedImageOp;
import com.sonicjumper.enhancedvisuals.filters.BoxBlurFilter;
import com.sonicjumper.enhancedvisuals.render.BlurHelper;

import net.minecraft.client.Minecraft;

public class BoxBlur extends Blur {
	protected int blurRadius, blurIterations;
	
	public BoxBlur(VisualType type, int time, Color rgba, boolean resets, float scale, int radius, int iterations) {
		super(type, time, rgba, resets, scale);
		blurRadius = radius;
		blurIterations = iterations;
	}

	public void resetBufferedImage(float translucency) {
		if(resetsOnTick || image == null) {
			long time = Minecraft.getSystemTime();
			long totalTime = Minecraft.getSystemTime();
			Minecraft mc = Minecraft.getMinecraft();
			BufferedImage screen = BlurHelper.captureScreenAsImage(mc.displayWidth, mc.displayHeight);
			System.out.println("Capture Time: " + (Minecraft.getSystemTime() - time));
			
			time = Minecraft.getSystemTime();
			BufferedImage scaledImage = BlurHelper.scaleImage(screen, blurScaling);
			System.out.println("Scale Time: " + (Minecraft.getSystemTime() - time));
			
			((BoxBlurFilter) filter).setRadius((int) (blurRadius * translucency));
			
			time = Minecraft.getSystemTime();
			BufferedImage filtered = filter.filter(scaledImage, null);
			System.out.println("Filter Time: " + (Minecraft.getSystemTime() - time));
			
			//time = Minecraft.getSystemTime();
			image = prepareImage(filtered);
			//System.out.println("Buffer Time: " + (Minecraft.getSystemTime() - time));
			System.out.println("Total Time: " + (Minecraft.getSystemTime() - totalTime));
		}
	}
	
	public AbstractBufferedImageOp getFilter() {
		BoxBlurFilter bbf = new BoxBlurFilter();
		bbf.setRadius(getRadius());
		bbf.setIterations(getIterations());
		return bbf;
	}

	public int getRadius() {
		return blurRadius;
	}
	
	public int getIterations() {
		return blurIterations;
	}
}
