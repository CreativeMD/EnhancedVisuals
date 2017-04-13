package com.sonicjumper.enhancedvisuals.handlers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nullable;

import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.ThrowableImpactEvent;

public class PotionHandler extends VisualHandler {

	public PotionHandler() {
		super("potion", "splash potion effect");
	}
	
	@Override
	public void onThrowableImpact(ThrowableImpactEvent event)
	{
		if(event.getEntityThrowable() instanceof EntityPotion && !event.isCanceled())
		{
			EntityPotion ep = (EntityPotion) event.getEntityThrowable();
			if(!ep.worldObj.isRemote)
			{
				double modifier = 1-ep.getDistanceToEntity(Minecraft.getMinecraft().thePlayer)/5;
				int var11 = PotionUtils.getPotionColor(PotionUtils.getPotionFromItem(ep.getPotion()));
				float r = (float)(var11 >> 16 & 255) / 255.0F;
				float g = (float)(var11 >> 8 & 255) / 255.0F;
				float b = (float)(var11 & 255) / 255.0F;
				float f1 = (float) (modifier * 2.0F);
				VisualManager.addVisualWithShading(VisualType.potion, Math.min(1, f1), 30, 60, new Color(r, g, b));
			}
		}
	}
}
