package com.sonicjumper.enhancedvisuals.render;

import com.sonicjumper.enhancedvisuals.visuals.Blur;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderBlur extends RenderVisual {
    public static Minecraft mc = Minecraft.getMinecraft();
	
    @Override
	public void renderVisual(Visual v, float partialTicks) {
		
		Blur b = (Blur) v;
		/*Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(v.getResource());
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, b.getImageWidth(), b.getImageHeight(), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, b.getBufferedImage());
		ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		GL11.glColor4f(v.getColor().getRed() / 255.0F, v.getColor().getGreen() / 255.0F, v.getColor().getBlue() / 255.0F, v.getTranslucencyByTime());
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV(0.0D, scaledRes.getScaledHeight_double(), -90.0D, 0.0D, 1.0D);
		worldrenderer.addVertexWithUV(scaledRes.getScaledWidth_double(), scaledRes.getScaledHeight_double(), -90.0D, 1.0D, 1.0D);
		worldrenderer.addVertexWithUV(scaledRes.getScaledWidth_double(), 0.0D, -90.0D, 1.0D, 0.0D);
		worldrenderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator.draw();*/
		
		/*ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
		Minecraft.getMinecraft().getTextureManager().bindTexture(v.getResource());
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();
		
		float red = v.getColor().getRed() / 255.0F;
		float green = v.getColor().getGreen() / 255.0F;
		float blue = v.getColor().getBlue() / 255.0F;
		float alpha = v.getTranslucencyByTime();
		double z = -90;
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		renderer.pos(0.0D, scaledRes.getScaledHeight(), z).tex(0.0D, 1.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), z).tex(1.0D, 1.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(scaledRes.getScaledWidth(), 0.0D, z).tex( 1.0D, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(0.0D, 0.0D, z).tex( 0.0D, 0.0D).color(red, green, blue, alpha).endVertex();*/
	}
}
