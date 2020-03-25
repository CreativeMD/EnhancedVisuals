package team.creative.enhancedvisuals.common.handler;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeOverlay;
import team.creative.enhancedvisuals.client.VisualManager;

public class PotionHandler extends VisualHandler {
	
	@CreativeConfig
	public IntMinMax duration = new IntMinMax(30, 60);
	
	@CreativeConfig
	public VisualType potion = new VisualTypeOverlay("potion");
	
	@OnlyIn(value = Dist.CLIENT)
	public void impact(ProjectileImpactEvent.Throwable event) {
		if (event.getEntity() instanceof PotionEntity && !event.isCanceled()) {
			PotionEntity ep = (PotionEntity) event.getEntity();
			if (!ep.world.isRemote) {
				double modifier = 1 - ep.getDistance(Minecraft.getInstance().player) / 5;
				int var11 = PotionUtils.getPotionColor(PotionUtils.getPotionFromItem(ep.getItem()));
				float r = (var11 >> 16 & 255) / 255.0F;
				float g = (var11 >> 8 & 255) / 255.0F;
				float b = (var11 & 255) / 255.0F;
				float f1 = (float) (modifier * 2.0F);
				Visual v = VisualManager.addVisualFadeOut(potion, duration);
				v.opacity = Math.min(1, f1);
				v.color = new Color(r, g, b);
			}
		}
	}
	
}
