package team.creative.enhancedvisuals.common.handler;

import java.util.Random;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.premade.DecimalMinMax;
import com.creativemd.creativecore.common.config.premade.IntMinMax;
import com.creativemd.creativecore.common.config.premade.curve.DecimalCurve;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.event.SplashEvent;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeBlur;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.common.event.EVEvents;

public class SplashHandler extends VisualHandler {
	
	@CreativeConfig
	public IntMinMax duration = new IntMinMax(10, 10);
	
	@CreativeConfig
	public DecimalMinMax intensity = new DecimalMinMax(5, 10);
	
	@CreativeConfig
	public VisualType blur = new VisualTypeBlur("blur");
	
	public boolean wasInWater = false;
	
	public Random rand = new Random();
	
	@Override
	public void tick(@Nullable EntityPlayer player) {
		if (player != null) {
			boolean isInWater = EVEvents.areEyesInWater(player);
			if (isInWater != wasInWater) {
				SplashEvent event = new SplashEvent(player);
				MinecraftForge.EVENT_BUS.post(event);
				if (!event.isCanceled())
					VisualManager.addVisualFadeOut(blur, new DecimalCurve(rand, duration, intensity));
			}
			wasInWater = isInWater;
			
		}
	}
	
}
