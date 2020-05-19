package com.charles445.simpledifficulty.api.thirst;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public interface IThirstCapability
{
	public float getThirstExhaustion();
	public int getThirstLevel();
	public float getThirstSaturation();
	public int getThirstTickTimer();
	
	public void setThirstExhaustion(float exhaustion);
	public void setThirstLevel(int thirst);
	public void setThirstSaturation(float saturation);
	public void setThirstTickTimer(int ticktimer);
	
	public void addThirstExhaustion(float exhaustion);
	public void addThirstLevel(int thirst);
	public void addThirstSaturation(float saturation);
	public void addThirstTickTimer(int ticktimer);

	/**
	 * Check whether the thirst level isn't maximum
	 * <br>
	 * Not to be confused with the "Thirsty" effect!
	 * @return boolean thirst isn't maximum
	 */
	public boolean isThirsty();
	
	/**
	 * (Don't use this!) <br>
	 * Checks if the capability needs an update
	 * @return boolean has thirst changed
	 */
	public boolean isDirty();
	
	/**
	 * (Don't use this!) <br>
	 * Sets the capability as updated
	 */
	public void setClean();
	
	/**
	 * (Don't use this!) <br>
	 * Runs a tick update for the player's thirst capability
	 * @param player
	 * @param world
	 * @param phase
	 */
	public void tickUpdate(EntityPlayer player, World world, TickEvent.Phase phase);
	

	/**
	 * (Don't use this!) <br>
	 * Gets the current tick of the packet timer
	 * @return int packetTimer
	 */
	public int getPacketTimer();
}
