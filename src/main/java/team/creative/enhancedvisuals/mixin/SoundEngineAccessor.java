package team.creative.enhancedvisuals.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;

@Mixin(SoundEngine.class)
public interface SoundEngineAccessor {
	@Accessor
	Map<SoundInstance, ChannelAccess.ChannelHandle> getInstanceToChannel();
	@Invoker
	float invokeCalculateVolume(SoundInstance soundInstance);
}
