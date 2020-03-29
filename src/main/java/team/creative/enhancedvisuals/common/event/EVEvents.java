package team.creative.enhancedvisuals.common.event;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.Explosion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.client.sound.SoundMuteHandler;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;
import team.creative.enhancedvisuals.common.packet.DamagePacket;
import team.creative.enhancedvisuals.common.packet.ExplosionPacket;

public class EVEvents {
	
	private Field size = ObfuscationReflectionHelper.findField(Explosion.class, "field_77280_f");
	private Field exploder = ObfuscationReflectionHelper.findField(Explosion.class, "field_77283_e");
	
	@SubscribeEvent
	public void explosion(ExplosionEvent.Detonate event) {
		if (!event.getWorld().isRemote) {
			try {
				ExplosionPacket packet = new ExplosionPacket(event.getExplosion().getPosition(), size.getFloat(event.getExplosion()), (Entity) exploder.get(event.getExplosion()) != null ? ((Entity) exploder.get(event.getExplosion())).getEntityId() : -1);
				for (Entity entity : event.getAffectedEntities())
					if (entity instanceof ServerPlayerEntity)
						EnhancedVisuals.NETWORK.sendToClient(packet, (ServerPlayerEntity) entity);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@SubscribeEvent
	@OnlyIn(value = Dist.CLIENT)
	public void impact(ProjectileImpactEvent.Throwable event) {
		if (!event.getEntity().world.isRemote)
			if (VisualHandlers.POTION.isEnabled(Minecraft.getInstance().player))
				VisualHandlers.POTION.impact(event);
			
	}
	
	@SubscribeEvent
	public void damage(LivingDamageEvent event) {
		if (event.getEntity() instanceof PlayerEntity)
			EnhancedVisuals.NETWORK.sendToClient(new DamagePacket(event), (ServerPlayerEntity) event.getEntity());
	}
	
	@SubscribeEvent
	@OnlyIn(value = Dist.CLIENT)
	public void clientTick(ClientTickEvent event) {
		if (event.phase == Phase.START && EVClient.shouldTick()) {
			PlayerEntity player = Minecraft.getInstance().player;
			VisualManager.onTick(player);
			
			SoundMuteHandler.tick();
		}
	}
	
	private static Field eyesInWaterField = ObfuscationReflectionHelper.findField(Entity.class, "field_205013_W");
	
	public static boolean areEyesInWater(PlayerEntity player) {
		try {
			return eyesInWaterField.getBoolean(player);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
}
