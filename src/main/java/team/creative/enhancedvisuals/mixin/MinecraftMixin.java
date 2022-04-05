package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import team.creative.enhancedvisuals.client.render.EVRenderer;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(
			method = "Lnet/minecraft/client/Minecraft;runTick(Z)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/toasts/ToastComponent;render(Lcom/mojang/blaze3d/vertex/PoseStack;)V", shift = At.Shift.AFTER),
	locals = LocalCapture.CAPTURE_FAILHARD)
	private void afterRenderGui(boolean bl, CallbackInfo ci, long l, PoseStack poseStack) {
		EVRenderer.render(poseStack, Minecraft.getInstance().getFrameTime());
	}
}
