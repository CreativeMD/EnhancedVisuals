package team.creative.enhancedvisuals.common.packet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import team.creative.creativecore.common.network.CanBeNull;
import team.creative.creativecore.common.network.CreativePacket;
import team.creative.enhancedvisuals.common.handler.DamageHandler.EnhancedDamageSource;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class DamagePacket extends CreativePacket {
    
    @CanBeNull
    public String attackerClass;
    @CanBeNull
    public ItemStack stack;
    public float damage;
    
    public float distance;
    public EnhancedDamageSource source;
    
    public DamagePacket(LivingDamageEvent event) {
        this.damage = event.getAmount();
        Entity attacker = event.getSource().getDirectEntity();
        if (attacker instanceof LivingEntity || attacker instanceof ArrowEntity) {
            attackerClass = attacker.getClass().getName().toLowerCase();
            source = EnhancedDamageSource.ATTACKER;
            
            if (attacker instanceof LivingEntity && ((LivingEntity) attacker).getMainHandItem() != null)
                stack = ((LivingEntity) attacker).getMainHandItem();
            
        } else if (event.getSource() == DamageSource.CACTUS)
            this.source = EnhancedDamageSource.CACTUS;
        else if (event.getSource() == DamageSource.FALL || event.getSource() == DamageSource.FALLING_BLOCK)
            this.source = EnhancedDamageSource.FALL;
        else if (event.getSource().equals(DamageSource.DROWN))
            this.source = EnhancedDamageSource.DROWN;
        else if (event.getSource().isFire() || event.getSource() == DamageSource.ON_FIRE)
            this.source = EnhancedDamageSource.FIRE;
        else if (event.getSource() == DamageSource.OUT_OF_WORLD)
            this.source = EnhancedDamageSource.VOID;
        else
            this.source = EnhancedDamageSource.UNKOWN;
    }
    
    public DamagePacket() {
        
    }
    
    @Override
    public void executeClient(PlayerEntity player) {
        if (VisualHandlers.DAMAGE.isEnabled(player))
            VisualHandlers.DAMAGE.playerDamaged(player, this);
    }
    
    @Override
    public void executeServer(PlayerEntity player) {
        
    }
    
}
