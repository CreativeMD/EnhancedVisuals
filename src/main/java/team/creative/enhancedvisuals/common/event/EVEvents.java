package team.creative.enhancedvisuals.common.event;

import java.lang.reflect.Field;
import java.util.List;

import com.creativemd.creativecore.common.packet.PacketHandler;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.client.sound.SoundMuteHandler;
import team.creative.enhancedvisuals.common.packet.DamagePacket;
import team.creative.enhancedvisuals.common.packet.ExplosionPacket;
import team.creative.enhancedvisuals.common.packet.PotionPacket;

public class EVEvents {
    
    private Field size = ReflectionHelper.findField(Explosion.class, new String[] { "field_77280_f", "size" });
    private Field exploder = ReflectionHelper.findField(Explosion.class, new String[] { "field_77283_e", "exploder" });
    
    @SubscribeEvent
    public void explosion(ExplosionEvent.Detonate event) {
        if (event.getWorld() != null && !event.getWorld().isRemote) {
            try {
                int sourceEntity = -1;
                Entity source = ((Entity) exploder.get(event.getExplosion()));
                if (source != null)
                    sourceEntity = source.getEntityId();
                ExplosionPacket packet = new ExplosionPacket(event.getExplosion().getPosition(), size.getFloat(event.getExplosion()), sourceEntity);
                for (Entity entity : event.getAffectedEntities())
                    if (entity instanceof EntityPlayerMP)
                        PacketHandler.sendPacketToPlayer(packet, (EntityPlayerMP) entity);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    @SubscribeEvent
    public void impact(ProjectileImpactEvent.Throwable event) {
        if (!event.getEntity().world.isRemote && event.getEntity() instanceof EntityPotion && !event.isCanceled()) {
            EntityPotion entity = (EntityPotion) event.getEntity();
            AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D);
            List<Entity> list = entity.world.getEntitiesWithinAABBExcludingEntity(entity, axisalignedbb);
            if (!list.isEmpty()) {
                for (Entity livingentity : list) {
                    if (livingentity instanceof EntityLivingBase && ((EntityLivingBase) livingentity).canBeHitWithPotion() && livingentity instanceof EntityPlayer) {
                        double d0 = entity.getDistanceSq(livingentity);
                        if (d0 < 16.0D) {
                            PacketHandler.sendPacketToPlayer(new PotionPacket(Math.sqrt(d0), entity.getPotion()), (EntityPlayerMP) livingentity);
                        }
                    }
                }
            }
        }
        
    }
    
    @SubscribeEvent
    public void damage(LivingDamageEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP)
            PacketHandler.sendPacketToPlayer(new DamagePacket(event), (EntityPlayerMP) event.getEntity());
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void clientTick(ClientTickEvent event) {
        if (event.phase == Phase.START && EVClient.shouldTick()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            VisualManager.onTick(player);
            
            SoundMuteHandler.tick();
        }
    }
    
    public static boolean areEyesInWater(EntityPlayer player) {
        return player.isInsideOfMaterial(Material.WATER);
    }
}
