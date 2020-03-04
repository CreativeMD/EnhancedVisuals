package team.creative.enhancedvisuals.common.handler;

import java.awt.Color;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.premade.IntMinMax;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
	
	@SideOnly(Side.CLIENT)
	public void impact(ProjectileImpactEvent.Throwable event) {
		if (event.getEntity() instanceof EntityPotion && !event.isCanceled()) {
			EntityPotion ep = (EntityPotion) event.getEntity();
			if (!ep.world.isRemote) {
				double modifier = 1 - ep.getDistance(Minecraft.getMinecraft().player) / 5;
				int var11 = PotionUtils.getPotionColor(PotionUtils.getPotionFromItem(ep.getPotion()));
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
