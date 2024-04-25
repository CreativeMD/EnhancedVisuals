package team.creative.enhancedvisuals.mixin;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.shaders.Program;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.client.render.EVRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    
    @Inject(method = "reloadShaders(Lnet/minecraft/server/packs/resources/ResourceProvider;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GameRenderer;shutdownShaders()V"), locals = LocalCapture.CAPTURE_FAILHARD, require = 1)
    private void reloadShaders(ResourceProvider provier, CallbackInfo info, List<Program> programs, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaders) {
        try {
            EVRenderer.loadShaders(provier, shaders);
        } catch (IOException e) {
            EnhancedVisuals.LOGGER.error(e);
        }
    }
    
}
