package team.creative.enhancedvisuals.common.packet;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import team.creative.creativecore.common.network.CreativePacket;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class ExplosionPacket extends CreativePacket {
    
    public Vec3 pos;
    public float size;
    public int sourceEntity;
    public Explosion.BlockInteraction blockInteraction;
    
    public ExplosionPacket(Vec3 pos, float size, Explosion.BlockInteraction blockInteraction, int sourceEntity) {
        this.pos = pos;
        this.size = size;
        this.blockInteraction = blockInteraction;
        this.sourceEntity = sourceEntity;
    }
    
    public ExplosionPacket() {
        
    }
    
    @Override
    public void executeClient(Player player) {
        if (VisualHandlers.EXPLOSION.isEnabled(player))
            VisualHandlers.EXPLOSION.onExploded(player, pos, size, blockInteraction, player.level().getEntity(sourceEntity));
    }
    
    @Override
    public void executeServer(ServerPlayer player) {
        
    }
    
}
