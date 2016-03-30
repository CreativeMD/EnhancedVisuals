package com.sonicjumper.enhancedvisuals.render;

import com.sonicjumper.enhancedvisuals.visuals.Animation;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderAnimation extends RenderVisual {
	
	@Override
	public void renderVisual(Visual v, float partialTicks) {
		if(v instanceof Animation){
			Animation animation = (Animation) v;
			if(animation.intensity > 0)
			{
				ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
				//GL11.glColor4f(v.getColor().getRed() / 255.0F, v.getColor().getGreen() / 255.0F, v.getColor().getGreen() / 255.0F, animation.intensity);
				int time = (int) (System.currentTimeMillis()/animation.animationSpeed);
				int index = Math.floorMod(time, v.getType().resourceArray.length);
				
				if(index >= 0 && index < v.getType().resourceArray.length)
					Minecraft.getMinecraft().getTextureManager().bindTexture(v.getType().resourceArray[index]);
				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer renderer = tessellator.getBuffer();
				//Base.log.info("Width/Height: " + Minecraft.getMinecraft().displayWidth + "/" + Minecraft.getMinecraft().displayHeight + "; Scaled Width/Height: " + scaledRes.getScaledWidth() + "/" + scaledRes.getScaledHeight());
				float red = v.getColor().getRed() / 255.0F;
				float green = v.getColor().getGreen() / 255.0F;
				float blue = v.getColor().getBlue() / 255.0F;
				float alpha = v.getTranslucencyByTime();
				double z = -90;
				renderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				renderer.pos(0.0D, scaledRes.getScaledHeight(), z).tex(0.0D, 1.0D).color(red, green, blue, alpha).endVertex();
				renderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), z).tex(1.0D, 1.0D).color(red, green, blue, alpha).endVertex();
				renderer.pos(scaledRes.getScaledWidth(), 0.0D, z).tex( 1.0D, 0.0D).color(red, green, blue, alpha).endVertex();
				renderer.pos(0.0D, 0.0D, z).tex( 0.0D, 0.0D).color(red, green, blue, alpha).endVertex();
				/*renderer.startDrawingQuads();
				renderer.addVertexWithUV(0.0D, scaledRes.getScaledHeight(), -90.0D, 0.0D, 1.0D);
				renderer.addVertexWithUV(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D, 1.0D, 1.0D);
				renderer.addVertexWithUV(scaledRes.getScaledWidth(), 0.0D, -90.0D, 1.0D, 0.0D);
				renderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);*/
				tessellator.draw();
			}
		}else
			super.renderVisual(v, partialTicks);
		
	}
}
