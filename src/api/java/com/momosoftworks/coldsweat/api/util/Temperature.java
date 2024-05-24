package com.momosoftworks.coldsweat.api.util;

import net.minecraft.world.entity.LivingEntity;

/** just a dummy class */
public class Temperature {
    
    public static double convert(double value, Units from, Units to, boolean absolute) {
        return value;
    }
    
    public static double get(LivingEntity entity, Trait trait) {
        return 0;
    }
    
    public enum Trait {
        BODY
    }
    
    public enum Units {
        F,
        C,
        MC;
    }
}
