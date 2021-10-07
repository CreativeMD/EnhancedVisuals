package team.creative.enhancedvisuals.common.handler;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.config.api.CreativeConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
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
    
    public Class infectedEnderman = loadInfectedEnderman();
    
    private Class loadMutantEnderman() {
        try {
            return Class.forName("chumbanotz.mutantbeasts.entity.mutant.MutantEndermanEntity");
        } catch (Exception e) {
            return null;
        }
    }
    
    private Class loadInfectedEnderman() {
        try {
            return Class.forName("com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman");
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public void tick(@Nullable EntityPlayer player) {
        if (slenderVisual == null) {
            slenderVisual = new Visual(slender, this, 0);
            VisualManager.add(slenderVisual);
        }
        
        float intensity = (float) defaultIntensity;
        
        if (player != null) {
            float modifier = 0.0F;
            
            double d0 = player.posX;
            double d1 = player.posY;
            double d2 = player.posZ;
            
            AxisAlignedBB box = player.getEntityBoundingBox();
            box = box.grow(16, 16, 16);
            
            SelectEndermanEvent event = new SelectEndermanEvent((x) -> true);
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.isCanceled()) {
                
                Double distance = null;
                
                for (EntityEnderman mob : player.world.getEntitiesWithinAABB(EntityEnderman.class, box, null))
                    if (mob != null && event.predicate.test(mob))
                        if (distance == null)
                            distance = Math.sqrt(Math.pow(d0 - mob.posX, 2) + Math.pow(d1 - mob.posY, 2) + Math.pow(d2 - mob.posZ, 2));
                        else
                            distance = Math.min(distance, Math.sqrt(Math.pow(d0 - mob.posX, 2) + Math.pow(d1 - mob.posY, 2) + Math.pow(d2 - mob.posZ, 2)));
                        
                if (mutantEnderman != null)
                    for (Entity mob : player.world.getEntitiesWithinAABB((Class<? extends Entity>) mutantEnderman, box, null))
                        if (mob != null)
                            if (distance == null)
                                distance = Math.sqrt(Math.pow(d0 - mob.posX, 2) + Math.pow(d1 - mob.posY, 2) + Math.pow(d2 - mob.posZ, 2));
                            else
                                distance = Math.min(distance, Math.sqrt(Math.pow(d0 - mob.posX, 2) + Math.pow(d1 - mob.posY, 2) + Math.pow(d2 - mob.posZ, 2)));
                            
                if (infectedEnderman != null)
                    for (Entity mob : player.world.getEntitiesWithinAABB((Class<? extends Entity>) infectedEnderman, box, null))
                        if (mob != null)
                            if (distance == null)
                                distance = Math.sqrt(Math.pow(d0 - mob.posX, 2) + Math.pow(d1 - mob.posY, 2) + Math.pow(d2 - mob.posZ, 2));
                            else
                                distance = Math.min(distance, Math.sqrt(Math.pow(d0 - mob.posX, 2) + Math.pow(d1 - mob.posY, 2) + Math.pow(d2 - mob.posZ, 2)));
                            
                if (distance != null) {
                    float distModifier = (float) Math.pow((1.0F / distance) / 3.0D, 2);
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
