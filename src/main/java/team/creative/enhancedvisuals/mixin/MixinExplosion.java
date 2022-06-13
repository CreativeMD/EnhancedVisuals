package team.creative.enhancedvisuals.mixin;

import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.world.level.Explosion;
import team.creative.enhancedvisuals.EnhancedVisuals;

@Mixin(Explosion.class)
public class MixinExplosion {
    
    @Inject(at = @At(value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"),
            method = "Lnet/minecraft/world/level/Explosion;explode()V", locals = LocalCapture.CAPTURE_FAILHARD, require = 1)
    private void onDetonate(CallbackInfo ci, Set set, int i, float f2, int k1, int l1, int i2, int i1, int j2, int j1, List list) {
        EnhancedVisuals.EVENTS.explosion((Explosion) (Object) this, list);
    }
}
