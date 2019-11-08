package com.sonicjumper.enhancedvisuals.handlers;

import javax.annotation.Nullable;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.FloatSegment;
import com.creativemd.igcm.api.segments.IntegerSegment;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Optional.Method;
import team.creative.enhancedvisuals.client.VisualManager;

public class SandSplatHandler extends VisualHandler {
	
	public SandSplatHandler() {
		super("sand", "walking on sand");
	}
	
	public float defaultmodifier = 0.5F;
	public float sprintingmodifier = 1.5F;
	
	public int minDuration = 100;
	public int maxDuration = 100;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		defaultmodifier = config.getFloat("defaultmodifier", name, 0.5F, 0, 10000, "modifier: splashes per tick = (int) modifier * Math.random()");
		sprintingmodifier = config.getFloat("sprintingmodifier", name, 1.5F, 0, 10000, "sprinting -> increased modifier");
		
		minDuration = config.getInt("minDuration", name, 100, 1, 10000, "min duration of one splash");
		maxDuration = config.getInt("maxDuration", name, 100, 1, 10000, "max duration of one splash");
	}
	
	@Override
	@Method(modid = "igcm")
	public void registerConfigElements(ConfigBranch branch) {
		branch.registerElement("defaultmodifier", new FloatSegment("defaultmodifier", 0.5F, 0, 10000).setToolTip("modifier: splashes per tick = (int) modifier * Math.random()"));
		branch.registerElement("sprintingmodifier", new FloatSegment("sprintingmodifier", 1.5F, 0, 10000).setToolTip("sprinting -> increased modifier"));
		
		branch.registerElement("minDuration", new IntegerSegment("minDuration", 100, 1, 10000).setToolTip("min duration of one splash"));
		branch.registerElement("maxDuration", new IntegerSegment("maxDuration", 100, 1, 10000).setToolTip("max duration of one splash"));
	}
	
	@Override
	@Method(modid = "igcm")
	public void receiveConfigElements(ConfigBranch branch) {
		defaultmodifier = (Float) branch.getValue("defaultmodifier");
		sprintingmodifier = (Float) branch.getValue("sprintingmodifier");
		
		minDuration = (Integer) branch.getValue("minDuration");
		maxDuration = (Integer) branch.getValue("maxDuration");
	}
	
	@Override
	public void onTick(@Nullable EntityPlayer player) {
		if (player != null && player.onGround && isOnSand(player)) {
			float modifier = defaultmodifier;
			if (player.isSprinting())
				modifier = sprintingmodifier;
			VisualManager.addVisualsWithShading(VisualType.sand, (int) (Math.random() * modifier), minDuration, maxDuration);
		}
	}
	
	private boolean isOnSand(EntityPlayer player) {
		BlockPos pos = player.getPosition().down();
		int posX = (int) player.posX;
		int posY = (int) (player.posY - 1);
		int posZ = (int) player.posZ;
		if (player.world.getBlockState(pos).getBlock() == Blocks.SAND) {
			return true;
		}
		return false;
	}
	
}
