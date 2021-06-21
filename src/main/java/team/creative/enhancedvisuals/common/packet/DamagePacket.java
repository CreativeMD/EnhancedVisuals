package team.creative.enhancedvisuals.common.packet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
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
    
    public DamagePacket(LivingDamageEvent event) {
        this.damage = event.getAmount();
        Entity attacker = event.getSource().getImmediateSource();
        this.fire = event.getSource().isFireDamage();
        if (attacker instanceof EntityLiving || attacker instanceof EntityArrow) {
            attackerClass = attacker.getClass().getName().toLowerCase();
            this.source = "attacker";
            
            if (attacker instanceof LivingEntity && ((LivingEntity) attacker).getMainHandItem() != null)
                stack = ((LivingEntity) attacker).getMainHandItem();
            
            if (attacker instanceof EntityLiving && ((EntityLiving) attacker).getHeldItemMainhand() != null)
                stack = ((EntityLiving) attacker).getHeldItemMainhand();
            
        } else
            this.source = event.getSource().damageType;
    }
    
    public DamagePacket() {
        
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        if (VisualHandlers.DAMAGE.isEnabled(player))
            VisualHandlers.DAMAGE.playerDamaged(player, this);
    }
    
    @Override
    public void executeServer(PlayerEntity player) {
        
    }
    
}
