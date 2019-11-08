package com.sonicjumper.enhancedvisuals.addon.igcm;

import java.util.Iterator;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.ConfigTab;
import com.sonicjumper.enhancedvisuals.handlers.VisualHandler;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import team.creative.enhancedvisuals.api.VisualCategory;

public class IGCMLoader {
	
	public static void initConfiguration() {
		ConfigTab.root.registerElement("enhancedvisuals", new ConfigBranch("Enhanced Visuals", new ItemStack(Items.BONE)) {
			
			@Override
			public void createChildren() {
				for (int i = 0; i < VisualCategory.values().length; i++) {
					VisualCategory category = VisualCategory.values()[i];
					
					registerElement(category.name(), new ConfigBranch(category.name(), ItemStack.EMPTY) {
						
						@Override
						public void saveExtra(NBTTagCompound nbt) {
							
						}
						
						@Override
						public void loadExtra(NBTTagCompound nbt) {
							
						}
						
						@Override
						public boolean requiresSynchronization() {
							return true;
						}
						
						@Override
						public void onRecieveFrom(Side side) {
							
						}
						
						@Override
						public void createChildren() {
							for (int j = 0; j < category.types.size(); j++) {
								registerElement(category.types.get(j).getConfigCat(), category.types.get(j).getConfigBranch());
							}
						}
					});
					
				}
				
				for (Iterator<VisualHandler> iterator = VisualHandler.getAllHandlers().iterator(); iterator.hasNext();) {
					VisualHandler handler = iterator.next();
					registerElement(handler.name, handler.getConfigBranch());
				}
			}
			
			@Override
			public boolean requiresSynchronization() {
				return true;
			}
			
			@Override
			public void onRecieveFrom(Side side) {
				
			}
			
			@Override
			public void loadExtra(NBTTagCompound nbt) {
				
			}
			
			@Override
			public void saveExtra(NBTTagCompound nbt) {
				
			}
		});
	}
}
