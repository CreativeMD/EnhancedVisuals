package team.creative.enhancedvisuals.client.type;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import team.creative.creativecore.client.render.gui.CreativeGuiGraphics;
import team.creative.creativecore.common.util.mc.ColorUtils;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualTypeTexture;
import team.creative.enhancedvisuals.client.render.TextureCache;

public class VisualTypeTextureClient<T extends VisualTypeTexture> extends VisualTypeClient<T> {
    
    private static final float DEFAULT_PARTICLE_SIZE = 0.15F;
    
    public TextureCache[] resources;
    public Dimension dimension;
    public float ratio;
    
    public VisualTypeTextureClient(T type) {
        super(type);
    }
    
    @Override
    public void loadResources(ResourceManager manager) {
        String baseLocation = "visuals/" + type.cat.name() + "/" + type.name + "/" + type.name;
        
        List<TextureCache> caches = new ArrayList<>();
        int i = 0;
        TextureCache resource = null;
        String domain = type.domain == null ? EnhancedVisuals.MODID : type.domain;
        try {
            while ((resource = TextureCache.parse(manager, domain, baseLocation + i)) != null) {
                if (i == 0) {
                    Optional<Resource> re = manager.getResource(resource.getFirst());
                    if (re.isPresent()) {
                        InputStream input = re.orElseThrow().open();
                        try {
                            BufferedImage image = ImageIO.read(input);
                            dimension = new Dimension(image.getWidth(), image.getHeight());
                            ratio = dimension.width / (float) dimension.height;
                        } finally {
                            input.close();
                        }
                    }
                }
                caches.add(resource);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resources = caches.toArray(new TextureCache[0]);
        if (resources.length == 0)
            EnhancedVisuals.LOGGER.warn("Could not find any resources for '" + type.name + "'!");
        
    }
    
    @Override
    public int getVariantAmount() {
        return resources.length;
    }
    
    public Identifier getResource(Visual visual) {
        if (type.animationSpeed > 0) {
            long time = Math.abs(System.nanoTime() / 3000000 / type.animationSpeed);
            return resources[(int) (time % resources.length)].getResource();
        }
        return resources[visual.variant].getResource();
    }
    
    @Override
    public void render(GuiGraphics graphics, VisualHandler handler, Visual visual, int screenWidth, int screenHeight, float partialTicks) {
        int red = visual.color != null ? visual.color.getRed() : 255;
        int green = visual.color != null ? visual.color.getGreen() : 255;
        int blue = visual.color != null ? visual.color.getBlue() : 255;
        
        int width = visual.getWidth(screenWidth);
        int height = visual.getHeight(screenHeight);
        
        float opacity = visual.getOpacity();
        
        int color = ColorUtils.rgba(red, green, blue, (int) (opacity * 255));
        ((CreativeGuiGraphics) graphics).textureRectColorStretched(getResource(visual), 0, 0, width, height, color);
    }
    
    @Override
    public int getWidth(int screenWidth, int screenHeight) {
        return (int) (screenHeight * DEFAULT_PARTICLE_SIZE * ratio);
    }
    
    @Override
    public int getHeight(int screenWidth, int screenHeight) {
        return (int) (screenHeight * DEFAULT_PARTICLE_SIZE);
    }
}
