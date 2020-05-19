package com.charles445.simpledifficulty.api.temperature;

import net.minecraft.util.math.MathHelper;

public enum TemperatureEnum
{
	//Must be in order
	//temperature <= threshold is a match
	//For example, HOT = 15 - 19
	
	FREEZING(0,5),
	COLD(6,10),
	NORMAL(11,14),
	HOT(15,19),
	BURNING(20,25);
	
	private int lowerBound;
	private int upperBound;
	
	private TemperatureEnum(int lowerBound, int upperBound)
	{
		this.lowerBound=lowerBound;
		this.upperBound=upperBound;
	}

	/**
	 * Returns whether the given temperature fits in the enum's boundaries
	 * @param temperature
	 * @return boolean
	 * <br>
	 */
	public boolean matches(int temperature)
	{
		return (temperature>=this.lowerBound && temperature<=this.upperBound);
	}
	
	public int getMiddle()
	{
		return (this.upperBound + this.lowerBound)/2;
	}
	
	public int getLowerBound()
	{
		return this.lowerBound;
	}
	
	public int getUpperBound()
	{
		return this.upperBound;
	}
}
