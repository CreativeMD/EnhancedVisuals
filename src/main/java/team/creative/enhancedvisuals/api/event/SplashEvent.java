package team.creative.enhancedvisuals.api.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class SplashEvent extends Event implements ICancellableEvent {
    
    public final Player player;
    
    public SplashEvent(Player player) {
        this.player = player;
    }
    
}
