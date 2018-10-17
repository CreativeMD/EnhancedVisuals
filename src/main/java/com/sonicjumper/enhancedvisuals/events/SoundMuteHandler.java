package com.sonicjumper.enhancedvisuals.events;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import paulscode.sound.Library;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.Source;

public class SoundMuteHandler {
	
	public static boolean isMuting = false;
	
	public static HashMap<Source, Float> sources = null;
	
	public static Library soundLibrary;
	public static SoundSystem sndSystem;
	
	public static ArrayList<String> ignoredSounds;
	
	public static int mutingTime;
	public static int timer = 0;
	public static float mutingFactor;
	
	public static void tick() {
		if (isMuting) {
			int remaining = mutingTime - timer;
			
			if (remaining <= 0)
				endMuting();
			else {
				updateSounds();
				
				setMuteFactor(getMutingFactorPerTick());
				timer++;
			}
		}
	}
	
	public static float getMutingFactorPerTick() {
		int remaining = mutingTime - timer;
		float percentage = remaining / (float) mutingTime;
		float volumeSpan = 1 - mutingFactor;
		return (float) (volumeSpan * Math.pow(1 - percentage, 2) + mutingFactor);
	}
	
	public static void updateSounds() {
		try {
			HashMap<String, Source> sourcesAndIDs = null;
			synchronized (SoundSystemConfig.THREAD_SYNC) {
				sourcesAndIDs = new HashMap<>(soundLibrary.getSources());
			}
			for (Source source : sourcesAndIDs.values()) {
				if (!sources.containsKey(source) && !ignoredSounds.contains(source.sourcename))
					sources.put(source, source.sourceVolume);
			}
		} catch (Exception e) {
			//Thread error
			e.printStackTrace();
		}
	}
	
	public static void startMuting(int mutingTime, float mutingFactor) {
		if (soundLibrary == null) {
			SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
			SoundManager sndManager = ReflectionHelper.getPrivateValue(SoundHandler.class, handler, "sndManager", "field_147694_f");
			sndSystem = ReflectionHelper.getPrivateValue(SoundManager.class, sndManager, "sndSystem", "field_148620_e");
			soundLibrary = ReflectionHelper.getPrivateValue(SoundSystem.class, sndSystem, "soundLibrary");
		}
		
		if (isMuting && getMutingFactorPerTick() > mutingFactor) {
			SoundMuteHandler.mutingFactor = mutingFactor;
			SoundMuteHandler.mutingTime = mutingTime;
			SoundMuteHandler.timer = 0;
		}
		if (!isMuting) {
			SoundMuteHandler.mutingFactor = mutingFactor;
			SoundMuteHandler.mutingTime = mutingTime;
			SoundMuteHandler.timer = 0;
			sources = new HashMap<>();
			ignoredSounds = new ArrayList<>();
			updateSounds();
			isMuting = true;
		}
	}
	
	public static void endMuting() {
		setMuteFactor(1F);
		sources = null;
		isMuting = false;
		ignoredSounds = null;
	}
	
	public static synchronized void setMuteFactor(float muteVolume) {
		if (isMuting && sources != null) {
			synchronized (SoundSystemConfig.THREAD_SYNC) {
				for (Source source : sources.keySet()) {
					sndSystem.setVolume(source.sourcename, sources.get(source) * muteVolume);
				}
			}
		}
	}
	
}
