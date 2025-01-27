package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import team.creative.enhancedvisuals.EnhancedVisuals;

@Mixin(Player.class)
public abstract class PlayerMixin {
    
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getHealth()F"),
            method = "Lnet/minecraft/world/entity/player/Player;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V",
            require = 1)
    private void actuallyHurt(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfo info) {
        EnhancedVisuals.EVENTS.damage((Player) (Object) this, damageSource, f);
    }
}
