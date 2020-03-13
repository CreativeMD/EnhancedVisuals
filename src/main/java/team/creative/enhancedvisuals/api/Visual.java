package team.creative.enhancedvisuals.api;

import java.awt.Color;

import com.creativemd.creativecore.common.config.premade.curve.Curve;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.creative.enhancedvisuals.api.type.VisualType;

public class Visual {
	
	public final VisualType type;
	
	public float opacity;
	
	public final boolean endless;
	public final Curve animation;
	
	private boolean displayed = false;
	private int tick = 0;
	
	public Color color;
	
	public int variant;
	
	public Visual(VisualType type, Curve animation, int variant) {
		this.type = type;
		this.animation = animation;
		this.variant = variant;
		this.endless = false;
	}
	
	public Visual(VisualType type, int variant) {
		this.type = type;
		this.animation = null;
		this.variant = variant;
		this.endless = true;
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
	
	@SideOnly(Side.CLIENT)
	public void render(TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
		type.render(this, manager, screenWidth, screenHeight, partialTicks);
	}
	
	public boolean isVisible() {
		return type.isVisible(this);
	}
	
	public boolean tick() {
		if (endless)
			return true;
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
