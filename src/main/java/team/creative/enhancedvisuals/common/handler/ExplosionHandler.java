package team.creative.enhancedvisuals.common.handler;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.premade.IntMinMax;
import com.creativemd.creativecore.common.config.premade.curve.DecimalCurve;
import com.creativemd.creativecore.common.config.premade.curve.IntCurve;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.event.VisualExplosionEvent;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeBlur;
import team.creative.enhancedvisuals.api.type.VisualTypeParticle;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.client.sound.SoundMuteHandler;

public class ExplosionHandler extends VisualHandler {
    
    @CreativeConfig
    public VisualType dust = new VisualTypeParticle("dust", 1.0F);
    @CreativeConfig
    public IntMinMax dustDuration = new IntMinMax(500, 1000);
    @CreativeConfig
    public IntCurve dustAmount = new IntCurve(0, 0, 20, 30);
    
    @CreativeConfig
    public float maxExplosionVolume = 0.5F;
    @CreativeConfig
    public IntCurve explosionSoundTime = new IntCurve(0, 50, 20, 200);
    
    @CreativeConfig
    public VisualType blur = new VisualTypeBlur("blur");
    @CreativeConfig
    public DecimalCurve maxBlur = new DecimalCurve(0, 50, 10, 100);
    @CreativeConfig
    public IntCurve explosionBlurTime = new IntCurve(0, 10, 20, 20);
    
    public void onExploded(EntityPlayer player, Vec3d pos, float size, @Nullable Entity source) {
        float f3 = size * 2.0F;
        double d12 = player.getDistance(pos.x, pos.y, pos.z) / f3;
        
        double d14 = getBlockDensity(pos, player);
        double d10 = (1.0D - d12) * d14;
        
        float damage = ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * f3 + 1.0D));
        VisualExplosionEvent event = new VisualExplosionEvent(damage);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled() && event.getNewDamage() > 0) {
            damage = event.getNewDamage();
            VisualManager.addParticlesFadeOut(dust, this, (int) dustAmount.valueAt(damage), dustDuration, true);
            
            DecimalCurve explosionSoundVolume = new DecimalCurve(0, maxExplosionVolume, explosionSoundTime.valueAt(damage), 0);
            DecimalCurve explosionSoundMuteVolume = new DecimalCurve(0, 1, explosionSoundTime.valueAt(damage), 0);
            if (SoundMuteHandler.startMuting(explosionSoundMuteVolume))
                playSoundFadeOut(new ResourceLocation(EnhancedVisuals.MODID, "ringing"), null, explosionSoundVolume);
            
            if (!event.isBlurDisabled())
                VisualManager.addVisualFadeOut(blur, this, new DecimalCurve(0, maxBlur.valueAt(damage), (int) (explosionBlurTime.valueAt(damage)), 0));
        }
    }
    
    public static double lerp(double pct, double start, double end) {
        return start + pct * (end - start);
    }
    
    public static float getBlockDensity(Vec3d p_222259_0_, Entity p_222259_1_) {
        AxisAlignedBB axisalignedbb = p_222259_1_.getEntityBoundingBox();
        double d0 = 1.0D / ((axisalignedbb.maxX - axisalignedbb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.maxY - axisalignedbb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
            int i = 0;
            int j = 0;
            
            for (float f = 0.0F; f <= 1.0F; f = (float) (f + d0)) {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) (f1 + d1)) {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) (f2 + d2)) {
                        double d5 = lerp(f, axisalignedbb.minX, axisalignedbb.maxX);
                        double d6 = lerp(f1, axisalignedbb.minY, axisalignedbb.maxY);
                        double d7 = lerp(f2, axisalignedbb.minZ, axisalignedbb.maxZ);
                        Vec3d vec3d = new Vec3d(d5 + d3, d6, d7 + d4);
                        RayTraceResult result = p_222259_1_.world.rayTraceBlocks(vec3d, p_222259_0_);
                        if (result == null || result.typeOfHit == RayTraceResult.Type.MISS) {
                            ++i;
                        }
                        
                        ++j;
                    }
                }
            }
            
            return (float) i / (float) j;
        } else {
            return 0.0F;
        }
    }
}
