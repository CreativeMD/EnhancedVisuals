package com.sonicjumper.enhancedvisuals.environment;

import java.awt.Color;
import java.util.ArrayList;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.event.VisualEventHandler;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.AxisAlignedBB;

public class PotionSplashHandler extends BaseEnvironmentEffect {

	public PotionSplashHandler(VisualEventHandler veh) {
		super(veh);
	}

	@Override
	public void onTick() {
		AxisAlignedBB var1 = new AxisAlignedBB(Math.floor(parent.mc.thePlayer.posX) - 4.5D, Math.floor(parent.mc.thePlayer.posY) - 5.0D, Math.floor(parent.mc.thePlayer.posZ) - 4.5D, Math.floor(parent.mc.thePlayer.posX) + 4.5D, Math.floor(parent.mc.thePlayer.posY) + 2.0D, Math.floor(parent.mc.thePlayer.posZ) + 4.5D);

		for(EntityPotion ep : (ArrayList<EntityPotion>) parent.mc.theWorld.getEntitiesWithinAABB(EntityPotion.class, var1)) {
			if(ep.isDead) {
				double modifier = 1/Math.sqrt(Math.pow(Math.floor(parent.mc.thePlayer.posX) - ep.posX, 2) + Math.pow(Math.floor(parent.mc.thePlayer.posY) - ep.posY, 2) + Math.pow(Math.floor(parent.mc.thePlayer.posZ) - ep.posZ, 2));
				int var11 = PotionUtils.getPotionColor(PotionUtils.getPotionFromItem(ep.getPotion()));
				float r = (float)(var11 >> 16 & 255) / 255.0F;
				float g = (float)(var11 >> 8 & 255) / 255.0F;
				float b = (float)(var11 & 255) / 255.0F;
				float f1 = (float) (modifier * 2.0F);
				Base.instance.manager.addVisualsWithShading(VisualType.potion, 1, 30, 60, new Color(r, g, b, f1 <= 1.0F ? f1 : 1.0F));
			}
		}
	}
	
	@Override
	public void resetEffect() {
		
	}
}
