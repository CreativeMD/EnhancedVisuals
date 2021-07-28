package team.creative.enhancedvisuals.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class SplashEvent extends Event {
    
    public final Player player;
    
    public SplashEvent(Player player) {
        this.player = player;
    }
    
}
