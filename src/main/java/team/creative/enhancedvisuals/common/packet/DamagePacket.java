package team.creative.enhancedvisuals.common.packet;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import team.creative.enhancedvisuals.common.handler.DamageHandler.EnhancedDamageSource;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class DamagePacket extends CreativeCorePacket {
    
    public String attackerClass;
    public ItemStack stack;
    public float damage;
    
    public float distance;
    public EnhancedDamageSource source;
    
    public DamagePacket(LivingDamageEvent event) {
        this.damage = event.getAmount();
        Entity attacker = event.getSource().getImmediateSource();
        if (attacker instanceof EntityLiving || attacker instanceof EntityArrow) {
            attackerClass = attacker.getClass().getName().toLowerCase();
            source = EnhancedDamageSource.ATTACKER;
            
            if (attacker instanceof EntityLiving && ((EntityLiving) attacker).getHeldItemMainhand() != null)
                stack = ((EntityLiving) attacker).getHeldItemMainhand();
            
        } else if (event.getSource() == DamageSource.CACTUS)
            this.source = EnhancedDamageSource.CACTUS;
        else if (event.getSource() == DamageSource.FALL || event.getSource() == DamageSource.FALLING_BLOCK)
            this.source = EnhancedDamageSource.FALL;
        else if (event.getSource().equals(DamageSource.DROWN))
            this.source = EnhancedDamageSource.DROWN;
        else if (event.getSource().isFireDamage() || event.getSource() == DamageSource.ON_FIRE)
            this.source = EnhancedDamageSource.FIRE;
        else
            this.source = EnhancedDamageSource.UNKOWN;
    }
    
    public DamagePacket() {
        
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        if (attackerClass != null) {
            buf.writeBoolean(true);
            writeString(buf, attackerClass);
        } else
            buf.writeBoolean(false);
        
        if (stack != null) {
            buf.writeBoolean(true);
            writeItemStack(buf, stack);
        } else
            buf.writeBoolean(false);
        
        buf.writeFloat(damage);
        buf.writeFloat(distance);
        buf.writeInt(source.ordinal());
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        if (buf.readBoolean())
            attackerClass = readString(buf);
        if (buf.readBoolean())
            stack = readItemStack(buf);
        damage = buf.readFloat();
        distance = buf.readFloat();
        source = EnhancedDamageSource.values()[buf.readInt()];
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        if (VisualHandlers.DAMAGE.isEnabled(player))
            VisualHandlers.DAMAGE.playerDamaged(player, this);
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        
    }
    
}
