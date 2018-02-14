package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;
import java.util.Random;

import javax.annotation.Nullable;

import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;

public abstract class Visual {
	
	public static Random rand = new Random();
	
	public VisualType type;
	protected float intensity;
	protected Color color;
	public int variant;
	
	public VisualProperties properties;
	
	public Visual(VisualType type, float intensity) {
		this(type, intensity, Color.WHITE);
	}
	
	public Visual(VisualType type, float intensity, Color color) {
		this.type = type;
		this.intensity = intensity;
		this.color = color;
		variant = type.getVariantAmount() > 0 ? rand.nextInt(type.getVariantAmount()) : 0;
		if(isRandomized())
			properties = randomize();
	}
	
	public void onTick(@Nullable EntityPlayer player)
	{
		
	}
	
	public float getIntensity(float partialTicks)
	{
		return intensity;
	}
	
	public boolean supportsColor()
	{
		return type.supportsColor();
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public abstract boolean hasFinished();
	
	public abstract boolean isRandomized();
	
	public VisualProperties randomize()
	{
		int size = rand.nextInt(type.getSize());
		VisualProperties properties = new VisualProperties(0, 0, size, size, rand.nextFloat()*360);
		ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
		properties.posX = generateOffset(scaledRes.getScaledWidth(), properties.width);
		properties.posY = generateOffset(scaledRes.getScaledHeight(), properties.height);
		return properties;
	}
		
	public static int generateOffset(int dimensionLength, int spacingBuffer) {
		float halfDimLength = (float) dimensionLength / 2.0F;
		float multiplier = (float) (1 - Math.pow(rand.nextDouble(), 2));
		float textureCenterPosition = rand.nextInt(2) == 0 ? halfDimLength + halfDimLength * multiplier : halfDimLength - halfDimLength * multiplier;
		return (int) (textureCenterPosition - (spacingBuffer / 2.0F));
	}
}
