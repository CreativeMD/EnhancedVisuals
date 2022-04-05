package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import team.creative.enhancedvisuals.EnhancedVisuals;

@Mixin(Projectile.class)
public class ProjectileMixin {
	@Inject(at=@At("HEAD"), method="Lnet/minecraft/world/entity/projectile/Projectile;onHit(Lnet/minecraft/world/phys/HitResult;)V")
	private void onHit(HitResult hitResult, CallbackInfo ci) {
		EnhancedVisuals.EVENTS.impact((Projectile) (Object) this);
	}

}
