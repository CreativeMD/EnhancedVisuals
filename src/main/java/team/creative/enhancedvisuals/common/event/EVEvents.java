package team.creative.enhancedvisuals.common.event;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.AABB;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.common.packet.DamagePacket;
import team.creative.enhancedvisuals.common.packet.ExplosionPacket;
import team.creative.enhancedvisuals.common.packet.PotionPacket;
import team.creative.enhancedvisuals.mixin.EntityAccessor;

public class EVEvents {
    
    public void explosion(Explosion explosion, List<Entity> affected) {
        ExplosionPacket packet = new ExplosionPacket(explosion.center(), explosion.radius(), explosion.getBlockInteraction(), explosion.getDirectSourceEntity() != null ? (explosion
                .getDirectSourceEntity()).getId() : -1, explosion.getDirectSourceEntity() != null ? explosion.getDirectSourceEntity().getClass() : null);
        for (Entity entity : affected)
            if (entity instanceof ServerPlayer s)
                EnhancedVisuals.NETWORK.sendToClient(packet, s);
    }
    
    public void impact(Projectile projectile) {
        if (projectile instanceof ThrownSplashPotion entity && !projectile.level().isClientSide()) {
            AABB axisalignedbb = entity.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
            List<LivingEntity> list = entity.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb);
            if (!list.isEmpty()) {
                for (LivingEntity livingentity : list) {
                    if (livingentity.isAffectedByPotions() && livingentity instanceof ServerPlayer s) {
                        double d0 = entity.distanceToSqr(livingentity);
                        if (d0 < 16.0D)
                            EnhancedVisuals.NETWORK.sendToClient(new PotionPacket(Math.sqrt(d0), entity.getItem()), s);
                    }
                }
            }
        }
    }
    
    public void damage(Player target, DamageSource source, float damage) {
        if (target.level().isClientSide())
            return;
        if (EnhancedVisuals.CONFIG.enableDamageDebug)
            target.displayClientMessage(Component.literal(source.getMsgId() + "," + source.getLocalizedDeathMessage(target).getString()), false);
        EnhancedVisuals.NETWORK.sendToClient(new DamagePacket(target, source, damage), (ServerPlayer) target);
    }
    
    public static boolean areEyesInWater(Player player) {
        return ((EntityAccessor) player).getWasEyeInWater();
    }
}
