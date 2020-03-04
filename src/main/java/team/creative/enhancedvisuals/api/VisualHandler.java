package team.creative.enhancedvisuals.api;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.api.ICreativeConfig;
import com.creativemd.creativecore.common.config.api.CreativeConfig.FloatRange;
import com.creativemd.creativecore.common.config.premade.curve.DecimalCurve;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
	
	public void tick(@Nullable EntityPlayer player) {
		
	}
	
	public boolean isEnabled(@Nullable EntityPlayer player) {
		return enabled && opacity > 0;
	}
	
	@SideOnly(Side.CLIENT)
	public synchronized void playSound(ResourceLocation location) {
		playSound(location, null, 1.0F);
	}
	
	@SideOnly(Side.CLIENT)
	public synchronized void playSound(ResourceLocation location, BlockPos pos) {
		playSound(location, pos, 1.0F);
	}
	
	@SideOnly(Side.CLIENT)
	public synchronized void playSound(ResourceLocation location, float volume) {
		playSound(location, null, volume);
	}
	
	@SideOnly(Side.CLIENT)
	public synchronized void playSound(ResourceLocation location, BlockPos pos, float volume) {
		if (pos != null)
			Minecraft.getMinecraft().getSoundHandler().playSound(new PositionedSound(location, SoundCategory.MASTER, volume, 1, pos));
		else
			Minecraft.getMinecraft().getSoundHandler().playSound(new PositionedSound(location, SoundCategory.MASTER, volume, 1));
	}
	
	@SideOnly(Side.CLIENT)
	public synchronized void playSoundFadeOut(ResourceLocation location, BlockPos pos, DecimalCurve volume) {
		
		if (pos != null)
			Minecraft.getMinecraft().getSoundHandler().playSound(new TickedSound(location, SoundCategory.MASTER, 1, pos, volume));
		else
			Minecraft.getMinecraft().getSoundHandler().playSound(new TickedSound(location, SoundCategory.MASTER, 1, volume));
	}
	
}
