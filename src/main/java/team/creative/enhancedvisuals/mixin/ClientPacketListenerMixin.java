package team.creative.enhancedvisuals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import team.creative.enhancedvisuals.EnhancedVisuals;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    
    @Inject(method = "handleRespawn(Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;)V", at = @At("TAIL"))
    public void handleRespawn(ClientboundRespawnPacket packet, CallbackInfo info) {
        EnhancedVisuals.EVENTS.respawn();
    }
    
}
