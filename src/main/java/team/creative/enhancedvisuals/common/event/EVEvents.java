package team.creative.enhancedvisuals.common.event;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.Explosion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.client.sound.SoundMuteHandler;
import team.creative.enhancedvisuals.common.packet.ExplosionPacket;

public class EVEvents {
	
	private Field size = ObfuscationReflectionHelper.findField(Explosion.class, "size");
	private Field exploder = ObfuscationReflectionHelper.findField(Explosion.class, "exploder");
	
	@SubscribeEvent
	public void explosion(ExplosionEvent.Detonate event) {
		if (!event.getWorld().isRemote) {
			try {
				ExplosionPacket packet = new ExplosionPacket(event.getExplosion().getPosition(), size.getFloat(event.getExplosion()), ((Entity) exploder.get(event.getExplosion())).getEntityId());
				for (Entity entity : event.getAffectedEntities())
					if (entity instanceof ServerPlayerEntity)
						EnhancedVisuals.NETWORK.sendToClient(packet, entity);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@SubscribeEvent
	public void hurt(LivingHurtEvent source) {
		
	}
	
	@SubscribeEvent
	public void damage(LivingDamageEvent source) {
		
	}
	
	@SubscribeEvent
	@OnlyIn(value = Dist.CLIENT)
	public void clientTick(ClientTickEvent event) {
		if (event.phase == Phase.START && EVClient.shouldTick()) {
			VisualManager.onTick(Minecraft.getInstance().player);
			
			SoundMuteHandler.tick();
		}
	}
	
}
