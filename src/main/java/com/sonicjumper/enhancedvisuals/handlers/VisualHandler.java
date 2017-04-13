package com.sonicjumper.enhancedvisuals.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.ThrowableImpactEvent;

public abstract class VisualHandler {
	
	private static ArrayList<VisualHandler> handlers = new ArrayList<>();
	
	public static PotionHandler potion = new PotionHandler();
	public static SplashHandler splash = new SplashHandler();
	public static SandSplatHandler sand = new SandSplatHandler();
	public static HeartBeatHandler heartBeat = new HeartBeatHandler();
	
	public static ExplosionHandler explosion = new ExplosionHandler();
	public static DamageHandler damage = new DamageHandler();
	
	public static SaturationHandler saturation = new SaturationHandler();
	public static SlenderHandler slender = new SlenderHandler();
	
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
	public final String comment;
	
	public VisualHandler(String name, String comment) {
		handlers.add(this);
		this.name = name + "-handler";
		this.comment = comment;
	}
	
	public void initConfig(Configuration config)
	{
		config.addCustomCategoryComment(name, comment);
		enabled = config.getBoolean("enabled", name, enabled, "");
	}
	
	public void onPlayerDamaged(EntityPlayer player, DamageSource source, float damage) {}
	
	public void onEntityDamaged(EntityLivingBase entity, DamageSource source, float damage, double distance) {}
	
	public void onTick(@Nullable EntityPlayer player) {}
	
	public void onThrowableImpact(ThrowableImpactEvent event) {}
	
	public void onExplosion(EntityPlayer player, double x, double y, double z, double distance) {}
	
	public synchronized void playSound(ResourceLocation location, BlockPos pos)
	{
		playSound(location, pos, 1.0F);
	}
	
	public synchronized void playSound(ResourceLocation location, BlockPos pos, float volume)
	{
		if(pos != null)
			Minecraft.getMinecraft().getSoundHandler().playDelayedSound(new PositionedSoundRecord(new SoundEvent(location), SoundCategory.MASTER, volume, 1, pos), 0);
		else
			Minecraft.getMinecraft().getSoundHandler().playDelayedSound(new PositionedSoundRecord(location, SoundCategory.MASTER, volume, 1, false, 0, AttenuationType.NONE, 0, 0, 0), 0);
	}
	
}
