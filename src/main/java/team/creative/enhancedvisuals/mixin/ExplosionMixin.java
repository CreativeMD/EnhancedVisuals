package team.creative.enhancedvisuals.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.phys.AABB;
import team.creative.enhancedvisuals.EnhancedVisuals;

@Mixin(ServerExplosion.class)
public abstract class ExplosionMixin {
    
    @WrapOperation(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"),
            method = "hurtEntities(Ljava/util/List;)V")
    private List<Entity> onDetonate(ServerLevel level, Entity causer, AABB box, Operation<List<Entity>> entity) {
        List<Entity> list = entity.call(level, causer, box);
        EnhancedVisuals.EVENTS.explosion((Explosion) this, list);
        return list;
    }
    
    @WrapOperation(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"),
            method = "hurtEntities()V")
    private List<Entity> onDetonateFabric(ServerLevel level, Entity causer, AABB box, Operation<List<Entity>> entity) {
        List<Entity> list = entity.call(level, causer, box);
        EnhancedVisuals.EVENTS.explosion((Explosion) this, list);
        return list;
    }
}
