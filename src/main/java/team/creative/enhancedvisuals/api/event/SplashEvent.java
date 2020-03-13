package team.creative.enhancedvisuals.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SplashEvent extends Event {
	
	public final EntityPlayer player;
	
	public SplashEvent(EntityPlayer player) {
		this.player = player;
	}
	
}
