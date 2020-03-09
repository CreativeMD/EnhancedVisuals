package team.creative.enhancedvisuals.common.event;

import java.lang.reflect.Field;

import com.creativemd.creativecore.common.packet.PacketHandler;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
				ExplosionPacket packet = new ExplosionPacket(event.getExplosion().getPosition(), size.getFloat(event.getExplosion()), ((Entity) exploder.get(event.getExplosion())).getEntityId());
				for (Entity entity : event.getAffectedEntities())
					if (entity instanceof EntityPlayerMP)
						PacketHandler.sendPacketToPlayer(packet, (EntityPlayerMP) entity);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@SubscribeEvent
	public void impact(ProjectileImpactEvent.Throwable event) {
		if (!event.getEntity().world.isRemote)
			if (VisualHandlers.POTION.isEnabled(Minecraft.getMinecraft().player))
				VisualHandlers.POTION.impact(event);
			
	}
	
	@SubscribeEvent
	public void damage(LivingDamageEvent event) {
		if (event.getEntity() instanceof EntityPlayer)
			PacketHandler.sendPacketToPlayer(new DamagePacket(event), (EntityPlayerMP) event.getEntity());
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void clientTick(ClientTickEvent event) {
		if (event.phase == Phase.START && EVClient.shouldTick()) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			VisualManager.onTick(player);
			
			SoundMuteHandler.tick();
		}
	}
	
	public static boolean areEyesInWater(EntityPlayer player) {
		return player.isInsideOfMaterial(Material.WATER);
	}
}
