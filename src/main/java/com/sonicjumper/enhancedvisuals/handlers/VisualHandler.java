package com.sonicjumper.enhancedvisuals.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.config.Configuration;

public abstract class VisualHandler {
	
	private static ArrayList<VisualHandler> handlers = new ArrayList<>();
	
	public static List<VisualHandler> getAllHandlers()
	{
		return handlers;
	}
	
	public static ArrayList<VisualHandler> activeHandlers = new ArrayList<>();
	
	public static void afterInit()
	{
		for (int i = 0; i < handlers.size(); i++) {
			if(handlers.get(i).enabled)
				activeHandlers.add(handlers.get(i));
		}
	}
	
	public boolean enabled = true;
	
	public final String name;
	
	public VisualHandler(String name) {
		handlers.add(this);
		this.name = name;
	}
	
	public void initConfig(Configuration config)
	{
		enabled = config.getBoolean("enalbed", name, enabled, "");
	}
	
	public void onPlayerDamaged(EntityPlayer player, DamageSource source, float damage) {}
	
	public void onEntityDamaged(EntityLivingBase entity, DamageSource source, float damage, double distance) {}
	
	public void onTick(@Nullable EntityPlayer player) {}
	
}
