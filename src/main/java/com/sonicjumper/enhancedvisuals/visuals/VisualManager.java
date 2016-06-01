package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySquid;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.visuals.Visual.VisualCatagory;

public class VisualManager {
	private ArrayList<Visual> playerVisuals = new ArrayList<Visual>();
	private ArrayList<Visual> permVisuals = new ArrayList<Visual>();
	
	//private Overlay heatOverlay;
	//private Overlay iceOverlay;
	//private Overlay wetOverlay;
	public Animation slenderOverlay;
	public ShaderDesaturate desaturate;
	//public Shader blur;

	private Random rand = new Random();

	public VisualManager() {
		//heatOverlay = new Overlay(VisualType.heat, -1, new Color(1.0F, 1.0F, 1.0F, 0.0F));
		//iceOverlay = new Overlay(VisualType.ice, -1, new Color(1.0F, 1.0F, 1.0F, 0.0F));
		//wetOverlay = new Overlay(VisualType.waterO, -1, new Color(1.0F, 1.0F, 1.0F, 0.0F));
		slenderOverlay = new Animation(VisualType.slender, -1, new Color(1.0F, 1.0F, 1.0F), 100);
		desaturate = new ShaderDesaturate(VisualType.desaturate, -1);
		//blur = new ShaderBlurFade(VisualType.blur, -1, 0);
		
		//permVisuals.add(heatOverlay);
		//if(VisualType.ice.enabled)
			//permVisuals.add(iceOverlay);
		//if(VisualType.waterO.enabled)
			//permVisuals.add(wetOverlay);
		if(VisualType.slender.enabled)
			permVisuals.add(slenderOverlay);
		if(VisualType.desaturate.enabled)
			permVisuals.add(desaturate);
		//permVisuals.add(blur);
	}

	public ArrayList<Visual> getActiveVisuals() {
		ArrayList<Visual> alv = new ArrayList<Visual>();
		alv.addAll(playerVisuals);
		alv.addAll(permVisuals);
		return alv;
	}
	
	public ArrayList<Shader> getActiveShaders() {
		ArrayList<Shader> shaders = new ArrayList<Shader>();
		for(Visual v : playerVisuals) {
			if(v instanceof Shader) {
				shaders.add((Shader) v);
			}
		}
		return shaders;
	}

	public void removeVisual(Visual v) {
		v.beingRemoved();
		playerVisuals.remove(v);
	}

	public void clearAllVisuals() {
		playerVisuals.clear();
	}

	public void addVisuals(VisualType vt, int num, int minTime, int maxTime) {
		addVisualsWithShading(vt, num, minTime, maxTime, Color.WHITE);
	}

	public void addVisualDirect(Visual v) {
		if(v.getType().enabled)
			playerVisuals.add(v);
	}
	
	public void addRandomNumVisualsWithColor(VisualType vt, int minNum, int maxNum, int minTime, int maxTime, Color color) {
		if(maxNum <= minNum) {
			addVisualsWithShading(vt, minNum, minTime, maxTime, color);
		} else {
			addVisualsWithShading(vt, minNum + rand.nextInt(maxNum - minNum), minTime, maxTime, color);
		}
	}

	public void addVisualsWithShading(VisualType vt, int num, int minTime, int maxTime, Color color) {
		if(!vt.enabled)
			return ;
		for(int i = 0; i < num; i++) {
			Visual v;
			if(vt.getCatagory().ordinal() == VisualCatagory.Animation.ordinal()) {
				v = minTime == maxTime ? new Animation(vt, minTime, color) : new Animation(vt, minTime + rand.nextInt(maxTime - minTime), color);
			} else if(vt.getCatagory().ordinal() == VisualCatagory.Overlay.ordinal()) {
				v = minTime == maxTime ? new Overlay(vt, minTime, color) : new Overlay(vt, minTime + rand.nextInt(maxTime - minTime), color);
			} else if(vt.getCatagory().ordinal() == VisualCatagory.Splat.ordinal()) {
				v = minTime == maxTime ? new Splat(vt, minTime, color) : new Splat(vt, minTime + rand.nextInt(maxTime - minTime), color);
			} else {
				v = minTime == maxTime ? new Visual(vt, minTime, color) : new Visual(vt, minTime + rand.nextInt(maxTime - minTime), color);
			}
			playerVisuals.add(v);
		}
	}

	public void createVisualFromDamage(VisualType type, float damage, EntityLivingBase bleedingEntity) {
		createVisualFromDamageAndDistance(type, damage, bleedingEntity, 1.0D);
	}

	public void createVisualFromDamageAndDistance(VisualType type, float damage, EntityLivingBase bleedingEntity, double distanceSqToEntity) {
		if(damage <= 0.0F) {
			return;
		}

		double distance = Math.sqrt(distanceSqToEntity);
		distance = Math.max(distance, 1.0D);
		double distMultiplier = 1.0D / (distance / 2.0D);

		float rate = 0.0F;
		float health = bleedingEntity.getHealth() - damage;
		if (health > 12.0F) {
			rate = 1.0F;
		}
		if ((health <= 12.0F) && (health > 8.0F)) {
			rate = 1.5F;
		}
		if ((health <= 8.0F) && (health > 4.0F)) {
			rate = 2.0F;
		}
		if ((health <= 4.0F) && (health > 0.0F)) {
			rate = 2.5F;
		}
		if (health <= 0.0F) {
			rate = 3.0F;
		}
		int splats = (int)(damage * rate * distMultiplier);
		//System.out.println("Created " + splats + " splats of type " + type.getName());
		if((type.equals(VisualType.splatter)) || (type.equals(VisualType.slash)) || (type.equals(VisualType.pierce)) || (type.equals(VisualType.impact)))
		{
			if(bleedingEntity instanceof EntityCreeper) {
				addVisualsWithShading(type, splats, 500, 1500, new Color(0.0F, 0.4F, 0.0F, 0.7F));
			} else if(bleedingEntity instanceof EntitySkeleton) {
				addVisualsWithShading(type, splats, 500, 1500, new Color(0.1F, 0.1F, 0.1F, 0.7F));
			} else if(bleedingEntity instanceof EntitySquid) {
				addVisualsWithShading(type, splats, 500, 1500, new Color(0.0F, 0.0F, 0.2F, 0.7F));
			} else {
				addVisualsWithShading(type, splats, 500, 1500, new Color(0.3F, 0.01F, 0.01F, 0.7F));
			}
		}
		else if (type.equals(VisualType.dust)) {
			addVisualsWithShading(type, splats * 20, 100, 1000, new Color(0.2F, 0.2F, 0.2F, 1.0F));
		}
	}

	private float randMultiplier(double min, double max) {
		return (float) (min + rand.nextDouble() * (max - min));
	}
	
	/*public void adjustHeatOverlay(float intensity) {
		heatOverlay.setTranslucency(intensity);
	}*/
	
	/*public void adjustColdOverlay(float intensity) {
		iceOverlay.setTranslucency(intensity);
	}*/
	
	/*public void adjustWetOverlay(float intensity) {
		wetOverlay.setTranslucency(intensity);
	}*/
}
