package team.creative.enhancedvisuals.common.packet;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import team.creative.creativecore.common.network.CanBeNull;
import team.creative.creativecore.common.network.CreativePacket;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class DamagePacket extends CreativePacket {
    
    @CanBeNull
    public String attackerClass;
    @CanBeNull
    public ItemStack stack;
    public float damage;
    
    public float distance;
    public boolean fire;
    public String source;
    
    public DamagePacket(Player target, DamageSource source, float damage) {
        this.damage = damage;
        Entity attacker = source.getDirectEntity();
        
        this.fire = source.is(DamageTypeTags.IS_FIRE);
        if (attacker instanceof LivingEntity || attacker instanceof Arrow) {
            attackerClass = attacker.getClass().getName().toLowerCase();
            this.source = "attacker";
            
            if (attacker instanceof LivingEntity && ((LivingEntity) attacker).getMainHandItem() != null)
                stack = ((LivingEntity) attacker).getMainHandItem();
            
        } else
            this.source = source.getMsgId();
    }
    
    public DamagePacket() {}
    
    @Override
    public void executeClient(Player player) {
        if (VisualHandlers.DAMAGE.isEnabled(player))
            VisualHandlers.DAMAGE.playerDamaged(player, this);
    }
    
    @Override
    public void executeServer(ServerPlayer player) {}
    
}
