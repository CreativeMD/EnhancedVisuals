package team.creative.enhancedvisuals.common.handler;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.event.SelectEndermanEvent;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeOverlay;
import team.creative.enhancedvisuals.client.VisualManager;

public class SlenderHandler extends VisualHandler {
    
    @CreativeConfig
    public double defaultIntensity = 0;
    
    @CreativeConfig
    public double maxIntensity = 0.3;
    
    @CreativeConfig
    public double distanceFactor = 0.25;
    
    @CreativeConfig
    public VisualType slender = new VisualTypeOverlay("slender", 50);
    
    public Visual slenderVisual;
    
    public Class mutantEnderman = loadMutantEnderman();
    
    private Class loadMutantEnderman() {
        try {
            return Class.forName("chumbanotz.mutantbeasts.entity.mutant.MutantEndermanEntity");
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public void tick(@Nullable Player player) {
        if (slenderVisual == null) {
            slenderVisual = new Visual(slender, this, 0);
            VisualManager.add(slenderVisual);
        }
        
        float intensity = (float) defaultIntensity;
        
        if (player != null) {
            float modifier = 0.0F;
            double d0 = player.getX();
            double d1 = player.getY();
            double d2 = player.getZ();
            
            AABB box = player.getBoundingBox();
            box = box.inflate(16, 16, 16);
            
            SelectEndermanEvent event = new SelectEndermanEvent(TargetingConditions.forNonCombat());
            CreativeCore.loader().postForge(event);
            if (!event.isCanceled()) {
                Entity mob = player.level().getNearestEntity(EnderMan.class, event.conditions, player, d0, d1, d2, box);
                if (mutantEnderman != null)
                    mob = player.level().getNearestEntity(mutantEnderman, TargetingConditions.forNonCombat(), player, d0, d1, d2, box);
                
                if (mob != null) {
                    float distModifier = (float) (1.0F / Math.pow(Math.sqrt(Math.pow(d0 - mob.getX(), 2) + Math.pow(d1 - mob.getY(), 2) + Math.pow(d2 - mob.getZ(), 2)) / 3.0D, 2));
                    if (distModifier > modifier) {
                        modifier = distModifier;
                        if (modifier > 3.5F) {
                            modifier = 3.5F;
                        }
                    }
                    
                    intensity = (float) Math.max(defaultIntensity, Math.min(maxIntensity, distanceFactor * modifier));
                }
            }
        }
        slenderVisual.setOpacityInternal(intensity);
        
    }
}
