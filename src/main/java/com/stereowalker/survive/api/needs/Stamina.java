package com.stereowalker.survive.api.needs;

public interface Stamina {
    /**
     * @return Whether or not the player has burnt through their short term stamina and is
     * waiting for it to regenerate
     */
    public boolean isShortOfBreath();
    
    /**
     * @return Whether or not the player is out of stamina and is on their reserves
     */
    public boolean isDeadTired();
    /**
     * @return Get's the current amount of burst stamina the player has.
     * It's indicated by the green lightning above the yellow one
     */
    public int getBurstStamina();
}