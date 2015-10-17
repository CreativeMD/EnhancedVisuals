package com.sonicjumper.enhancedvisuals.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

import org.lwjgl.opengl.GL11;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

public class RenderOverlay extends RenderVisual {
	
	@Override
	public void renderVisual(Visual v, float partialTicks) {
		ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		GL11.glColor4f(v.getColor().getRed() / 255.0F, v.getColor().getGreen() / 255.0F, v.getColor().getGreen() / 255.0F, v.getTranslucencyByTime());
		Minecraft.getMinecraft().getTextureManager().bindTexture(v.getResource());
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		//Base.log.info("Width/Height: " + Minecraft.getMinecraft().displayWidth + "/" + Minecraft.getMinecraft().displayHeight + "; Scaled Width/Height: " + scaledRes.getScaledWidth() + "/" + scaledRes.getScaledHeight());
		renderer.startDrawingQuads();
		renderer.addVertexWithUV(0.0D, scaledRes.getScaledHeight(), -90.0D, 0.0D, 1.0D);
		renderer.addVertexWithUV(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D, 1.0D, 1.0D);
		renderer.addVertexWithUV(scaledRes.getScaledWidth(), 0.0D, -90.0D, 1.0D, 0.0D);
		renderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator.draw();
	}
}
