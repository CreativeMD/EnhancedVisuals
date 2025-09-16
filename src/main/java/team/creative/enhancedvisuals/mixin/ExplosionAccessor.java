package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;

@Mixin(Explosion.class)
public interface ExplosionAccessor {
    @Accessor("x")
    @Unique
    double getX();
    
    @Accessor("y")
    @Unique
    double getY();
    
    @Accessor("z")
    @Unique
    double getZ();
    
    @Accessor
    @Unique
    float getRadius();
    
    @Accessor
    @Unique
    Entity getSource();
}
