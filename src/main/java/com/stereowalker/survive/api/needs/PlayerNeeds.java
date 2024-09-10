package com.stereowalker.survive.api.needs;

import net.minecraft.world.entity.LivingEntity;

public abstract class PlayerNeeds {
    
    public abstract Temperature getTemperature(LivingEntity entity);
    
    public abstract Stamina getStamina(LivingEntity entity);
    
    public abstract Water getWater(LivingEntity entity);
    
    private static boolean hasSetApi = false;
    private static PlayerNeeds needsApi;
    
    public static PlayerNeeds api() {
        return needsApi;
    }
    
    public static void setImpl(PlayerNeeds api) throws UnsupportedOperationException {
        if (hasSetApi)
            throw new UnsupportedOperationException("An implementation for the needs api has already been set");
        needsApi = api;
        hasSetApi = true;
    }
}