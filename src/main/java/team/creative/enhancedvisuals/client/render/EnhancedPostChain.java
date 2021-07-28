package team.creative.enhancedvisuals.client.render;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;

import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class EnhancedPostChain extends PostChain {
    
    public EnhancedPostChain(TextureManager p_i1050_1_, ResourceManager resourceManagerIn, RenderTarget mainFramebufferIn, ResourceLocation p_i1050_4_) throws IOException, JsonSyntaxException {
        super(p_i1050_1_, resourceManagerIn, mainFramebufferIn, p_i1050_4_);
    }
    
    private static Field passes = ObfuscationReflectionHelper.findField(PostChain.class, "f_110009_");
    
    public List<PostPass> getPasses() {
        try {
            return (List<PostPass>) passes.get(this);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new ArrayList<>();
        }
    }
}
