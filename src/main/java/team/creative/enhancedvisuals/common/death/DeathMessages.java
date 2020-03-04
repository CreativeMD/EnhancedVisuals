package team.creative.enhancedvisuals.common.death;

import java.util.Random;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.api.ICreativeConfig;

public class DeathMessages implements ICreativeConfig {
	
	@CreativeConfig
	public String[] deathMessages = new String[] { "Do you really want to respawn? think of it again.", "Life is hard. Deal with it!", "You are dead ... wait you already know that.", "Did I let the stove on...?", "Should have shot back first...", "Yep, that's messed up...", "Rage incomming!", "I think you dropped something.", "Time for a break?" };
	
	public boolean enabled = true;
	
	private Random rand = new Random();
	
	public String pickRandomDeathMessage() {
		if (deathMessages.length == 0)
			return null;
		return deathMessages[rand.nextInt(deathMessages.length - 1)];
	}
	
	@Override
	public void configured() {
		
	}
	
}
