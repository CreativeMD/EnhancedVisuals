package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
    
    protected MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }
    
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getHealth()F"),
            method = "Lnet/minecraft/world/entity/player/Player;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V")
    private void actuallyHurt(DamageSource damageSource, float f, CallbackInfo info) {
        EnhancedVisuals.EVENTS.damage((Player) (Object) this, damageSource, f);
    }
    
    @Inject(at = @At("HEAD"), require = 1, method = "animateHurt(F)V")
    @OnlyIn(Dist.CLIENT)
    @Environment(EnvType.CLIENT)
    public void animateHurt(float yaw, CallbackInfo info) {
        if ((Player) (Object) this instanceof LocalPlayer)
            VisualHandlers.DAMAGE.clientHurt();
    }
}
