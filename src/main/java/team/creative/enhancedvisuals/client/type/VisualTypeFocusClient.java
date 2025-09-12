package team.creative.enhancedvisuals.client.type;

import java.nio.ByteBuffer;
import java.util.Map;

import org.lwjgl.system.MemoryStack;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.PostPass;
import team.creative.enhancedvisuals.api.type.VisualTypeFocus;
import team.creative.enhancedvisuals.mixin.PostChainAccessor;
import team.creative.enhancedvisuals.mixin.PostPassAccessor;

public class VisualTypeFocusClient extends VisualTypeShaderClient<VisualTypeFocus> {
    
    private static final int UNIFORM_SIZE = new Std140SizeCalculator().putFloat().get();
    
    public VisualTypeFocusClient(VisualTypeFocus type) {
        super(type);
    }
    
    @Override
    public void changeProperties(float intensity) {
        for (PostPass pass : ((PostChainAccessor) postChain).getPasses()) {
            Map<String, GpuBuffer> uniforms = ((PostPassAccessor) pass).getCustomUniforms();
            
            uniforms.compute("RadiusConfig", (String key, GpuBuffer buffer) -> {
                if (buffer == null)
                    buffer = RenderSystem.getDevice().createBuffer(() -> "Blobs2 / RadiusConfig", GpuBuffer.USAGE_COPY_DST | GpuBuffer.USAGE_UNIFORM, UNIFORM_SIZE);
                try (MemoryStack memoryStack = MemoryStack.stackPush()) {
                    ByteBuffer byteBuffer = Std140Builder.onStack(memoryStack, UNIFORM_SIZE).putFloat(intensity).get();
                    RenderSystem.getDevice().createCommandEncoder().writeToBuffer(buffer.slice(), byteBuffer);
                }
                return buffer;
            });
        }
    }
    
}
