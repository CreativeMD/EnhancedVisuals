package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;

@Mixin(SoundManager.class)
public interface SoundManagerAccessor {
	@Accessor
	SoundEngine getSoundEngine();
}
