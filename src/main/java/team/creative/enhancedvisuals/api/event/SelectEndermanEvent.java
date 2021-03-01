package team.creative.enhancedvisuals.api.event;

import net.minecraft.entity.EntityPredicate;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class SelectEndermanEvent extends Event {
    
    public EntityPredicate predicate;
    
    public SelectEndermanEvent(EntityPredicate predicate) {
        this.predicate = predicate;
    }
}
