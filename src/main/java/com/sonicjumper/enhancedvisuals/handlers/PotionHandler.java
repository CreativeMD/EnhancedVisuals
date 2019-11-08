package com.sonicjumper.enhancedvisuals.handlers;

import java.awt.Color;

import com.creativemd.igcm.api.ConfigBranch;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.creative.enhancedvisuals.client.VisualManager;

public class PotionHandler extends VisualHandler {
	
	public PotionHandler() {
		super("potion", "splash potion effect");
	}
	
	@Override
	@Method(modid = "igcm")
	public void registerConfigElements(ConfigBranch branch) {
		
	}
	
	@Override
	@Method(modid = "igcm")
	public void receiveConfigElements(ConfigBranch branch) {
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onThrowableImpact(ProjectileImpactEvent.Throwable event) {
		if (event.getEntity() instanceof EntityPotion && !event.isCanceled()) {
			EntityPotion ep = (EntityPotion) event.getEntity();
			if (!ep.world.isRemote) {
				double modifier = 1 - ep.getDistance(Minecraft.getMinecraft().player) / 5;
				int var11 = PotionUtils.getPotionColor(PotionUtils.getPotionFromItem(ep.getPotion()));
				float r = (float) (var11 >> 16 & 255) / 255.0F;
				float g = (float) (var11 >> 8 & 255) / 255.0F;
				float b = (float) (var11 & 255) / 255.0F;
				float f1 = (float) (modifier * 2.0F);
				VisualManager.addVisualWithShading(VisualType.potion, Math.min(1, f1), 30, 60, new Color(r, g, b));
			}
		}
	}
}
