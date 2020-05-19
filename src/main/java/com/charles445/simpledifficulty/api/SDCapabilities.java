package com.charles445.simpledifficulty.api;

import com.charles445.simpledifficulty.api.temperature.ITemperatureCapability;
import com.charles445.simpledifficulty.api.thirst.IThirstCapability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class SDCapabilities
{
	@CapabilityInject(ITemperatureCapability.class)
	public static final Capability<ITemperatureCapability> TEMPERATURE = null;
	
	public static final String TEMPERATURE_IDENTIFIER = "temperature";
	
	@CapabilityInject(IThirstCapability.class)
	public static final Capability<IThirstCapability> THIRST = null;
	
	public static final String THIRST_IDENTIFIER = "thirst";
	
	/**
	 * Gets the temperature capability for a player
	 * @param player
	 * @return ITemperatureCapability
	 */
	public static ITemperatureCapability getTemperatureData(EntityPlayer player)
	{
		return player.getCapability(TEMPERATURE, null);
	}
	
	/**
	 * Gets the thirst capability for a player
	 * @param player
	 * @return IThirsteCapability
	 */
	public static IThirstCapability getThirstData(EntityPlayer player)
	{
		return player.getCapability(THIRST, null);
	}
}
