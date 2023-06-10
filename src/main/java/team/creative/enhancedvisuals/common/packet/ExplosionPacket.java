package team.creative.enhancedvisuals.common.packet;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import team.creative.creativecore.common.network.CreativePacket;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class ExplosionPacket extends CreativePacket {
    
    public Vec3 pos;
    public float size;
    public int sourceEntity;
    
    public ExplosionPacket(Vec3 pos, float size, int sourceEntity) {
        this.pos = pos;
        this.size = size;
        this.sourceEntity = sourceEntity;
    }
    
    public ExplosionPacket() {
        
    }
    
    @Override
    public void executeClient(Player player) {
        if (VisualHandlers.EXPLOSION.isEnabled(player))
            VisualHandlers.EXPLOSION.onExploded(player, pos, size, player.level().getEntity(sourceEntity));
    }
    
    @Override
    public void executeServer(ServerPlayer player) {
        
    }
    
}
