package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;

import java.util.List;

public interface ITemperatureCapability
{
	public float getTemperatureLevel();
	public float getTargetTemperatureLevel();
	public int getTemperatureTickTimer();
	public int getFreezeTickTimer();
	public TemperatureEnum getTemperatureEnum();
	public List<Integer> getTemperatureImmunities();
	
	public void setTemperatureLevel(float temperature);
	public void setTargetTemperatureLevel(float targetTemperature);
	public void setTemperatureTickTimer(int tickTimer);
	public void setFreezeTickTimer(int tickTimer);
	
	public void addTemperatureLevel(float temperature);
	public void addTemperatureTickTimer(int tickTimer);
	public void addFreezeTickTimer(int tickTimer);
	public void addTemperatureImmunityId(int immunityId);
	public void removeTemperatureImmunityId(int immunityId);
	
	/**
	 * (Don't use this!) <br>
	 * Runs a tick update for the player's temperature capability
	 * @param player
	 * @param world
	 * @param phase
	 */
	public void tickUpdate(Player player, Level world, TickEvent.Phase phase);

	/**
	 * (Don't use this!) <br>
	 * Runs a tick on client side for the player's temperature capability
	 * @param player
	 * @param phase
	 */
	public void tickClient(Player player, TickEvent.Phase phase);
	
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