package com.sonicjumper.enhancedvisuals.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class EntityUtil {
	public static boolean isBlockNearEntity(Entity e, Block b, int radius) {
		for(int x = (int) (e.posX - radius); x < e.posX + radius; x++) {
			for(int y = (int) (e.posY - (radius/2)); y < e.posY + radius; y++) {
				for(int z = (int) (e.posZ - radius); z < e.posZ + radius; z++) {
					if(e.world.getBlockState(new BlockPos(x, y, z)).getBlock() == b) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static double getDistanceToNearestBlock(Entity e, Block b, int radius) {
		double minDist = radius * 2;
		for(int x = (int) (e.posX - radius); x < e.posX + radius; x++) {
			for(int y = (int) (e.posY - (radius/2)); y < e.posY + radius; y++) {
				for(int z = (int) (e.posZ - radius); z < e.posZ + radius; z++) {
					if(e.world.getBlockState(new BlockPos(x, y, z)).getBlock() == b) {
						double dist = Math.sqrt(Math.pow(e.posX - x, 2) + Math.pow(e.posY - y, 2) + Math.pow(e.posZ - z, 2));
						minDist = Math.min(minDist, dist);
					}
				}
			}
		}
		return minDist;
	}
}
