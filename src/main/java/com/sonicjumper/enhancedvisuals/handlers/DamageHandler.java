package com.sonicjumper.enhancedvisuals.handlers;

import java.awt.Color;
import java.util.ArrayList;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.BooleanSegment;
import com.creativemd.igcm.api.segments.FloatSegment;
import com.creativemd.igcm.api.segments.IntegerSegment;
import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Optional.Method;

public class DamageHandler extends VisualHandler {
	
	public DamageHandler() {
		super("damage", "when the player is taking damage");
	}
	
	public final ArrayList<Item> sharpList = new ArrayList<>();
	public final ArrayList<Item> bluntList = new ArrayList<>();
	public final ArrayList<Item> pierceList = new ArrayList<>();
	
	public static boolean hitEffect = false;
	
	public static float hitEffectIntensity = 1.0F;
	public static int hitEffectMinDuration = 1;
	public static int hitEffectMaxDuration = 10;
	
	public int fireSplashes = 1;
	public int fireMinDuration = 100;
	public int fireMaxDuration = 1000;
	
	public int drownSplashes = 4;
	
	public int drownMinDuration = 10;
	public int drownMaxDuration = 15;
	
	public int bloodDurationMin = 500;
	public int bloodDurationMax = 1500;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		
		sharpList.add(Items.IRON_SWORD);
		sharpList.add(Items.WOODEN_SWORD);
		sharpList.add(Items.STONE_SWORD);
		sharpList.add(Items.DIAMOND_SWORD);
		sharpList.add(Items.GOLDEN_SWORD);
		sharpList.add(Items.IRON_AXE);
		sharpList.add(Items.WOODEN_AXE);
		sharpList.add(Items.STONE_AXE);
		sharpList.add(Items.DIAMOND_AXE);
		sharpList.add(Items.GOLDEN_AXE);
		
		bluntList.add(Items.IRON_PICKAXE);
		bluntList.add(Items.WOODEN_PICKAXE);
		bluntList.add(Items.STONE_PICKAXE);
		bluntList.add(Items.DIAMOND_PICKAXE);
		bluntList.add(Items.GOLDEN_PICKAXE);
		bluntList.add(Items.IRON_SHOVEL);
		bluntList.add(Items.WOODEN_SHOVEL);
		bluntList.add(Items.STONE_SHOVEL);
		bluntList.add(Items.DIAMOND_SHOVEL);
		bluntList.add(Items.GOLDEN_SHOVEL);
		
		pierceList.add(Items.IRON_HOE);
		pierceList.add(Items.WOODEN_HOE);
		pierceList.add(Items.STONE_HOE);
		pierceList.add(Items.DIAMOND_HOE);
		pierceList.add(Items.GOLDEN_HOE);
		pierceList.add(Items.ARROW);
		
		hitEffect = config.getBoolean("hitEffect", name, hitEffect, "Red overlay effect once you get hit");
		hitEffectIntensity = config.getFloat("hitEffectIntensity", name, hitEffectIntensity, 0, 1, "Intensity of red overlay");
		hitEffectMinDuration = config.getInt("hitEffectMinDuration", name, hitEffectMinDuration, 1, 100000, "min duration of hit effect");
		hitEffectMaxDuration = config.getInt("hitEffectMaxDuration", name, hitEffectMaxDuration, 1, 100000, "max duration of hit effect");
		
		fireSplashes = config.getInt("fireSplashes", name, fireSplashes, 0, 10000, "splashes per tick");
		fireMinDuration = config.getInt("fireMinDuration", name, fireMinDuration, 1, 10000, "min duration of one particle");
		fireMaxDuration = config.getInt("fireMaxDuration", name, fireMaxDuration, 1, 10000, "max duration of one particle");
		
		drownSplashes = config.getInt("drownSplashes", name, drownSplashes, 0, 10000, "splashes per hit");
		drownMinDuration = config.getInt("drownMinDuration", name, drownMinDuration, 1, 10000, "min duration of one splash");
		drownMaxDuration = config.getInt("drownMaxDuration", name, drownMaxDuration, 1, 10000, "max duration of one splash");
		
		bloodDurationMin = config.getInt("bloodDurationMin", name, bloodDurationMin, 1, 100000, "min duration of blood splash");
		bloodDurationMax = config.getInt("bloodDurationMax", name, bloodDurationMax, 1, 100000, "max duration of blood splash");
		
	}
	
	@Override
	@Method(modid = "igcm")
	public void registerConfigElements(ConfigBranch branch) {
		branch.registerElement("hitEffect", new BooleanSegment("hitEffect", false).setToolTip("Red overlay effect once you get hit"));
		branch.registerElement("hitEffectIntensity", new FloatSegment("hitEffectIntensity", 1F, 0, 1).setToolTip("Intensity of red overlay"));
		branch.registerElement("hitEffectMinDuration", new IntegerSegment("hitEffectMinDuration", 1, 1, 10000).setToolTip("min duration of hit effect"));
		branch.registerElement("hitEffectMaxDuration", new IntegerSegment("hitEffectMaxDuration", 10, 1, 10000).setToolTip("max duration of hit effecte"));
		
		branch.registerElement("fireSplashes", new IntegerSegment("fireSplashes", 1, 0, 10000).setToolTip("splashes per tick"));
		branch.registerElement("fireMinDuration", new IntegerSegment("fireMinDuration", 100, 0, 10000).setToolTip("min duration of one particle"));
		branch.registerElement("fireMaxDuration", new IntegerSegment("fireMaxDuration", 1000, 0, 10000).setToolTip("max duration of one particle"));
		
		branch.registerElement("drownSplashes", new IntegerSegment("drownSplashes", 4, 0, 10000).setToolTip("splashes per tick"));
		branch.registerElement("drownMinDuration", new IntegerSegment("drownMinDuration", 10, 0, 10000).setToolTip("min duration of one splash"));
		branch.registerElement("drownMaxDuration", new IntegerSegment("drownMaxDuration", 15, 0, 10000).setToolTip("max duration of one splash"));
		
		branch.registerElement("bloodDurationMin", new IntegerSegment("bloodDurationMin", 500, 0, 100000).setToolTip("min duration of blood slpash"));
		branch.registerElement("bloodDurationMax", new IntegerSegment("bloodDurationMax", 1500, 0, 100000).setToolTip("max duration of blood slpash"));
	}
	
	@Override
	@Method(modid = "igcm")
	public void receiveConfigElements(ConfigBranch branch) {
		hitEffect = (Boolean) branch.getValue("hitEffect");
		hitEffectIntensity = (Float) branch.getValue("hitEffectIntensity");
		hitEffectMinDuration = (Integer) branch.getValue("hitEffectMinDuration");
		hitEffectMaxDuration = (Integer) branch.getValue("hitEffectMaxDuration");
		
		fireSplashes = (Integer) branch.getValue("fireSplashes");
		fireMinDuration = (Integer) branch.getValue("fireMinDuration");
		fireMaxDuration = (Integer) branch.getValue("fireMaxDuration");
		
		drownSplashes = (Integer) branch.getValue("drownSplashes");
		drownMinDuration = (Integer) branch.getValue("drownMinDuration");
		drownMaxDuration = (Integer) branch.getValue("drownMaxDuration");
		
		bloodDurationMin = (Integer) branch.getValue("bloodDurationMin");
		bloodDurationMax = (Integer) branch.getValue("bloodDurationMax");
	}
	
	@Override
	public void onPlayerDamaged(EntityPlayer player, DamageSource source, float damage) {
		Entity attacker = source.getImmediateSource();
		
		double distanceSq = 1;
		if (attacker instanceof EntityArrow)
			VisualManager.createVisualFromDamageAndDistance(VisualType.pierce, damage, player, distanceSq, bloodDurationMin, bloodDurationMax);
		
		if (attacker instanceof EntityLivingBase) {
			EntityLivingBase lastAttacker = (EntityLivingBase) attacker;
			// Check weapons
			if (lastAttacker.getHeldItemMainhand() != null) {
				if (isSharp(lastAttacker.getHeldItemMainhand().getItem())) {
					VisualManager.createVisualFromDamageAndDistance(VisualType.slash, damage, player, distanceSq, bloodDurationMin, bloodDurationMax);
				} else if (isBlunt(lastAttacker.getHeldItemMainhand().getItem())) {
					VisualManager.createVisualFromDamageAndDistance(VisualType.impact, damage, player, distanceSq, bloodDurationMin, bloodDurationMax);
				} else if (isPierce(lastAttacker.getHeldItemMainhand().getItem())) {
					VisualManager.createVisualFromDamageAndDistance(VisualType.pierce, damage, player, distanceSq, bloodDurationMin, bloodDurationMax);
				} else {
					// Default to splatter type
					VisualManager.createVisualFromDamageAndDistance(VisualType.splatter, damage, player, distanceSq, bloodDurationMin, bloodDurationMax);
				}
			} else {
				// If player received fall damage					
				if (lastAttacker instanceof EntityZombie || lastAttacker instanceof EntitySkeleton || lastAttacker instanceof EntityOcelot) {
					VisualManager.createVisualFromDamageAndDistance(VisualType.slash, damage, player, distanceSq, bloodDurationMin, bloodDurationMax);
				} else if (lastAttacker instanceof EntityGolem || lastAttacker instanceof EntityPlayer) {
					VisualManager.createVisualFromDamageAndDistance(VisualType.impact, damage, player, distanceSq, bloodDurationMin, bloodDurationMax);
				} else if (lastAttacker instanceof EntityWolf || lastAttacker instanceof EntitySpider) {
					VisualManager.createVisualFromDamageAndDistance(VisualType.pierce, damage, player, distanceSq, bloodDurationMin, bloodDurationMax);
				}
				
			}
		}
		
		if (source == DamageSource.CACTUS) {
			VisualManager.createVisualFromDamageAndDistance(VisualType.pierce, damage, player, distanceSq, bloodDurationMin, bloodDurationMax);
		}
		
		if (source == DamageSource.FALL || source == DamageSource.FALLING_BLOCK) {
			VisualManager.createVisualFromDamageAndDistance(VisualType.impact, damage, player, distanceSq, bloodDurationMin, bloodDurationMax);
		}
		
		if (source.equals(DamageSource.DROWN)) {
			VisualManager.addVisualsWithShading(VisualType.waterS, 1F, drownSplashes, drownMinDuration, drownMaxDuration, new Color(1.0F, 1.0F, 1.0F, 1.0F));
		}
		
		if (source.isFireDamage() || source == DamageSource.ON_FIRE)
			VisualManager.addVisualsWithShading(VisualType.fire, 1F, fireSplashes, fireMinDuration, fireMaxDuration, new Color(1, 1, 1));
		
	}
	
	private boolean isSharp(Item item) {
		return sharpList.contains(item);
	}
	
	private boolean isBlunt(Item item) {
		return bluntList.contains(item);
	}
	
	private boolean isPierce(Item item) {
		return pierceList.contains(item);
	}
}
