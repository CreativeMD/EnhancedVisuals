package team.creative.enhancedvisuals.api.animation;

import team.creative.enhancedvisuals.api.property.VisualProperties;

public interface IVisualAnimation<T extends VisualProperties> {
	
	/**
	 * @param inital
	 *            properties
	 * @param current
	 *            properties (to apply animation to)
	 * @param tick
	 * @return false if the animation is over
	 */
	public boolean apply(T inital, T current, int tick);
	
	public default boolean visible(T current) {
		return current.opacity > 0;
	}
	
}
