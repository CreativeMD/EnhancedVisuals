package com.charles445.simpledifficulty.api.temperature;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public interface ITemperatureCapability
{
	public int getTemperatureLevel();
	public int getTemperatureTickTimer();
	public ImmutableMap<String, TemporaryModifier> getTemporaryModifiers();
	
	public void setTemperatureLevel(int temperature);
	public void setTemperatureTickTimer(int ticktimer);
	public void setTemporaryModifier(String name, float temperature, int duration);
	
	public void addTemperatureLevel(int temperature);
	public void addTemperatureTickTimer(int ticktimer);
	
	public void clearTemporaryModifiers();
	
	/**
	 * Returns the capability's matching TemperatureEnum enum
	 * @return TemperatureEnum for the temperature
	 */
	public TemperatureEnum getTemperatureEnum();
	
	/**
	 * (Don't use this!) <br>
	 * Runs a tick update for the player's temperature capability
	 * @param player
	 * @param world
	 * @param phase
	 */
	public void tickUpdate(EntityPlayer player, World world, TickEvent.Phase phase);
	
	/**
	 * (Don't use this!) <br>
	 * Checks if the capability needs an update
	 * @return boolean has temperature changed
	 */
	public boolean isDirty();
	/**
	 * (Don't use this!) <br>
	 * Sets the capability as updated
	 */
	public void setClean();
	
	/**
	 * (Don't use this!) <br>
	 * Gets the current tick of the packet timer
	 * @return int packetTimer
	 */
	public int getPacketTimer();
}
