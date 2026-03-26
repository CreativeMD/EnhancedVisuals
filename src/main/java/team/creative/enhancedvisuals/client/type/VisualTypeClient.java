package team.creative.enhancedvisuals.client.type;

import com.mojang.blaze3d.pipeline.RenderTarget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.server.packs.resources.ResourceManager;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;

public abstract class VisualTypeClient<T extends VisualType> {
    
    public final T type;
    
    public VisualTypeClient(T type) {
        this.type = type;
    }
    
    public abstract void loadResources(ResourceManager manager);
    
    public abstract void render(GuiGraphicsExtractor graphics, VisualHandler handler, Visual visual, int screenWidth, int screenHeight, float partialTicks);
    
    public int getVariantAmount() {
        return 1;
    }
    
    public void resize(RenderTarget buffer) {}
    
    public int getWidth(int screenWidth, int screenHeight) {
        return screenWidth;
    }
    
    public int getHeight(int screenWidth, int screenHeight) {
        return screenHeight;
    }
    
}
