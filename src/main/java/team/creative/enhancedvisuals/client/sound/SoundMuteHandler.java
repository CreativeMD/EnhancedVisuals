package team.creative.enhancedvisuals.client.sound;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ChannelManager;
import net.minecraft.client.audio.ChannelManager.Entry;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@OnlyIn(value = Dist.CLIENT)
public class SoundMuteHandler {
	
	private static Minecraft mc = Minecraft.getInstance();
	
	public static boolean isMuting = false;
	
	public static SoundEngine engine;
	public static SoundHandler handler;
	
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
				setMuteVolume(getMutingFactorPerTick());
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
	
	private static Field playingSoundsChannelField = ObfuscationReflectionHelper.findField(SoundEngine.class, "playingSoundsChannel");
	
	public static Map<ISound, ChannelManager.Entry> getSounds() {
		try {
			return (Map<ISound, Entry>) playingSoundsChannelField.get(engine);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void setMuteVolume(float muteVolume) {
		if (!isMuting)
			return;
		
		getSounds().forEach((p_217926_1_, p_217926_2_) -> {
			float f = getClampedVolume(p_217926_1_, muteVolume);
			p_217926_2_.runOnSoundExecutor((p_217923_1_) -> {
				if (f <= 0.0F) {
					p_217923_1_.func_216418_f();
				} else {
					p_217923_1_.func_216430_b(f);
				}
				
			});
		});
	}
	
	private static float getClampedVolume(ISound soundIn, float muteVolume) {
		return MathHelper.clamp(soundIn.getVolume() * getVolume(soundIn.getCategory()) * muteVolume, 0.0F, 1.0F);
	}
	
	private static float getVolume(SoundCategory category) {
		return category != null && category != SoundCategory.MASTER ? mc.gameSettings.getSoundLevel(category) : 1.0F;
	}
	
	public static void startMuting(int mutingTime, float mutingFactor) {
		if (engine == null) {
			handler = Minecraft.getInstance().getSoundHandler();
			engine = ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, handler, "sndManager");
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
			ignoredSounds = new ArrayList<>();
			isMuting = true;
			tick();
		}
	}
	
	public static void endMuting() {
		setMuteVolume(1F);
		isMuting = false;
		ignoredSounds = null;
	}
	
}
