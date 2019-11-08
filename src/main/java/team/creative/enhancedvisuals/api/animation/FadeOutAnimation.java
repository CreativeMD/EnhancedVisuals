package team.creative.enhancedvisuals.api.animation;

import team.creative.enhancedvisuals.api.property.VisualProperties;

public class FadeOutAnimation implements IVisualAnimation {
	
	public final int duration;
	public final int startFade;
	public final int fadeDuration;
	
	public FadeOutAnimation(int duration) {
		this(0, duration);
	}
	
	public FadeOutAnimation(int startFade, int duration) {
		this.startFade = startFade;
		this.duration = duration;
		this.fadeDuration = duration - startFade;
	}
	
	@Override
	public boolean apply(VisualProperties inital, VisualProperties current, int tick) {
		if (tick >= duration)
			return false;
		
		if (tick >= startFade)
			current.opacity = ((tick - startFade) / (float) fadeDuration) * inital.opacity;
		return true;
	}
}
