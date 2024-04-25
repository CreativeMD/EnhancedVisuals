package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import team.creative.enhancedvisuals.EnhancedVisuals;

@Mixin(ShaderInstance.class)
public class ShaderInstanceMixin {
    
    @Redirect(method = "<init>(Lnet/minecraft/server/packs/resources/ResourceProvider;Ljava/lang/String;Lcom/mojang/blaze3d/vertex/VertexFormat;)V", at = @At(value = "NEW",
            target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"))
    public ResourceLocation redirectNewLocationInConstructor(String path) {
        EnhancedVisuals.LOGGER.info("FOUND PATH " + path);
        if (path.contains(":")) {
            path = path.replace("shaders/core/", "");
            String[] parts = path.split(":");
            return new ResourceLocation(parts[0], "shaders/core/" + parts[1]);
        }
        return new ResourceLocation(path);
    }
    
}
