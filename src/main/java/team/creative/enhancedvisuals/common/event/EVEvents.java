package team.creative.enhancedvisuals.common.event;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.client.sound.SoundMuteHandler;
import team.creative.enhancedvisuals.common.packet.DamagePacket;
import team.creative.enhancedvisuals.common.packet.ExplosionPacket;
import team.creative.enhancedvisuals.common.packet.PotionPacket;
import team.creative.enhancedvisuals.mixin.EntityAccessor;
import team.creative.enhancedvisuals.mixin.ExplosionAccessor;

public class EVEvents {
    
    public void explosion(Explosion explosion, List<Entity> affected) {
        ExplosionAccessor e = (ExplosionAccessor) explosion;
        Vec3 position = new Vec3(e.getX(), e.getY(), e.getZ());
        ExplosionPacket packet = new ExplosionPacket(position, e.getRadius(), explosion.getBlockInteraction(), e.getSource() != null ? (e.getSource()).getId() : -1, e
                .getSource() != null ? e.getSource().getClass() : null);
        for (Entity entity : affected)
            if (entity instanceof ServerPlayer s)
                EnhancedVisuals.NETWORK.sendToClient(packet, s);
    }
    
    public void impact(Projectile projectile) {
        if (projectile instanceof ThrownPotion entity && !projectile.level().isClientSide) {
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
        if (target.level().isClientSide)
            return;
        if (EnhancedVisuals.CONFIG.enableDamageDebug)
            target.sendSystemMessage(Component.literal(source.getMsgId() + "," + source.getLocalizedDeathMessage(target).getString()));
        EnhancedVisuals.NETWORK.sendToClient(new DamagePacket(target, source, damage), (ServerPlayer) target);
    }
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void respawn() {
        VisualManager.clearEverything();
    }
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void clientTick() {
        if (EVClient.shouldTick()) {
            Player player = Minecraft.getInstance().player;
            VisualManager.onTick(player);
        }
        SoundMuteHandler.tick();
    }
    
    public static boolean areEyesInWater(Player player) {
        return ((EntityAccessor) player).getWasEyeInWater();
    }
}
