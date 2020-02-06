package team.creative.enhancedvisuals.common.handler;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import team.creative.creativecore.common.config.CreativeConfig;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.creativecore.common.config.premade.curve.IntCurve;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.VisualHandler;
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
	
	public void onExploded(PlayerEntity player, Vec3d pos, float size, @Nullable Entity source) {
		float f3 = size * 2.0F;
		double d12 = MathHelper.sqrt(player.getDistanceSq(pos)) / f3;
		
		double d14 = Explosion.getBlockDensity(pos, player);
		double d10 = (1.0D - d12) * d14;
		
		float damage = ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * f3 + 1.0D));
		if (damage > 0) {
			VisualManager.addParticlesFadeOut(dust, (int) dustAmount.valueAt(damage), dustDuration, true);
			
			DecimalCurve explosionSoundVolume = new DecimalCurve(0, maxExplosionVolume, explosionSoundTime.valueAt(damage), 0);
			DecimalCurve explosionSoundMuteVolume = new DecimalCurve(0, 1, explosionSoundTime.valueAt(damage), 0);
			if (SoundMuteHandler.startMuting(explosionSoundMuteVolume))
				playSoundFadeOut(new ResourceLocation(EnhancedVisuals.MODID, "ringing"), null, explosionSoundVolume);
			
			VisualManager.addVisualFadeOut(blur, new DecimalCurve(0, maxBlur.valueAt(damage), (int) (explosionBlurTime.valueAt(damage)), 0));
		}
	}
}
