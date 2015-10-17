package com.sonicjumper.enhancedvisuals.environment;

public abstract class TemperatureFactor {
	public float factor;
	
	/**
	 * Run the current factor tick
	 */
	public abstract boolean runFactor();
	
	/**
	 * The maximum percentage change a factor can make in a single tick
	 */
	public abstract float getFactorRate();
	
	public float getFactor() {
		return factor;
	}
}
