package team.creative.enhancedvisuals.api.type;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.client.render.EVRenderer;
import team.creative.enhancedvisuals.client.render.TextureCache;

public abstract class VisualTypeTexture extends VisualType {
    
    private static final float DEFAULT_PARTICLE_SIZE = 0.15F;
    
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
    @OnlyIn(Dist.CLIENT)
    public float ratio;
    
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
    public void render(PoseStack pose, VisualHandler handler, Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
        RenderSystem.setShaderTexture(0, getResource(visual));
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder renderer = tessellator.getBuilder();
        
        RenderSystem.setShader(EVRenderer::getPositionTexColorSmoothShader);
        Matrix4f last = pose.last().pose();
        
        int red = visual.color != null ? visual.color.getRed() : 255;
        int green = visual.color != null ? visual.color.getGreen() : 255;
        int blue = visual.color != null ? visual.color.getBlue() : 255;
        float z = -90;
        
        int width = visual.getWidth(screenWidth);
        int height = visual.getHeight(screenHeight);
        
        float opacity = visual.getOpacity();
        renderer.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        renderer.vertex(last, 0.0f, height, z).uv(0.0F, 1.0F).color(red, green, blue, (int) (opacity * 255F)).endVertex();
        renderer.vertex(last, width, height, z).uv(1.0F, 1.0F).color(red, green, blue, (int) (opacity * 255F)).endVertex();
        renderer.vertex(last, width, 0.0f, z).uv(1.0F, 0.0F).color(red, green, blue, (int) (opacity * 255F)).endVertex();
        renderer.vertex(last, 0.0f, 0.0f, z).uv(0.0F, 0.0F).color(red, green, blue, (int) (opacity * 255F)).endVertex();
        tessellator.end();
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
