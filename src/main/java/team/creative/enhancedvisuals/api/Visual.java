package team.creative.enhancedvisuals.api;

import java.awt.Color;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.creativecore.common.config.premade.curve.Curve;
import team.creative.enhancedvisuals.api.type.VisualType;

public class Visual {
	
	public final VisualType type;
	
	public float opacity;
	
	public final Curve animation;
	
	private boolean displayed = false;
	private int tick = 0;
	
	public Color color;
	
	public int variant;
	
	public Visual(VisualType type, Curve animation, int variant) {
		this.type = type;
		this.animation = animation;
		this.variant = variant;
	}
	
	public boolean displayed() {
		return displayed;
	}
	
	public void addToDisplay() {
		displayed = true;
	}
	
	public void removeFromDisplay() {
		displayed = false;
	}
	
	public VisualCategory getCategory() {
		return type.cat;
	}
	
	@OnlyIn(value = Dist.CLIENT)
	public void render(TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
		type.render(this, manager, screenWidth, screenHeight, partialTicks);
	}
	
	public boolean isVisible() {
		return type.isVisible(this);
	}
	
	public boolean tick() {
		opacity = (float) animation.valueAt(tick++);
		return opacity > 0;
	}
	
	public int getWidth(int screenWidth) {
		return screenWidth;
	}
	
	public int getHeight(int screenHeight) {
		return screenHeight;
	}
	
	public boolean isAffectedByWater() {
		return type.isAffectedByWater();
	}
	
}
