package team.creative.enhancedvisuals.common.handler;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
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
    
    public void impact(double distance, ItemStack stack) {
        double modifier = 1 - Math.min(5, distance) / 5;
        int var11 = PotionUtils.getPotionColor(PotionUtils.getPotionFromItem(stack));
        float r = (var11 >> 16 & 255) / 255.0F;
        float g = (var11 >> 8 & 255) / 255.0F;
        float b = (var11 & 255) / 255.0F;
        if (modifier <= 0)
            return;
        Visual v = VisualManager.addVisualFadeOut(potion, this, new DecimalCurve(0, Math.min(1, modifier * 2), duration.next(VisualManager.rand), 0));
        v.color = new Color(r, g, b);
    }
    
}
