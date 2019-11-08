package team.creative.enhancedvisuals.client;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import team.creative.creativecore.common.utils.type.HashMapList;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.common.visual.Visual;

public class VisualManager {
	
	private static HashMapList<VisualCategory, Visual> visuals = new HashMapList<>();
	
	public static void onTick(@Nullable PlayerEntity player) {
		for (Iterator<Visual> iterator = visuals.iterator(); iterator.hasNext();) {
			Visual visual = iterator.next();
			if (!visual.tick()) {
				visual.removeFromDisplay();
				iterator.remove();
			}
		}
	}
	
	public static Iterable<Visual> visuals() {
		return visuals;
	}
	
	public static Collection<Visual> visuals(VisualCategory category) {
		return visuals.get(category);
	}
	
	static void add(Visual visual) {
		visual.addToDisplay();
		visuals.add(visual.getCategory(), visual);
	}
	
	public static void clearVisuals() {
		visuals.clear();
	}
	
}
