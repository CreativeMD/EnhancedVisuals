package com.sonicjumper.enhancedvisuals.visuals;

public class VisualProperties {
	
	public int posX;
	public int posY;
	
	public int width;
	public int height;
	
	public final float rotation;
	
	public VisualProperties(int posX, int posY, int width, int height, float rotation) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
	}
	
}
