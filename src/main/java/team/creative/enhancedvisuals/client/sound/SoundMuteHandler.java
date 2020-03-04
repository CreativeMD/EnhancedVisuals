package team.creative.enhancedvisuals.client.sound;

import java.util.ArrayList;
import java.util.HashMap;

import com.creativemd.creativecore.common.config.premade.curve.DecimalCurve;

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
	
	public static DecimalCurve muteGraph;
	public static int timeTick = 0;
	
	public static void tick() {
		if (isMuting) {
			double factor = muteGraph.valueAt(timeTick);
			
			if (factor <= 0)
				endMuting();
			else {
				setMuteVolume((float) (1 - factor));
				timeTick++;
			}
		}
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
	
	public static boolean startMuting(DecimalCurve muteGraph) {
		if (soundLibrary == null) {
			SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
			SoundManager sndManager = ReflectionHelper.getPrivateValue(SoundHandler.class, handler, new String[] { "sndManager", "field_147694_f" });
			sndSystem = ReflectionHelper.getPrivateValue(SoundManager.class, sndManager, new String[] { "sndSystem", "field_148620_e" });
			soundLibrary = ReflectionHelper.getPrivateValue(SoundSystem.class, sndSystem, new String[] { "soundLibrary" });
		}
		
		if (isMuting && SoundMuteHandler.muteGraph.valueAt(timeTick) > muteGraph.valueAt(0)) {
			SoundMuteHandler.muteGraph = muteGraph;
			SoundMuteHandler.timeTick = 0;
			tick();
			return true;
		} else if (!isMuting) {
			SoundMuteHandler.muteGraph = muteGraph;
			SoundMuteHandler.timeTick = 0;
			ignoredSounds = new ArrayList<>();
			isMuting = true;
			tick();
			return true;
		}
		return false;
	}
	
	public static void endMuting() {
		setMuteVolume(1F);
		sources = null;
		isMuting = false;
		ignoredSounds = null;
	}
	
	public static synchronized void setMuteVolume(float muteVolume) {
		if (isMuting && sources != null) {
			synchronized (SoundSystemConfig.THREAD_SYNC) {
				for (Source source : sources.keySet()) {
					sndSystem.setVolume(source.sourcename, sources.get(source) * muteVolume);
				}
			}
		}
	}
	
}
