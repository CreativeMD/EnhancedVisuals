package team.creative.enhancedvisuals.api.type;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.creativemd.creativecore.common.config.api.CreativeConfig;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
    
    @SideOnly(Side.CLIENT)
    public ResourceLocation[] resources;
    @SideOnly(Side.CLIENT)
    public Dimension dimension;
    
    @Override
    @SideOnly(Side.CLIENT)
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
    @SideOnly(Side.CLIENT)
    public boolean supportsColor() {
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getVariantAmount() {
        return resources.length;
    }
    
    @SideOnly(Side.CLIENT)
    public ResourceLocation getResource(Visual visual) {
        if (animationSpeed > 0) {
            long time = Math.abs(System.nanoTime() / 3000000 / animationSpeed);
            return resources[(int) (time % resources.length)];
        }
        return resources[visual.variant];
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void render(VisualHandler handler, Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
        manager.bindTexture(getResource(visual));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        
        float red = visual.color != null ? visual.color.getRed() / 255.0F : 1;
        float green = visual.color != null ? visual.color.getGreen() / 255.0F : 1;
        float blue = visual.color != null ? visual.color.getBlue() / 255.0F : 1;
        double z = -90;
        
        int width = visual.getWidth(screenWidth);
        int height = visual.getHeight(screenHeight);
        
        float opacity = visual.getOpacity();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        renderer.pos(0.0D, height, z).tex(0.0D, 1.0D).color(red, green, blue, opacity).endVertex();
        renderer.pos(width, height, z).tex(1.0D, 1.0D).color(red, green, blue, opacity).endVertex();
        renderer.pos(width, 0.0D, z).tex(1.0D, 0.0D).color(red, green, blue, opacity).endVertex();
        renderer.pos(0.0D, 0.0D, z).tex(0.0D, 0.0D).color(red, green, blue, opacity).endVertex();
        tessellator.draw();
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
