package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import team.creative.enhancedvisuals.EnhancedVisuals;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player {
	public MixinServerPlayer(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
		super(level, blockPos, f, gameProfile);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), method = "Lnet/minecraft/server/level/ServerPlayer;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
	private void onHurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
		EnhancedVisuals.EVENTS.damage(this, damageSource, f);
	}
}
