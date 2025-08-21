package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import team.creative.enhancedvisuals.client.mc.GameRendererExtender;
import team.creative.enhancedvisuals.client.render.EVRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements GameRendererExtender {
    
    @Shadow
    @Final
    private CrossFrameResourcePool resourcePool;
    
    @Inject(method = "render(Lnet/minecraft/client/DeltaTracker;Z)V", require = 1, at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/resource/CrossFrameResourcePool;endFrame()V"))
    public void renderEnd(DeltaTracker deltaTracker, boolean bl, CallbackInfo info) {
        EVRenderer.renderShaders(deltaTracker);
    }
    
    @Override
    public CrossFrameResourcePool getResourcePool() {
        return resourcePool;
    }
    
}
