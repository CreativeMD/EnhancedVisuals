package com.sonicjumper.enhancedvisuals.death;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.minecraftforge.common.config.Configuration;

public class DeathMessages {
	
	public static ArrayList<String> deathMessages = new ArrayList<>();
	
	private static Random rand = new Random();
	
	static{
		//Init default messages
		deathMessages.add("Do you really want to respawn? think of it again.");
		deathMessages.add("Life is hard. Deal with it!");
		deathMessages.add("You are dead ... wait you already know that.");
		deathMessages.add("Did I let the stove on...?");
		deathMessages.add("Should have shot back first...");
		deathMessages.add("Yep, that's messed up...");
		deathMessages.add("Rage incomming!");
		deathMessages.add("I think you dropped something.");
		deathMessages.add("Time for a break?");
	}
	
	public static void loadConfig(Configuration config)
	{
		deathMessages = new ArrayList(Arrays.asList(config.getStringList("deathMessages", "Messages", deathMessages.toArray(new String[0]), "")));
	}
	
	public static String pickRandomDeathMessage()
	{
		return deathMessages.get(rand.nextInt(deathMessages.size()-1));
	}
	
}
