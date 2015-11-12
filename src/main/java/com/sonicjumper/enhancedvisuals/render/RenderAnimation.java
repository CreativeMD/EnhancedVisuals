package com.sonicjumper.enhancedvisuals.render;

import org.lwjgl.opengl.GL11;

import com.sonicjumper.enhancedvisuals.visuals.Animation;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

public class RenderAnimation extends RenderVisual {
	
	@Override
	public void renderVisual(Visual v, float partialTicks) {
		if(v instanceof Animation){
			Animation animation = (Animation) v;
			if(animation.intensity > 0)
			{
				ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
				GL11.glColor4f(v.getColor().getRed() / 255.0F, v.getColor().getGreen() / 255.0F, v.getColor().getGreen() / 255.0F, animation.intensity);
				int time = (int) (System.currentTimeMillis()/animation.animationSpeed);
				int index = time%v.getType().resourceArray.length;
				
				if(index >= 0 && index < v.getType().resourceArray.length)
					Minecraft.getMinecraft().getTextureManager().bindTexture(v.getType().resourceArray[index]);
				Tessellator tessellator = Tessellator.instance;
				//WorldRenderer renderer = tessellator.getWorldRenderer();
				//Base.log.info("Width/Height: " + Minecraft.getMinecraft().displayWidth + "/" + Minecraft.getMinecraft().displayHeight + "; Scaled Width/Height: " + scaledRes.getScaledWidth() + "/" + scaledRes.getScaledHeight());
				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(0.0D, scaledRes.getScaledHeight(), -90.0D, 0.0D, 1.0D);
				tessellator.addVertexWithUV(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D, 1.0D, 1.0D);
				tessellator.addVertexWithUV(scaledRes.getScaledWidth(), 0.0D, -90.0D, 1.0D, 0.0D);
				tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
				tessellator.draw();
			}
		}else
			super.renderVisual(v, partialTicks);
		
	}
}
