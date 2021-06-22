package team.creative.enhancedvisuals.api.type;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.VisualHandler;

public abstract class VisualTypeTexture extends VisualType {
    
    @CreativeConfig
    public int animationSpeed;
    public String domain;
    
    public VisualTypeTexture(VisualCategory category, String name, String domain, int animationSpeed) {
        super(name, category);
        this.domain = domain;
        this.animationSpeed = animationSpeed;
    }
    
    public VisualTypeTexture(VisualCategory category, String name, int animationSpeed) {
        this(category, name, null, animationSpeed);
    }
    
    @OnlyIn(value = Dist.CLIENT)
    public ResourceLocation[] resources;
    @OnlyIn(value = Dist.CLIENT)
    public Dimension dimension;
    
    @Override
    @OnlyIn(value = Dist.CLIENT)
    public void loadResources(IResourceManager manager) {
        String baseLocation = "visuals/" + cat.name() + "/" + name + "/" + name;
        
        ArrayList<ResourceLocation> locations = new ArrayList<>();
        int i = 0;
        ResourceLocation location = null;
        IResource resource = null;
        try {
            while ((resource = manager.getResource((location = new ResourceLocation(domain == null ? EnhancedVisuals.MODID : domain, baseLocation + i + ".png")))) != null) {
                if (i == 0) {
                    BufferedImage image = ImageIO.read(resource.getInputStream());
                    dimension = new Dimension(image.getWidth(), image.getHeight());
                }
                locations.add(location);
                i++;
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        resources = locations.toArray(new ResourceLocation[0]);
        if (resources.length == 0) {
            EnhancedVisuals.LOGGER.warn("Could not find any resources for '" + name + "'!");
        }
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public int getVariantAmount() {
        return resources.length;
    }
    
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getResource(Visual visual) {
        if (animationSpeed > 0) {
            long time = Math.abs(System.nanoTime() / 3000000 / animationSpeed);
            return resources[(int) (time % resources.length)];
        }
        return resources[visual.variant];
    }
    
    @Override
    @OnlyIn(value = Dist.CLIENT)
    public void render(VisualHandler handler, Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
        manager.bind(getResource(visual));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuilder();
        
        int red = visual.color != null ? visual.color.getRed() : 255;
        int green = visual.color != null ? visual.color.getGreen() : 255;
        int blue = visual.color != null ? visual.color.getBlue() : 255;
        double z = -90;
        
        int width = visual.getWidth(screenWidth);
        int height = visual.getHeight(screenHeight);
        
        float opacity = visual.getOpacity();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        renderer.vertex(0.0D, height, z).uv(0.0F, 1.0F).color(red, green, blue, (int) (opacity * 255)).endVertex();
        renderer.vertex(width, height, z).uv(1.0F, 1.0F).color(red, green, blue, (int) (opacity * 255)).endVertex();
        renderer.vertex(width, 0.0D, z).uv(1.0F, 0.0F).color(red, green, blue, (int) (opacity * 255)).endVertex();
        renderer.vertex(0.0D, 0.0D, z).uv(0.0F, 0.0F).color(red, green, blue, (int) (opacity * 255)).endVertex();
        tessellator.end();
    }
    
    @Override
    public int getWidth(int screenWidth) {
        return (dimension.width);
    }
    
    @Override
    public int getHeight(int screenHeight) {
        return (dimension.height);
    }
}
