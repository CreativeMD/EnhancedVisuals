package team.creative.enhancedvisuals.common.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import team.creative.creativecore.common.network.CreativePacket;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class PotionPacket extends CreativePacket {
	
	public double distance;
	public ItemStack stack;
	
	public PotionPacket() {
		
	}
	
	public PotionPacket(double distance, ItemStack stack) {
		this.distance = distance;
		this.stack = stack;
	}
	
	@Override
	public void executeClient(PlayerEntity player) {
		if (VisualHandlers.POTION.isEnabled(player))
			VisualHandlers.POTION.impact(distance, stack);
	}
	
	@Override
	public void executeServer(PlayerEntity player) {
		
	}
}
