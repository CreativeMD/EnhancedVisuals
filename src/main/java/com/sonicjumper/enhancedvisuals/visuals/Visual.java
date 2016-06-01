package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;
import java.util.Random;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class Visual {
	public enum VisualCatagory {
		Splat,
		Overlay,
		Animation,
		Shader;
	}
	
	public static Random rand = new Random();

	private VisualType visualType;
	private float xOffset;
	private float yOffset;
	private int resourceID;
	private int height;
	private int width;
	private int ticksToLive;
	private int maxTime;
	private float rotation;
	private Color color;
	private boolean isDead;
	private int realTickTime;

	public Visual(VisualType type, int lifeTime) {
		this(type, lifeTime, Color.WHITE);
	}

	public Visual(VisualType type, int lifeTime, Color rgba) {
		this.visualType = type;
		if(type.resourceArray.length > 0) {
			this.resourceID = rand.nextInt(type.resourceArray.length);
		}
		this.ticksToLive = lifeTime;
		this.maxTime = lifeTime;
		this.realTickTime = 0;
		this.width = (int) (type.getSize()*Math.random());
		this.height = (int) (type.getSize()*Math.random());
		this.rotation = (float) (Math.random()*360F);
		this.color = rgba;
		if(type.getCatagory().equals(VisualCatagory.Splat)) {
			ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
			this.xOffset = generateOffset(scaledRes.getScaledWidth(), this.width);
			this.yOffset = generateOffset(scaledRes.getScaledHeight(), this.height);
		} else {
			this.xOffset = 0.0F;
			this.yOffset = 0.0F;
		}
	}
	
	public Visual(VisualType type, int lifeTime, Color rgba, float xOffset, float yOffset) {
		this(type, lifeTime, rgba);
		ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
		this.xOffset = xOffset * scaledRes.getScaledWidth() - width / 2.0F;
		this.yOffset = yOffset * scaledRes.getScaledHeight() - height / 2.0F;
	}

	public void beingRemoved() {}

	private float generateOffset(int dimensionLength, int spacingBuffer) {
		float halfDimLength = (float) dimensionLength / 2.0F;
		float multiplier = (float) (1 - Math.pow(rand.nextDouble(), 2));
		float textureCenterPosition = rand.nextInt(2) == 0 ? halfDimLength + halfDimLength * multiplier : halfDimLength - halfDimLength * multiplier;
		float textureShiftedPosition = textureCenterPosition - (spacingBuffer / 2.0F);
		return textureShiftedPosition;
		//return rand.nextInt(2) == 0 ? 0.5F + (0.5F * multiplier) : 0.5F - (0.5F * multiplier);
	}
	
	public void renderUpdate(float partialTicks)
	{
		if(!isDead)
		{
			getRenderer().renderVisual(this, partialTicks);
		}
	}

	public void tickUpdate() {
		this.realTickTime++;
		if(this.ticksToLive == 0) {
			this.isDead = true;
		} else if(!this.isDead) {
			this.ticksToLive = this.ticksToLive < 0 ? this.ticksToLive : this.ticksToLive - 1;
		}
		if(shouldRemove()) {
			Base.instance.manager.removeVisual(this);
		}
	}

	public void subtractTicks(int i) {
		this.ticksToLive -= i;
		if(this.ticksToLive < 0) {
			this.ticksToLive = 0;
		}
	}

	public void subtractTickPercent(float f) {
		this.ticksToLive -= this.maxTime * (f / 100.0F);
		if(this.ticksToLive < 0) {
			this.ticksToLive = 0;
		}
	}

	public void setTranslucency(float intensity) {
		this.color = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), (int)(intensity * 255.0F));
	}

	/**
	 * Note: For Shaders, this can mean the percentage of life this visual has left.
	 * @return A value 0.0 to 1.0 that represents what Alpha level this texture should be drawn at.
	 */
	public float getTranslucencyByTime() {
		if (this.ticksToLive != -1) {
			Float var1 = new Float(this.ticksToLive);
			Float var2 = new Float(this.maxTime);
			float var3 = var1.floatValue() / var2.floatValue();
			return this.color.getAlpha() / 255.0F * var3 * visualType.alpha;
		}
		return this.color.getAlpha() / 255.0F * visualType.alpha;
	}

	public VisualType getType() {
		return this.visualType;
	}

	public int getResourceID() {
		return this.resourceID;
	}

	public float getXOffset() {
		return this.xOffset;
	}

	public float getYOffset() {
		return this.yOffset;
	}

	public float getWidth() {
		return this.width;
	}

	public float getHeight() {
		return this.height;
	}

	public Color getColor() {
		return this.color;
	}

	public int getMaxTime() {
		return this.maxTime;
	}
	
	public float getRotation()
	{
		return this.rotation;
	}
	
	/**
	 * The realTickTime is how many ticks this visual has existed, regardless of state changes. Use for looping a shader or effect.
	 * @return How many ticks this visual has existed
	 */
	public int getRealTickTime() {
		return this.realTickTime;
	}

	public boolean shouldRemove() {
		return isDead;
	}

	public ResourceLocation getResource() {
		return this.visualType.resourceArray[this.resourceID];
	}
	
	public RenderVisual getRenderer() {
		return Base.instance.renderer.getRendererForClass(RenderVisual.class);
	}

	public String toString() {
		return super.toString() + "[x = " + getXOffset() + ", y = " + getYOffset() + ", translucency = " + getTranslucencyByTime() + ", timeLeft = " + this.ticksToLive + ", maxTime = " + this.maxTime + ", resourceLocation = " + getResource() + "]";
	}
}
