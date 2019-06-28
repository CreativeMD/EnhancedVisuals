package com.sonicjumper.enhancedvisuals.handlers;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.BooleanSegment;
import com.creativemd.igcm.api.segments.FloatSegment;
import com.creativemd.igcm.api.segments.IntegerSegment;
import com.sonicjumper.enhancedvisuals.EnhancedVisuals;
import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;
import java.awt.Color;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Optional.Method;

public class HeartBeatHandler extends VisualHandler {
	private static String USE_HEALTH_PERCENTAGE = "useHealthPercentage";
	private static String HEALTH_LEVEL = "healthLevel";
	private static String HEALTH_LEVEL_PERCENTAGE = "healthLevelPercentage";
	
	private boolean useHealthPercentage = false;
	private int maxHealth = 6;
	private float maxHealthPercentage = 0.3F;
	private float heartBeatIntensity = 50.0F;
	private int heartBeatDuration = 5;
	private int minHeartBeatLength = 15;
	private float heartBeatTimeFactor = 100;
	private float heartBeatVolume = 1F;

	public HeartBeatHandler() {
		super("heart beat", "blur & bloody overlay");
	}
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		heartBeatIntensity = config.getFloat("heartBeatIntensity", name, heartBeatIntensity, 0, 10000, "heartbeat blur intensity");
		heartBeatDuration = config.getInt("heartBeatDuration", name, heartBeatDuration, 0, 10000, "heartbeat blur duration");
		
		useHealthPercentage = config.getBoolean(USE_HEALTH_PERCENTAGE, name, useHealthPercentage, "True means use the maxHealthPercentage to decide when the " + "heartbeat effect occurs; false means use maxHealth");
		maxHealth = config.getInt(HEALTH_LEVEL, name, maxHealth, 0, 100, "Below or equal this amount of health enables the heartbeat effect");
		maxHealthPercentage = config.getFloat(HEALTH_LEVEL_PERCENTAGE, name, maxHealthPercentage, 0F, 1F, "Below or equal this percent of health enables the heartbeat effect");
		minHeartBeatLength = config.getInt("minHeartBeatLength", name, minHeartBeatLength, 0, 100000, "time between heartbeats = percent health remaining * heartBeatHeartFactor + " + "minHeartBeatLength");
		heartBeatTimeFactor = config.getFloat("heartBeatTimeFactor", name, heartBeatTimeFactor, 0, 100000, "time between heartbeats = percent health remaining * heartBeatHeartFactor + " + "minHeartBeatLength");
		
		heartBeatVolume = config.getFloat("heartBeatVolume", name, heartBeatVolume, 0, 1, "How loud the heart beat sounds should be");
	}
	
	@Override
	@Method(modid = "igcm")
	public void registerConfigElements(ConfigBranch branch) {
		branch.registerElement("heartBeatIntensity", new FloatSegment("heartBeatIntensity", 50F, 0, 10000).setToolTip("heartbeat blur intensity"));
		branch.registerElement("heartBeatDuration", new IntegerSegment("heartBeatDuration", 5, 0, 10000).setToolTip("heartbeat blur duration"));
		
		branch.registerElement(USE_HEALTH_PERCENTAGE, new BooleanSegment(USE_HEALTH_PERCENTAGE, false).setToolTip("True means use the maxHealthPercentage to decide when the " + "heartbeat effect occurs; false means use maxHealth"));
		branch.registerElement(HEALTH_LEVEL, new IntegerSegment(HEALTH_LEVEL, 6, 0, 100).setToolTip("Below or equal this amount of health enables the heartbeat effect"));
		branch.registerElement(HEALTH_LEVEL_PERCENTAGE, new FloatSegment(HEALTH_LEVEL_PERCENTAGE, 0.3F, 0.0F, 1.0F).setToolTip("Below or equal this percent of health enables the heartbeat effect"));
		branch.registerElement("minHeartBeatLength", new IntegerSegment("minHeartBeatLength", 15, 0, 100000).setToolTip("time between heartbeats = percent health remaining * heartBeatHeartFactor + " + "minHeartBeatLength"));
		branch.registerElement("heartBeatTimeFactor", new FloatSegment("heartBeatTimeFactor", 5F, 0, 10000).setToolTip("time between heartbeats = percent health remaining * heartBeatHeartFactor + " + "minHeartBeatLength"));
		
		branch.registerElement("heartBeatVolume", new FloatSegment("heartBeatVolume", 1F, 0, 1).setToolTip("How loud the heart beat sounds should be"));
	}
	
	@Override
	@Method(modid = "igcm")
	public void receiveConfigElements(ConfigBranch branch) {
		heartBeatIntensity = (Float) branch.getValue("heartBeatIntensity");
		heartBeatDuration = (Integer) branch.getValue("heartBeatDuration");

		useHealthPercentage = (Boolean) branch.getValue(USE_HEALTH_PERCENTAGE);
		maxHealth = (Integer) branch.getValue(HEALTH_LEVEL);
		maxHealthPercentage = (Float) branch.getValue(HEALTH_LEVEL_PERCENTAGE);
		minHeartBeatLength = (Integer) branch.getValue("minHeartBeatLength");
		heartBeatTimeFactor = (Float) branch.getValue("heartBeatTimeFactor");
		
		heartBeatVolume = (Float) branch.getValue("heartBeatVolume");
	}
	
	public int effectBufferTicks;
	
	@Override
	public void onTick(@Nullable EntityPlayer player) {
		if (shouldHeartbeatTrigger(player)) {
			if (this.effectBufferTicks <= 0) {
				float intensity = getIntensity(player);

				VisualManager.addVisualWithShading(VisualType.lowhealth, Math.min(0.7F, intensity), effectBufferTicks, effectBufferTicks, Color.WHITE);
				VisualManager.addVisualWithShading(VisualType.blur, Math.min(0.7F, intensity) * heartBeatIntensity, heartBeatDuration, heartBeatDuration, Color.WHITE);
				playSound(new ResourceLocation(EnhancedVisuals.modid + ":heartbeatOut"), new BlockPos(player), heartBeatVolume);

				resetBufferTicks(player);
			} else if (this.effectBufferTicks == 5) {
				float intensity = getIntensity(player);

				playSound(new ResourceLocation(EnhancedVisuals.modid + ":heartbeatIn"), new BlockPos(player), heartBeatVolume);
				VisualManager.addVisualWithShading(VisualType.blur, Math.min(0.7F, intensity) * heartBeatIntensity, heartBeatDuration, heartBeatDuration, Color.WHITE);
			}
		}
		this.effectBufferTicks -= 1;
	}

	private void resetBufferTicks(@Nonnull EntityPlayer player) {
		float percentHealthLeft = (player.getHealth() / player.getMaxHealth());
		this.effectBufferTicks = (int) (percentHealthLeft * heartBeatTimeFactor + minHeartBeatLength);
	}

	private float getIntensity(@Nonnull EntityPlayer player) {
		float percentHealthLeft = (player.getHealth() / player.getMaxHealth());
		if(useHealthPercentage) {
			return (maxHealthPercentage - percentHealthLeft) * 2.0F;
		} else {
			return ((maxHealth - player.getHealth()) / player.getMaxHealth()) * 2.0F;
		}
	}

	private boolean shouldHeartbeatTrigger(@Nullable EntityPlayer player) {
		if (player != null) {
			if (useHealthPercentage) {
				float percentageHealth = player.getHealth() / player.getMaxHealth();
				return percentageHealth < maxHealthPercentage;
			} else {
				return player.getHealth() < maxHealth;
			}
		}
		return false;
	}
}
