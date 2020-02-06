package team.creative.enhancedvisuals.api;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.creativecore.common.config.CreativeConfig;
import team.creative.creativecore.common.config.CreativeConfig.FloatRange;
import team.creative.creativecore.common.config.ICreativeConfig;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.enhancedvisuals.client.sound.PositionedSound;
import team.creative.enhancedvisuals.client.sound.TickedSound;

public class VisualHandler implements ICreativeConfig {
	
	@CreativeConfig
	public boolean enabled = true;
	
	@CreativeConfig
	@FloatRange(max = 1, min = 0)
	public float opacity = 1;
	
	@Override
	public void configured() {
		
	}
	
	public void tick(@Nullable PlayerEntity player) {
		
	}
	
	public boolean isEnabled(@Nullable PlayerEntity player) {
		return enabled && opacity > 0;
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public synchronized void playSound(ResourceLocation location) {
		playSound(location, null, 1.0F);
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public synchronized void playSound(ResourceLocation location, BlockPos pos) {
		playSound(location, pos, 1.0F);
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public synchronized void playSound(ResourceLocation location, float volume) {
		playSound(location, null, volume);
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public synchronized void playSound(ResourceLocation location, BlockPos pos, float volume) {
		if (pos != null)
			Minecraft.getInstance().getSoundHandler().play(new PositionedSound(location, SoundCategory.MASTER, volume, 1, pos));
		else
			Minecraft.getInstance().getSoundHandler().play(new PositionedSound(location, SoundCategory.MASTER, volume, 1));
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public synchronized void playSoundFadeOut(ResourceLocation location, BlockPos pos, DecimalCurve volume) {
		
		if (pos != null)
			Minecraft.getInstance().getSoundHandler().play(new TickedSound(location, SoundCategory.MASTER, 1, pos, volume));
		else
			Minecraft.getInstance().getSoundHandler().play(new TickedSound(location, SoundCategory.MASTER, 1, volume));
	}
	
}
