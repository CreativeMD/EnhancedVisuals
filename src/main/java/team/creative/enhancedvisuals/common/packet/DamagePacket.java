package team.creative.enhancedvisuals.common.packet;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.creative.creativecore.common.network.CanBeNull;
import team.creative.creativecore.common.network.CreativePacket;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class DamagePacket extends CreativePacket {
    
    public float damage;
    public ResourceLocation sourceType;
    public int sourceCauseId;
    public int sourceDirectId;
    @CanBeNull
    public Vec3 sourcePosition;
    
    public DamagePacket(Player target, DamageSource source, float damage) {
        this.damage = damage;
        this.sourceType = source.typeHolder().unwrapKey().get().location();
        this.sourceCauseId = source.getEntity() != null ? source.getEntity().getId() : -1;
        this.sourceDirectId = source.getDirectEntity() != null ? source.getDirectEntity().getId() : -1;
        this.sourcePosition = source.sourcePositionRaw();
    }
    
    public DamagePacket() {}
    
    public DamageSource getSource(Level level) {
        var type = level.registryAccess().lookup(Registries.DAMAGE_TYPE).get().getOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, sourceType));
        if (this.sourcePosition != null)
            return new DamageSource(type, this.sourcePosition);
        return new DamageSource(type, level.getEntity(this.sourceDirectId), level.getEntity(this.sourceCauseId));
    }
    
    @Override
    public void executeClient(Player player) {
        if (VisualHandlers.DAMAGE.isEnabled(player))
            VisualHandlers.DAMAGE.playerDamaged(player, getSource(player.level()), damage);
    }
    
    @Override
    public void executeServer(ServerPlayer player) {}
    
}
