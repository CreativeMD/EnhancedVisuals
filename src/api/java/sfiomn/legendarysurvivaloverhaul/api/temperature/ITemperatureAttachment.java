package sfiomn.legendarysurvivaloverhaul.api.temperature;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public interface ITemperatureAttachment
{
    float getTemperatureLevel();

    void setTemperatureLevel(float temperature);

    float getTargetTemperatureLevel();

    void setTargetTemperatureLevel(float targetTemperature);

    int getTemperatureTickTimer();

    void setTemperatureTickTimer(int tickTimer);

    int getFreezeTickTimer();

    void setFreezeTickTimer(int tickTimer);

    TemperatureEnum getTemperatureEnum();

    List<Integer> getTemperatureImmunities();

    void addTemperatureLevel(float temperature);

    void addTemperatureTickTimer(int tickTimer);

    void addFreezeTickTimer(int tickTimer);

    void addTemperatureImmunityId(int immunityId);

    void removeTemperatureImmunityId(int immunityId);

    /**
     * (Don't use this!) <br>
     * Runs a tick update for the player's temperature capability
     *
     * @param player
     * @param world
     * @param isStart
     */
    void tickUpdate(Player player, Level world, boolean isStart);

    /**
     * (Don't use this!) <br>
     * Runs a tick on client side for the player's temperature capability
     *
     * @param player
     * @param isStart
     */
    void tickClient(Player player, boolean isStart);

    /**
     * (Don't use this!) <br>
     * Checks if the capability needs an update
     *
     * @return boolean has temperature changed
     */
    boolean isDirty();

    /**
     * (Don't use this!) <br>
     * Sets the capability as updated
     */
    void setClean();

    /**
     * (Don't use this!) <br>
     * Gets the current tick of the packet timer
     *
     * @return int packetTimer
     */
    int getPacketTimer();
}