package com.sonicjumper.enhancedvisuals.environment;

import java.util.ArrayList;
import java.util.Iterator;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.event.VisualEventHandler;
import com.sonicjumper.enhancedvisuals.util.EntityUtil;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class TemperatureHandler extends BaseEnvironmentEffect {
	private float temperature;
	private ArrayList<TemperatureFactor> factors = new ArrayList<TemperatureFactor>();
	
	public TemperatureHandler(VisualEventHandler veh) {
		super(veh);
		temperature = 1.0F;
		factors.add(new TemperatureFactor() {
			@Override
			public boolean runFactor() {
				float currentWorldTemp = parent.mc.theWorld.getBiomeGenForCoords(new BlockPos((int)Math.floor(parent.mc.thePlayer.posX), 0, (int)Math.floor(parent.mc.thePlayer.posZ))).getTemperature();
				factor = currentWorldTemp + (parent.mc.thePlayer.isBurning() ? 4.0F : 0.0F) - parent.wetnessHandler.getWetness() * (parent.mc.thePlayer.isSprinting() ? 4.0F : 1.0F);
				//System.out.println("World temp factor: " + factor);
				return true;
			}

			@Override
			public float getFactorRate() {
				return (float) (parent.mc.thePlayer.isSprinting() ? 0.0004D : 0.0001D);
			}
		});
		factors.add(new TemperatureFactor() {
			@Override
			public boolean runFactor() {
				factor = 0.0F;
				int leatherCount = 0;
				Iterable<ItemStack> stacks = parent.mc.thePlayer.getArmorInventoryList();
				for (Iterator iterator = stacks.iterator(); iterator.hasNext();) {
					ItemStack wornItem = (ItemStack) iterator.next();
	                if(wornItem != null && wornItem.getItem() instanceof ItemArmor) {
	                	ItemArmor armor = (ItemArmor) wornItem.getItem();
	                	if(armor.getArmorMaterial().equals(ArmorMaterial.LEATHER)) {
	                		leatherCount++;
	                	}
	                	/*if(armor.getArmorMaterial().equals(EnumArmorMaterial.IRON) || armor.getArmorMaterial().equals(EnumArmorMaterial.CHAIN)) {
	                		factor += 0.2F;
	                	}
	                	if(armor.getArmorMaterial().equals(EnumArmorMaterial.DIAMOND)) {
	                		factor -= 0.1F;
	                	}
	                	if(armor.getArmorMaterial().equals(EnumArmorMaterial.GOLD)) {
	                		factor = factor < 1.0F ? factor + 0.25F : factor - 0.25F;
	                	}*/
	                }
	            }
				if(temperature < 0.15F * leatherCount) {
					factor = 0.15F * leatherCount;
				}
				//System.out.println("Armor factor: " + factor);
				return factor != 0.0F;
			}

			@Override
			public float getFactorRate() {
				return 0.0001F;
			}
			
		});
		factors.add(new TemperatureFactor() {

			@Override
			public boolean runFactor() {
				factor = 1.0F;
				float heat = 0.0F;
				if(EntityUtil.isBlockNearEntity(parent.mc.thePlayer, Blocks.fire, 8)) {
					double dist = EntityUtil.getDistanceToNearestBlock(parent.mc.thePlayer, Blocks.fire, 8);
					heat = Math.max((float) (1 / dist) * 2.0F, heat);
				}
				if(EntityUtil.isBlockNearEntity(parent.mc.thePlayer, Blocks.lava, 8)) {
					double dist = EntityUtil.getDistanceToNearestBlock(parent.mc.thePlayer, Blocks.lava, 8);
					heat = Math.max((float) (1 / dist) * 5.0F, heat);
				}
				if(EntityUtil.isBlockNearEntity(parent.mc.thePlayer, Blocks.flowing_lava, 8)) {
					double dist = EntityUtil.getDistanceToNearestBlock(parent.mc.thePlayer, Blocks.flowing_lava, 8);
					heat = Math.max((float) (1 / dist) * 5.0F, heat);
				}
				factor += heat;
				//System.out.println("Fire/Lava factor:" + factor);
				return factor != 1.0F;
			}

			@Override
			public float getFactorRate() {
				return 0.0001F;
			}
		});
	}

	@Override
	public void onTick() {
		for(TemperatureFactor tf : factors) {
			if(tf.runFactor()) {
				temperature = (float) (temperature + (tf.getFactor() - temperature) * tf.getFactorRate());
			}
		}
		temperature = temperature > 2.0F ? 2.0F : temperature;
		temperature = temperature < 0.0F ? 0.0F : temperature;
		//System.out.println("Overall temp: " + temperature);
		
		if(parent.mc.thePlayer.isInWater())
			if(temperature < 1.0F)
				temperature = Math.min(1.0F, temperature+0.1F);
			else if(temperature > 1.0F)
				temperature = Math.max(1.0F, temperature-0.1F);
		
		if(temperature < 0.25F) {
			//System.out.println("Adjusting freeze overlay");
			float cold = (1.0F - (temperature * 4.0F)) * 0.75F;
			Base.instance.manager.adjustColdOverlay(cold < 1.0F ? cold : 1.0F);
		}
		if(temperature > 1.0F) {
			//System.out.println("Adjusting burn overlay");
			float heat = (temperature - 1.0F) * 0.75F;
			Base.instance.manager.adjustHeatOverlay(heat < 1.0F ? heat : 1.0F);
		}
	}

	@Override
	public void resetEffect() {
		temperature = 1.0F;
	}
}
