package sfiomn.legendarysurvivaloverhaul.api.thirst;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Interface for thirst capability
 */
public interface IThirstAttachment
{
    float getThirstExhaustion();

    void setExhaustion(float exhaustion);

    int getHydrationLevel();

    void setHydrationLevel(int thirst);

    float getSaturationLevel();

    int getTickTimer();

    void setTickTimer(int ticktimer);

    int getThirstDamageTickTimer();

    void setThirstDamageTickTimer(int ticktimer);

    int getThirstDamageCounter();

    void setThirstDamageCounter(int damagecounter);

    void setSaturation(float saturation);

    void addThirstExhaustion(float exhaustion);

    void addHydrationLevel(int thirst);

    void addSaturationLevel(float saturation);

    void addTickTimer(int ticktimer);

    void addThirstDamageTickTimer(int ticktimer);

    void addThirstDamageCounter(int damagecounter);

    /**
     * Check whether the hydration level is at maximum or not
     * <br>
     *
     * @return boolean hydration is at maximum
     */
    boolean isHydrationLevelAtMax();

    boolean hasShownBlurWarning();

    void setShownBlurWarning(boolean shown);

    /**
     * (Don't use this!) <br>
     * Checks if the capability needs an update
     *
     * @return boolean has thirst changed
     */
    boolean isDirty();

    /**
     * (Don't use this!) <br>
     * Sets the capability as updated
     */
    void setClean();

    /**
     * Force the synchronization server - client
     * of the thirst capability
     */
    void setDirty();

    /**
     * (Don't use this!) <br>
     * Runs a tick update for the player's thirst capability
     *
     * @param player
     * @param world
     * @param isStart
     */
    void tickUpdate(Player player, Level world, boolean isStart);

    /**
     * (Don't use this!) <br>
     * Gets the current tick of the packet timer
     *
     * @return int packetTimer
     */
    int getPacketTimer();
}