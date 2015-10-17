package com.sonicjumper.enhancedvisuals.environment;

import java.awt.Color;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.ConfigCore;
import com.sonicjumper.enhancedvisuals.event.VisualEventHandler;
import com.sonicjumper.enhancedvisuals.visuals.BoxGlowBlur;
import com.sonicjumper.enhancedvisuals.visuals.BoxReverseGlowBlur;
import com.sonicjumper.enhancedvisuals.visuals.GlowBlur;
import com.sonicjumper.enhancedvisuals.visuals.ReverseGlowBlur;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualManager;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

import net.minecraft.client.Minecraft;

public class EyeSensitivityHandler extends BaseEnvironmentEffect {
	private float eyeAdjustment;
	private int glowBuffer;
	
	public EyeSensitivityHandler(VisualEventHandler veh) {
		super(veh);
		eyeAdjustment = 0.4F;
	}
	
	@Override
	public void onTick() {
		float currentPlayerBrightness = parent.mc.thePlayer.getBrightness(0.0F);
		if(currentPlayerBrightness <= eyeAdjustment) {
			eyeAdjustment = (float)((double)this.eyeAdjustment + (double)(currentPlayerBrightness - this.eyeAdjustment) * 0.01D);
		} else {
			eyeAdjustment = (float)((double)this.eyeAdjustment + (double)(currentPlayerBrightness - this.eyeAdjustment) * 0.05D);
		}
		if(currentPlayerBrightness - eyeAdjustment > 0.5F) {
			// If the brightness has a large change
			if(glowBuffer <= 0) {
				// Package a GlowBlur or BoxGlowBlur inside a reverse glow to get a "flare" effect
				Visual r;
				if(ConfigCore.useTrueGlow) {
					Visual g = new GlowBlur(VisualType.blur, (int) ((currentPlayerBrightness - eyeAdjustment) * 200), new Color(1.0F, 1.0F, 1.0F, 1.0F), true, ConfigCore.blurQuality, (int) (15 * (currentPlayerBrightness - eyeAdjustment)), (currentPlayerBrightness - eyeAdjustment) * 2.0F);
					r = new ReverseGlowBlur(VisualType.blur, (int) ((currentPlayerBrightness - eyeAdjustment) * 25), new Color(1.0F, 1.0F, 1.0F, 1.0F), true, ConfigCore.blurQuality, (int) (15 * (currentPlayerBrightness - eyeAdjustment)), (currentPlayerBrightness - eyeAdjustment) * 2.0F, (GlowBlur) g);
				} else {
					Visual g = new BoxGlowBlur(VisualType.blur, (int) ((currentPlayerBrightness - eyeAdjustment) * 200), new Color(1.0F, 1.0F, 1.0F, 1.0F), true, ConfigCore.blurQuality, (int) (15 * (currentPlayerBrightness - eyeAdjustment)), 1, (currentPlayerBrightness - eyeAdjustment) * 2.0F);
					r = new BoxReverseGlowBlur(VisualType.blur, (int) ((currentPlayerBrightness - eyeAdjustment) * 25), new Color(1.0F, 1.0F, 1.0F, 1.0F), true, ConfigCore.blurQuality, (int) (15 * (currentPlayerBrightness - eyeAdjustment)), 1, (currentPlayerBrightness - eyeAdjustment) * 2.0F, (BoxGlowBlur) g);
				}
				Base.instance.manager.addVisualDirect(r);
				glowBuffer = (int) ((currentPlayerBrightness - eyeAdjustment) * 200);
			}
			eyeAdjustment += currentPlayerBrightness - eyeAdjustment;
		}
		if(glowBuffer > 0) {
			glowBuffer--;
		}
	}

	@Override
	public void resetEffect() {
		eyeAdjustment = 0.4F;
	}
}
