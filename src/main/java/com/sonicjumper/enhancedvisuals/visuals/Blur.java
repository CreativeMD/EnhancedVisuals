package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;

import org.lwjgl.BufferUtils;

import com.sonicjumper.enhancedvisuals.filters.AbstractBufferedImageOp;
import com.sonicjumper.enhancedvisuals.render.BlurHelper;
import com.sonicjumper.enhancedvisuals.render.RenderBlur;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;

public class Blur extends Visual {
	protected ByteBuffer image;
	protected int imageHeight, imageWidth;
	protected boolean resetsOnTick;
	protected float blurScaling;
	public AbstractBufferedImageOp filter;
	
	public Blur(VisualType type, int time, Color rgba, boolean resets, float scale) {
		super(type, time, rgba);
		resetsOnTick = resets;
		blurScaling = scale;
	}

	public void resetBufferedImage(float translucency) {
		if(resetsOnTick || image == null) {
			BufferedImage screen = BlurHelper.captureScreenAsImage(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
			if(filter != null) {
				BufferedImage scaledImage = BlurHelper.scaleImage(screen, blurScaling);
				image = prepareImage(filter.filter(scaledImage, null));
			} else {
				image = prepareImage(screen);
			}
		}
	}

	protected ByteBuffer prepareImage(BufferedImage image) {
		imageHeight = image.getHeight();
		imageWidth = image.getWidth();
		int[] pixels = new int[image.getWidth() * image.getHeight()];
        filter.getRGB(image, 0, 0, imageWidth, imageHeight, pixels);
        
        ByteBuffer buffer = BufferUtils.createByteBuffer(imageWidth * imageHeight * 3); //4 for RGBA, 3 for RGB
        
        for(int y = 0; y < imageHeight; y++){
            for(int x = 0; x < imageWidth; x++){
                int pixel = pixels[y * imageWidth + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                //buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }
        buffer.flip();
        
        return buffer;
	}
	
	@Override
	public void tickUpdate() {
		super.tickUpdate();
	}
	
	@Override
	public void renderUpdate(float partialTicks) {
		if(filter == null) {
			filter = getFilter();
		}
		resetBufferedImage(getTranslucencyByTime());
		super.renderUpdate(partialTicks);
	}
	
	public int getImageHeight() {
		return imageHeight;
	}
	
	public int getImageWidth() {
		return imageWidth;
	}
	
	public RenderVisual getRenderer() {
		return new RenderBlur();
	}
	
	public ByteBuffer getBufferedImage() {
		return image;
	}
	
	public AbstractBufferedImageOp getFilter() {
		return null;
	}
	
	public float getScale() {
		return blurScaling;
	}
}
