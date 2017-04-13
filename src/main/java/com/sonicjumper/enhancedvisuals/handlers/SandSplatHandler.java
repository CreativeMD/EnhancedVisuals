package com.sonicjumper.enhancedvisuals.handlers;

import javax.annotation.Nullable;

import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;

public class SandSplatHandler extends VisualHandler {

	public SandSplatHandler() {
		super("sand", "walking on sand");
	}
	
	public float defaultmodifier = 0.5F;
	public float sprintingmodifier = 1.5F;
	
	public int maxDuration = 100;
	public int minDuration = 100;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		defaultmodifier = config.getFloat("defaultmodifier", name, 0.5F, 0, 10000, "modifier: splashes per tick = (int) modifier * Math.random()");
		sprintingmodifier = config.getFloat("sprintingmodifier", name, 1.5F, 0, 10000, "sprinting -> increased modifier");
		
		maxDuration = config.getInt("maxDuration", name, 100, 1, 10000, "max duration of one splash");
		minDuration = config.getInt("minDuration", name, 100, 1, 10000, "min duration of one splash");
	}
	
	@Override
	public void onTick(@Nullable EntityPlayer player)
	{
		if(player != null && player.onGround && isOnSand(player))
	    {
	    	float modifier = defaultmodifier;
			if (player.isSprinting())
				modifier = sprintingmodifier;
	    	VisualManager.addVisualsWithShading(VisualType.sand, (int) (Math.random()*modifier), minDuration, maxDuration);
	    }
	}
	
	private boolean isOnSand(EntityPlayer player)
	{
		BlockPos pos = player.getPosition().down();
		int posX = (int)player.posX;
		int posY = (int)(player.posY - 1);
		int posZ = (int)player.posZ;
	    if (player.world.getBlockState(pos).getBlock() == Blocks.SAND) {
	    	return true;
	    }
	    return false;
	}

}
