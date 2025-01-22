package team.creative.enhancedvisuals.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.ShaderProgram;
import team.creative.enhancedvisuals.client.render.EVRenderer;

@Mixin(CoreShaders.class)
public class CoreShadersMixin {
    
    @Inject(at = @At("HEAD"), method = "getProgramsToPreload()Ljava/util/List;")
    private static void getProgramsToPreload(CallbackInfoReturnable<List<ShaderProgram>> info) {
        EVRenderer.init();
    }
    
}
