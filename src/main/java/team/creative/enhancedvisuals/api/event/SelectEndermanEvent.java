package team.creative.enhancedvisuals.api.event;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class SelectEndermanEvent extends Event implements ICancellableEvent {
    
    public TargetingConditions conditions;
    
    public SelectEndermanEvent(TargetingConditions conditions) {
        this.conditions = conditions;
    }
}
