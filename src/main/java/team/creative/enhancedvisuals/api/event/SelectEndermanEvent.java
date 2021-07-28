package team.creative.enhancedvisuals.api.event;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class SelectEndermanEvent extends Event {
    
    public TargetingConditions conditions;
    
    public SelectEndermanEvent(TargetingConditions conditions) {
        this.conditions = conditions;
    }
}
