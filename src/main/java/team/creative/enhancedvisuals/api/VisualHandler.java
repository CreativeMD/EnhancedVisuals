package team.creative.enhancedvisuals.api;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.creativecore.common.config.CreativeConfig;
import team.creative.creativecore.common.config.CreativeConfig.FloatRange;
import team.creative.creativecore.common.config.CreativeConfigBase;
import team.creative.enhancedvisuals.client.sound.PositionedSound;

public class VisualHandler extends CreativeConfigBase {
	
	@CreativeConfig
	public boolean enabled;
	
	@CreativeConfig
	@FloatRange(max = 1, min = 0)
	public float opacity = 1;
	
	@Override
	public void configured() {
		
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public synchronized void playSound(ResourceLocation location, BlockPos pos) {
		playSound(location, pos, 1.0F);
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public synchronized void playSound(ResourceLocation location, BlockPos pos, float volume) {
		if (pos != null)
			Minecraft.getInstance().getSoundHandler().play(new PositionedSound(location, SoundCategory.MASTER, volume, 1, pos));
		else
			Minecraft.getInstance().getSoundHandler().play(new PositionedSound(location, SoundCategory.MASTER, volume, 1));
	}
	
}
