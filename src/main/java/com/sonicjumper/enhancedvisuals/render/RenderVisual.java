package com.sonicjumper.enhancedvisuals.render;

import org.lwjgl.opengl.GL11;

import com.sonicjumper.enhancedvisuals.visuals.Visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderVisual {
	public void renderVisual(Visual v, float partialTicks) {
		GlStateManager.pushMatrix();
		
		
		
		//GL11.glColor4f(v.getColor().getRed() / 255.0F, v.getColor().getGreen() / 255.0F, v.getColor().getGreen() / 255.0F, v.getTranslucencyByTime());
		Minecraft.getMinecraft().getTextureManager().bindTexture(v.getResource());
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();
		//Base.log.info("X: " + v.getXOffset() + "; Y: " + v.getYOffset() + "; Width: " + v.getWidth() + "; Height: " + v.getHeight());
		//GlStateManager.rotate(90, 0, 0, 1);
		
		GL11.glTranslated(v.getXOffset()+v.getWidth()/2, v.getYOffset()+v.getHeight()/2, 0);
		GlStateManager.rotate(v.getRotation(), 0, 0, 1);
		
		float red = v.getColor().getRed() / 255.0F;
		float green = v.getColor().getGreen() / 255.0F;
		float blue = v.getColor().getBlue() / 255.0F;
		float alpha = v.getTranslucencyByTime();
		double z = -90;
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		renderer.pos(0.0D, v.getHeight(), z).tex(0.0D, 1.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(v.getWidth(), v.getHeight(), z).tex(1.0D, 1.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(v.getWidth(), 0.0D, z).tex( 1.0D, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(0.0D, 0.0D, z).tex( 0.0D, 0.0D).color(red, green, blue, alpha).endVertex();
		/*renderer.startDrawingQuads();
		renderer.addVertexWithUV(0.0D, v.getHeight(), -90.0D, 0.0D, 1.0D);
		renderer.addVertexWithUV(v.getWidth(), v.getHeight(), -90.0D, 1.0D, 1.0D);
		renderer.addVertexWithUV(v.getWidth(), 0.0D, -90.0D, 1.0D, 0.0D);
		renderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);*/
		/*renderer.addVertexWithUV(0.0D + v.getXOffset(), v.getHeight() + v.getYOffset(), -90.0D, 0.0D, 1.0D);
		renderer.addVertexWithUV(v.getWidth() + v.getXOffset(), v.getHeight() + v.getYOffset(), -90.0D, 1.0D, 1.0D);
		renderer.addVertexWithUV(v.getWidth() + v.getXOffset(), 0.0D + v.getYOffset(), -90.0D, 1.0D, 0.0D);
		renderer.addVertexWithUV(0.0D + v.getXOffset(), 0.0D + v.getYOffset(), -90.0D, 0.0D, 0.0D);*/
		
		tessellator.draw();
		GlStateManager.popMatrix();
	}
}
