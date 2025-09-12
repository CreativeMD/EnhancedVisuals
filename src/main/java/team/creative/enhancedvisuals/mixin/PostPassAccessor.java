package team.creative.enhancedvisuals.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.blaze3d.buffers.GpuBuffer;

import net.minecraft.client.renderer.PostPass;

@Mixin(PostPass.class)
public interface PostPassAccessor {
    
    @Accessor
    public Map<String, GpuBuffer> getCustomUniforms();
}
