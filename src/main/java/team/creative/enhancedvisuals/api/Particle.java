package team.creative.enhancedvisuals.api;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import net.minecraft.client.renderer.texture.TextureManager;
import team.creative.creativecore.common.config.premade.curve.Curve;
import team.creative.enhancedvisuals.api.type.VisualType;

public class Particle extends Visual {
    
    public int x;
    public int y;
    
    public int width;
    public int height;
    
    public float rotation;
    
    public Particle(VisualType type, VisualHandler handler, Curve animation, int x, int y, int width, int height, float rotation, int variant) {
        super(type, handler, animation, variant);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }
    
    @Override
    public void render(PoseStack stack, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
        stack.translate(x + width / 2, y + height / 2, 0);
        stack.mulPose(Quaternion.fromXYZ(0, 0, (float) Math.toRadians(rotation)));
        RenderSystem.applyModelViewMatrix();
        super.render(stack, manager, screenWidth, screenHeight, partialTicks);
    }
    
    @Override
    public int getWidth(int screenWidth) {
        return width;
    }
    
    @Override
    public int getHeight(int screenHeight) {
        return height;
    }
    
}
