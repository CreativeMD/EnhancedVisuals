package team.creative.enhancedvisuals.common.event;

import java.lang.reflect.Field;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.client.sound.SoundMuteHandler;
import team.creative.enhancedvisuals.common.packet.DamagePacket;
import team.creative.enhancedvisuals.common.packet.ExplosionPacket;
import team.creative.enhancedvisuals.common.packet.PotionPacket;

public class EVEvents {
    
    private Field size = ObfuscationReflectionHelper.findField(Explosion.class, "f_46017_");
    private Field exploder = ObfuscationReflectionHelper.findField(Explosion.class, "f_46016_");
    
    @SubscribeEvent
    public void explosion(ExplosionEvent.Detonate event) {
        if (!event.getWorld().isClientSide) {
            try {
                ExplosionPacket packet = new ExplosionPacket(event.getExplosion().getPosition(), size
                        .getFloat(event.getExplosion()), (Entity) exploder.get(event.getExplosion()) != null ? ((Entity) exploder.get(event.getExplosion())).getId() : -1);
                for (Entity entity : event.getAffectedEntities())
                    if (entity instanceof ServerPlayer)
                        EnhancedVisuals.NETWORK.sendToClient(packet, (ServerPlayer) entity);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    @SubscribeEvent
    public void impact(ProjectileImpactEvent event) {
        if (event.getEntity() instanceof ThrownPotion && !event.getEntity().level.isClientSide && !event.isCanceled()) {
            ThrownPotion entity = (ThrownPotion) event.getEntity();
            AABB axisalignedbb = entity.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
            List<LivingEntity> list = entity.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
            if (!list.isEmpty()) {
                for (LivingEntity livingentity : list) {
                    if (livingentity.isAffectedByPotions() && livingentity instanceof ServerPlayer) {
                        double d0 = entity.distanceToSqr(livingentity);
                        if (d0 < 16.0D)
                            EnhancedVisuals.NETWORK.sendToClient(new PotionPacket(Math.sqrt(d0), entity.getItem()), (ServerPlayer) livingentity);
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void damage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (EnhancedVisuals.CONFIG.enableDamageDebug)
                ((ServerPlayer) event.getEntity()).sendMessage(new TextComponent(event.getSource().msgId + "," + event.getSource().getLocalizedDeathMessage(event.getEntityLiving())
                        .getString()), Util.NIL_UUID);
            EnhancedVisuals.NETWORK.sendToClient(new DamagePacket(event), (ServerPlayer) event.getEntity());
        }
    }
    
    @SubscribeEvent
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void clientTick(ClientTickEvent event) {
        if (event.phase == Phase.START && EVClient.shouldTick()) {
            Player player = Minecraft.getInstance().player;
            VisualManager.onTick(player);
            
        }
        SoundMuteHandler.tick();
    }
    
    private static Field eyesInWaterField = ObfuscationReflectionHelper.findField(Entity.class, "f_19800_");
    
    public static boolean areEyesInWater(Player player) {
        try {
            return eyesInWaterField.getBoolean(player);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
