package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import team.creative.enhancedvisuals.client.render.EVRenderer;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "Lnet/minecraft/client/Minecraft;runTick(Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;render(FJZ)V", shift = At.Shift.AFTER), require = 1)
    private void afterRenderGui(CallbackInfo ci) {
        EVRenderer.render();
    }
}
