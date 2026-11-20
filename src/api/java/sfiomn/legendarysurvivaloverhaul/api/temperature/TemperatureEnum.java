package sfiomn.legendarysurvivaloverhaul.api.temperature;

public enum TemperatureEnum
{
    FROSTBITE(5),
    COLD(10),
    NORMAL(20),
    HOT(30),
    HEAT_STROKE(35);

    private final float value;

    TemperatureEnum(float value)
    {
        this.value = value;
    }

    public static TemperatureEnum get(float temperature) {
        if (temperature < (FROSTBITE.value + COLD.value) / 2)
            return FROSTBITE;
        else if (temperature < (COLD.value + NORMAL.value) / 2)
            return COLD;
        else if (temperature < (NORMAL.value + HOT.value) / 2)
            return NORMAL;
        else if (temperature < (HOT.value + HEAT_STROKE.value) / 2)
            return HOT;
        else
            return HEAT_STROKE;
    }

    public float getValue()
    {
        return this.value;
    }
    
    public static float getMin()
    {
        return 0;
    }
    
    public static float getMax()
    {
        return 40;
    }
}