package team.creative.enhancedvisuals.api.event;

import java.util.function.Predicate;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SelectEndermanEvent extends Event {
	
	public Predicate<EntityEnderman> predicate;
	
	public SelectEndermanEvent(Predicate<EntityEnderman> predicate) {
		this.predicate = predicate;
	}
}
