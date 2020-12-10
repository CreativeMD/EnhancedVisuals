package team.creative.enhancedvisuals.common.death;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.api.ICreativeConfig;

public class DeathMessages implements ICreativeConfig {
	
	@CreativeConfig
	public List<String> deathMessages = Arrays.asList("Do you really want to respawn? think of it again.", "Life is hard. Deal with it!", "You are dead ... wait you already know that.", "Did I let the stove on...?", "Should have shot back first...", "Yep, that's messed up...", "Rage incomming!", "I think you dropped something.", "Time for a break?");
	
	public boolean enabled = true;
	
	private Random rand = new Random();
	
	public String pickRandomDeathMessage() {
		if (deathMessages.size() == 0)
			return null;
		return deathMessages.get(rand.nextInt(deathMessages.size() - 1));
	}
	
	@Override
	public void configured() {
		
	}
	
}
