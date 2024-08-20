package team.creative.enhancedvisuals.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import team.creative.enhancedvisuals.EnhancedVisuals;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"), method = "Lnet/minecraft/world/level/Explosion;explode()V", require = 1)
    private List<Entity> onDetonate(Level world, Entity causer, AABB box) {
        List<Entity> list = world.getEntities(causer, box);
        EnhancedVisuals.EVENTS.explosion((Explosion) (Object) this, list);
        return list;
    }
}
