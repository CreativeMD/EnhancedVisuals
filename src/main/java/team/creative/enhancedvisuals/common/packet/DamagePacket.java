package team.creative.enhancedvisuals.common.packet;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.creative.creativecore.common.network.CanBeNull;
import team.creative.creativecore.common.network.CreativePacket;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class DamagePacket extends CreativePacket {
    
    public float damage;
    public Holder<DamageType> sourceType;
    public int sourceCauseId;
    public int sourceDirectId;
    @CanBeNull
    public Vec3 sourcePosition;
    
    public DamagePacket(Player target, DamageSource source, float damage) {
        this.damage = damage;
        this.sourceType = source.typeHolder();
        this.sourceCauseId = source.getEntity() != null ? source.getEntity().getId() : -1;
        this.sourceDirectId = source.getDirectEntity() != null ? source.getDirectEntity().getId() : -1;
        this.sourcePosition = source.sourcePositionRaw();
    }
    
    public DamagePacket() {}
    
    public DamageSource getSource(Level level) {
        if (this.sourcePosition != null)
            return new DamageSource(this.sourceType, this.sourcePosition);
        return new DamageSource(this.sourceType, level.getEntity(this.sourceDirectId), level.getEntity(this.sourceCauseId));
    }
    
    @Override
    public void executeClient(Player player) {
        if (VisualHandlers.DAMAGE.isEnabled(player))
            VisualHandlers.DAMAGE.playerDamaged(player, getSource(player.level()), damage);
    }
    
    @Override
    public void executeServer(ServerPlayer player) {}
    
}
