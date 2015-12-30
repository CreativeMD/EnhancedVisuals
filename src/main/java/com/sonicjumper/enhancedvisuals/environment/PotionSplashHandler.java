package com.sonicjumper.enhancedvisuals.environment;

import java.awt.Color;
import java.util.ArrayList;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.event.VisualEventHandler;
import com.sonicjumper.enhancedvisuals.shaders.util.MathUtil;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.AxisAlignedBB;

public class PotionSplashHandler extends BaseEnvironmentEffect {

	public PotionSplashHandler(VisualEventHandler veh) {
		super(veh);
	}

	@Override
	public void onTick() {
		try{
			AxisAlignedBB var1 = AxisAlignedBB.getBoundingBox(Math.floor(parent.mc.thePlayer.posX) - 4.5D, Math.floor(parent.mc.thePlayer.posY) - 5.0D, Math.floor(parent.mc.thePlayer.posZ) - 4.5D, Math.floor(parent.mc.thePlayer.posX) + 4.5D, Math.floor(parent.mc.thePlayer.posY) + 2.0D, Math.floor(parent.mc.thePlayer.posZ) + 4.5D);
			
			for(EntityPotion ep : (ArrayList<EntityPotion>) parent.mc.theWorld.getEntitiesWithinAABB(EntityPotion.class, var1)) {
				if(ep.isDead) {
					double modifier = 1/Math.sqrt(Math.pow(Math.floor(parent.mc.thePlayer.posX) - ep.posX, 2) + Math.pow(Math.floor(parent.mc.thePlayer.posY) - ep.posY, 2) + Math.pow(Math.floor(parent.mc.thePlayer.posZ) - ep.posZ, 2));
					int var11 = PotionHelper.func_77915_a(ep.getPotionDamage(), false);
					float r = (float)(var11 >> 16 & 255) / 255.0F;
					float g = (float)(var11 >> 8 & 255) / 255.0F;
					float b = (float)(var11 & 255) / 255.0F;
					float f1 = (float) (modifier * 2.0F);
					Base.instance.manager.addVisualsWithShading(VisualType.potion, 1, 30, 60, new Color(r, g, b, f1 <= 1.0F ? f1 : 1.0F));
				}
			}
		}catch(Exception e){
			
		}
	}
	
	@Override
	public void resetEffect() {
		
	}
}
