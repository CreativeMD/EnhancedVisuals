package com.sonicjumper.enhancedvisuals.environment;

import com.sonicjumper.enhancedvisuals.event.VisualEventHandler;

import net.minecraft.client.Minecraft;

public abstract class BaseEnvironmentEffect {
	protected VisualEventHandler parent;
	
	public BaseEnvironmentEffect(VisualEventHandler veh) {
		parent = veh;
	}

	public abstract void onTick();

	public abstract void resetEffect();
}
