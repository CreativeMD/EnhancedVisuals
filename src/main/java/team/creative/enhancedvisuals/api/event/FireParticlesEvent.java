package team.creative.enhancedvisuals.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class FireParticlesEvent extends Event {
	
	public final int fireSplashes;
	public final int fireDurationMin;
	public final int fireDurationMax;
	
	protected int newFireSplashes;
	protected int newFireDurationMin;
	protected int newFireDurationMax;
	
	public FireParticlesEvent(int fireSplashes, int fireDurationMin, int fireDurationMax) {
		this.fireSplashes = this.newFireSplashes = fireSplashes;
		this.fireDurationMin = this.newFireDurationMin = fireDurationMin;
		this.fireDurationMax = this.newFireDurationMax = fireDurationMax;
	}
	
	public void setFireSplashes(int count) {
		newFireSplashes = count;
	}
	
	public void setDuration(int min, int max) {
		newFireDurationMin = min;
		newFireDurationMax = max;
	}
	
	public int getNewFireSplashes() {
		return newFireSplashes;
	}
	
	public int getNewFireDurationMin() {
		return newFireDurationMin;
	}
	
	public int getNewFireDurationMax() {
		return newFireDurationMax;
	}
	
}
