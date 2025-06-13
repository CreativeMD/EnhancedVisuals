package com.stereowalker.survive.api.needs;

public interface Temperature {
    /**
     * @return the temperature of the player on a scale of -1 to 1 with resistances taken into account
     */
    public double getDisplayTemperature();
}