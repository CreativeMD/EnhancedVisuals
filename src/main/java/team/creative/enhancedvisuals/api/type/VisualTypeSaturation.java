package team.creative.enhancedvisuals.api.type;

import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.creative.enhancedvisuals.api.Visual;

public class VisualTypeSaturation extends VisualTypeShader {
    
    public VisualTypeSaturation(String name) {
        super(name, new ResourceLocation("shaders/post/desaturate.json"));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void changeProperties(float intensity) {
        for (Shader mcShader : shaderGroup.getShaders()) {
            ShaderUniform shaderuniform = mcShader.getShaderManager().getShaderUniform("Saturation");
            
            if (shaderuniform != null)
                shaderuniform.set(intensity);
        }
    }
    
    @Override
    public boolean isVisible(Visual visual) {
        return visual.opacity != 1;
    }
}
