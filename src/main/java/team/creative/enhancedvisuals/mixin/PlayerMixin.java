package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import team.creative.enhancedvisuals.EnhancedVisuals;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    
    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }
    
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getHealth()F"),
            method = "Lnet/minecraft/world/entity/player/Player;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V")
    private void actuallyHurt(DamageSource damageSource, float f, CallbackInfo info) {
        EnhancedVisuals.EVENTS.damage((Player) (Object) this, damageSource, f);
    }
}
