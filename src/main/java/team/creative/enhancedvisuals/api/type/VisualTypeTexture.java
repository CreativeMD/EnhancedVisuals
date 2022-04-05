package team.creative.enhancedvisuals.api.type;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.client.render.TextureCache;

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
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public TextureCache[] resources;
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public Dimension dimension;
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void loadResources(ResourceManager manager) {
        String baseLocation = "visuals/" + cat.name() + "/" + name + "/" + name;
        
        List<TextureCache> caches = new ArrayList<>();
        int i = 0;
        TextureCache resource = null;
        String domain = this.domain == null ? EnhancedVisuals.MODID : this.domain;
        try {
            while ((resource = TextureCache.parse(manager, domain, baseLocation + i)) != null) {
                if (i == 0) {
                    BufferedImage image = ImageIO.read(manager.getResource(resource.getFirst()).getInputStream());
                    dimension = new Dimension(image.getWidth(), image.getHeight());
                }
                caches.add(resource);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resources = caches.toArray(new TextureCache[0]);
        if (resources.length == 0)
            EnhancedVisuals.LOGGER.warn("Could not find any resources for '" + name + "'!");
        
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public int getVariantAmount() {
        return resources.length;
    }
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getResource(Visual visual) {
        if (animationSpeed > 0) {
            long time = Math.abs(System.nanoTime() / 3000000 / animationSpeed);
            return resources[(int) (time % resources.length)].getResource();
        }
        return resources[visual.variant].getResource();
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void render(VisualHandler handler, Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
        RenderSystem.setShaderTexture(0, getResource(visual));
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder renderer = tessellator.getBuilder();
        
        int red = visual.color != null ? visual.color.getRed() : 255;
        int green = visual.color != null ? visual.color.getGreen() : 255;
        int blue = visual.color != null ? visual.color.getBlue() : 255;
        double z = -90;
        
        int width = visual.getWidth(screenWidth);
        int height = visual.getHeight(screenHeight);
        
        float opacity = visual.getOpacity();
        renderer.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
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
