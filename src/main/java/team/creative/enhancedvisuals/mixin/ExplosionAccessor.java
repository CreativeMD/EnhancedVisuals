package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;

@Mixin(Explosion.class)
public interface ExplosionAccessor {
    @Accessor("x")
    double eh$getX();
    
    @Accessor("y")
    double eh$getY();
    
    @Accessor("z")
    double eh$getZ();
    
    @Accessor("radius")
    float eh$getRadius();
    
    @Accessor("source")
    Entity eh$getSource();
}
