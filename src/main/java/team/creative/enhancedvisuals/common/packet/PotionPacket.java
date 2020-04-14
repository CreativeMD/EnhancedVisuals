package team.creative.enhancedvisuals.common.packet;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class PotionPacket extends CreativeCorePacket {
	
	public double distance;
	public ItemStack stack;
	
	public PotionPacket() {
		
	}
	
	public PotionPacket(double distance, ItemStack stack) {
		this.distance = distance;
		this.stack = stack;
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		buf.writeDouble(distance);
		writeItemStack(buf, stack);
	}
	
	@Override
	public void readBytes(ByteBuf buf) {
		distance = buf.readDouble();
		stack = readItemStack(buf);
	}
	
	@Override
	public void executeClient(EntityPlayer player) {
		if (VisualHandlers.POTION.isEnabled(player))
			VisualHandlers.POTION.impact(distance, stack);
	}
	
	@Override
	public void executeServer(EntityPlayer player) {
		
	}
}
