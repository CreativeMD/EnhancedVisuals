package team.creative.enhancedvisuals.common.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import team.creative.creativecore.common.network.CreativePacket;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class ExplosionPacket extends CreativePacket {
	
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
	public void executeClient(PlayerEntity player) {
		if (VisualHandlers.EXPLOSION.isEnabled(player))
			VisualHandlers.EXPLOSION.onExploded(player, pos, size, player.world.getEntityByID(sourceEntity));
	}
	
	@Override
	public void executeServer(PlayerEntity player) {
		
	}
	
}
