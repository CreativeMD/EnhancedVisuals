package team.creative.enhancedvisuals.common.packet;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class ExplosionPacket extends CreativeCorePacket {
	
	public Vec3d pos;
	public float size;
	public int sourceEntity;
	
	public ExplosionPacket(Vec3d pos, float size, int sourceEntity) {
		this.pos = pos;
		this.size = size;
		this.sourceEntity = sourceEntity;
	}
	
	public ExplosionPacket() {
		
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		writeVec3d(pos, buf);
		buf.writeFloat(size);
		buf.writeInt(sourceEntity);
	}
	
	@Override
	public void readBytes(ByteBuf buf) {
		pos = readVec3d(buf);
		size = buf.readFloat();
		sourceEntity = buf.readInt();
	}
	
	@Override
	public void executeClient(EntityPlayer player) {
		if (VisualHandlers.EXPLOSION.isEnabled(player))
			VisualHandlers.EXPLOSION.onExploded(player, pos, size, player.world.getEntityByID(sourceEntity));
	}
	
	@Override
	public void executeServer(EntityPlayer player) {
		
	}
	
}
