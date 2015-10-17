package com.sonicjumper.enhancedvisuals.event;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicates;
import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.util.SplatUtil;
import com.sonicjumper.enhancedvisuals.visuals.Shader;
import com.sonicjumper.enhancedvisuals.visuals.ShaderBlurFade;
import com.sonicjumper.enhancedvisuals.visuals.Splat;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

public class VisualEventHandler {
	
	public Minecraft mc = Minecraft.getMinecraft();
	// Swords and Axes are slashing
	// Shovels and Pickaxes are impact
	// Hoes and Arrows are piercing

	private ArrayList<Item> sharpList;
	private ArrayList<Item> bluntList;
	private ArrayList<Item> pierceList;

	//private HashMap<EntityLivingBase, Float> entityHealthMap;

	private Random rand;

	//private float playerWetness = 0.5F;
	//private float playerTemp = 1.0F;
	//private float eyeAdjustment = 0.4F;
	//private int glowBuffer;
	private int lowHealthBuffer;

	public VisualEventHandler() {
		sharpList = new ArrayList<Item>();
		bluntList = new ArrayList<Item>();
		pierceList = new ArrayList<Item>();

		sharpList.add(Items.iron_sword);
		sharpList.add(Items.wooden_sword);
		sharpList.add(Items.stone_sword);
		sharpList.add(Items.diamond_sword);
		sharpList.add(Items.golden_sword);
		sharpList.add(Items.iron_axe);
		sharpList.add(Items.wooden_axe);
		sharpList.add(Items.stone_axe);
		sharpList.add(Items.diamond_axe);
		sharpList.add(Items.golden_axe);

		bluntList.add(Items.iron_pickaxe);
		bluntList.add(Items.wooden_pickaxe);
		bluntList.add(Items.stone_pickaxe);
		bluntList.add(Items.diamond_pickaxe);
		bluntList.add(Items.golden_pickaxe);
		bluntList.add(Items.iron_shovel);
		bluntList.add(Items.wooden_shovel);
		bluntList.add(Items.stone_shovel);
		bluntList.add(Items.diamond_shovel);
		bluntList.add(Items.golden_shovel);

		pierceList.add(Items.iron_hoe); 
		pierceList.add(Items.wooden_hoe);
		pierceList.add(Items.stone_hoe);
		pierceList.add(Items.diamond_hoe);
		pierceList.add(Items.golden_hoe);
		pierceList.add(Items.arrow);

		//entityHealthMap = new HashMap<EntityLivingBase, Float>();

		rand = new Random();
	}

	/*@SubscribeEvent
	public void onPlayerDamage(LivingHurtEvent e) {
		if (e.source.equals(na.i)) {
			return;
		}
		if (((e.entityLiving instanceof ue)) && (((ue)e.entityLiving).equals(this.mc.h)))
		{
			ue p = (ue)e.entityLiving;
			if ((e.source.i() != null) && ((e.source.i() instanceof oe)))
			{
				oe elb = (oe)e.source.i();
				if (((elb.aY() != null) && (isSharp(elb.aY().b()))) || ((elb instanceof tv))) {
					VisualManager.createVisualFromDamage(VisualType.slash, e.ammount, e.entityLiving);
				} else if (((elb.aY() != null) && (isBlunt(elb.aY().b()))) || ((elb instanceof ru))) {
					VisualManager.createVisualFromDamage(VisualType.impact, e.ammount, e.entityLiving);
				} else if (((elb.aY() != null) && (isPierce(elb.aY().b()))) || ((elb instanceof ts)) || ((elb instanceof se))) {
					VisualManager.createVisualFromDamage(VisualType.pierce, e.ammount, e.entityLiving);
				} else {
					VisualManager.createVisualFromDamage(VisualType.splatter, e.ammount, e.entityLiving);
				}
			}
			if (((e.source.i() != null) && ((e.source.i() instanceof ug))) || (e.source.equals(na.g))) {
				VisualManager.createVisualFromDamage(VisualType.pierce, e.ammount, e.entityLiving);
			}
			if ((e.source.equals(na.h)) || (e.source.equals(na.n))) {
				VisualManager.createVisualFromDamage(VisualType.impact, e.ammount, e.entityLiving);
			}
			if (e.source.c()) {
				if ((e.source.h() != null) && (e.source.h().d(this.mc.h) < 16.0D))
				{
					VisualManager.createVisualFromDamageAndDistance(VisualType.dust, e.ammount, e.entityLiving, e.source.h().e(this.mc.h));
					Blur b = new BoxBlur(VisualType.blur, (int)(e.ammount * 10.0F), new Color(1.0F, 1.0F, 1.0F, 0.8F), 10, 1, true, ConfigCore.blurQuality);
					VisualManager.addVisual(b);
				}
				else
				{
					VisualManager.createVisualFromDamage(VisualType.dust, e.ammount, e.entityLiving);
					Blur b = new BoxBlur(VisualType.blur, (int)(e.ammount * 10.0F), new Color(1.0F, 1.0F, 1.0F, 0.8F), 10, 1, true, ConfigCore.blurQuality);
					VisualManager.addVisual(b);
				}
			}
			if (getOverlayFromSource(e.source) != null) {
				VisualManager.addVisualsWithColor(getOverlayFromSource(e.source), 1, (int)(e.ammount * 10.0F), (int)(e.ammount * 15.0F), new Color(1.0F, 1.0F, 1.0F, 0.6F));
			}
			if (e.source.equals(na.e)) {
				VisualManager.addRandomNumVisualsWithColor(VisualType.waterS, 4, 8, (int)(e.ammount * 10.0F), (int)(e.ammount * 15.0F), new Color(1.0F, 1.0F, 1.0F, 1.0F));
			}
		}
		else if (this.mc.h != null)
		{
			na ds = e.source;
			if ((ds.equals(na.m)) || (ds.equals(na.h)) || (ds.equals(na.n)) || (ds.n().equals("mob")) || (ds.n().equals("player"))) {
				if (e.entityLiving.d(this.mc.h) < 8.0D) {
					VisualManager.createVisualFromDamageAndDistance(VisualType.splatter, e.ammount, e.entityLiving, e.entityLiving.e(this.mc.h));
				}
			}
		}
	}*/
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerDamage(LivingAttackEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			entityDamaged(event.entityLiving, event.source, event.ammount);
		}
	}
	
	/*@SubscribeEvent
	public void onEntityReceiveDamage(LivingHurtEvent e) {
		EntityLivingBase entity = e.entityLiving;
		float ammount = e.ammount;
		DamageSource source = e.source;
		Base.log.info("LivingHurtEvent thrown; Entity: " + entity.getName() + "; Source: " + source.getDamageType() + "; Ammount: " + ammount);
		if(e.source.getEntity() != null) {
			Base.log.info("Source Entity: " + e.source.getEntity().getName());
		} else {
			Base.log.info("Source Entity: null source entity");
		}
	}*/

	@SubscribeEvent
	public void playerTickEvent(PlayerTickEvent event) {
		if(event.phase.equals(Phase.END)) {
			onTickInGame();
			/*if(Keyboard.isKeyDown(Keyboard.KEY_0)) {
				/*boolean doesBlurExist = false;
				for(Shader s : Base.instance.manager.getActiveShaders()) {
					if(s instanceof ShaderBlurFade) {
						doesBlurExist = true;
					}
				}
				if(!doesBlurExist) {
					Base.log.info("Creating new blur shader");
					Shader s = new ShaderBlurFade(VisualType.blur, 100, 10.0F);
					Base.instance.manager.addVisualDirect(s);
				}
			}*/
		}
	}

	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent e) {
		if(e.entityLiving.equals(mc.thePlayer)) {
			Base.instance.manager.clearAllVisuals();
			//this.playerWetness = 0.5F;
			//this.playerTemp = 1.0F;
			//this.eyeAdjustment = 0.4F;
		}
	}

	/*@SubscribeEvent
	public void onEntityTakeDamage(LivingHurtEvent event) {
		
		//System.out.println("Entity took damage: " + event.toString());
		//TODO See if this event fires on multiplayer damage events. Historically it hasn't, but maybe it's updated
	}*/

	/*@SubscribeEvent
	public void onExplosion(ExplosionEvent event) {
		//System.out.println("Explosion occurred: " + event.toString());
		//TODO See if this event fires on multiplayer explosions. If it does, then use this for explosion dust control
	}*/

	private void entityDamaged(EntityLivingBase entity, DamageSource source, float damage) {
		//Base.log.info("Damage amount:" + damage + " called for entity " + entity.toString());
		/*for(Point2D point : SplatUtil.generateRandomSplatStreak(25)) {
			Splat s = new Splat(VisualType.splatter, 200, Color.WHITE, (float) point.getX(), (float) point.getY());
			Base.instance.manager.addVisualDirect(s);
		}*/
		// Check distance to player and use that as splat distance pattern
		double distanceSq = Minecraft.getMinecraft().thePlayer.getDistanceSqToEntity(entity);
		if(distanceSq > 64.0D) {
			return;
		}
		Entity attacker = source.getSourceOfDamage();
		if(attacker instanceof EntityLivingBase) {
			EntityLivingBase lastAttacker = (EntityLivingBase) attacker;
			// Check weapons
			if(lastAttacker.getHeldItem() != null) {
				if(isSharp(lastAttacker.getHeldItem().getItem())) {
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.slash, damage, entity, distanceSq);
				} else if(isBlunt(lastAttacker.getHeldItem().getItem())) {
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.impact, damage, entity, distanceSq);
				} else if(isPierce(lastAttacker.getHeldItem().getItem())) {
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.pierce, damage, entity, distanceSq);
				} else {
					// Default to splatter type
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.splatter, damage, entity, distanceSq);
				}
			} else {
				// Then check entity type if not holding a weapon(aka bare hands)
				if(lastAttacker instanceof EntityZombie || lastAttacker instanceof EntitySkeleton || lastAttacker instanceof EntityOcelot) {
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.slash, damage, entity, distanceSq);
				} else if(lastAttacker instanceof EntityGolem || lastAttacker instanceof EntityPlayer) {
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.impact, damage, entity, distanceSq);
				} else if(lastAttacker instanceof EntityWolf || lastAttacker instanceof EntitySpider) {
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.pierce, damage, entity, distanceSq);
				} else if(source.isExplosion()) {
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.impact, damage, entity, distanceSq);
					// Assume explosion damage, only apply dust effect when it's player who is damaged
					if(entity.equals(Minecraft.getMinecraft().thePlayer)) {
						Base.instance.manager.createVisualFromDamageAndDistance(VisualType.dust, damage, entity, distanceSq);
					}
				} else {
					// Default to splatter type
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.splatter, damage, entity, distanceSq);
				}
			}
		} else {
			// For now, just assume damage was another source(falling, drowning, cactus, etc.) and use splatter
			Base.instance.manager.createVisualFromDamageAndDistance(VisualType.splatter, damage, entity, distanceSq);
		}
	}

	public void onTickInGame() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		// Tick all visuals
		ArrayList<Visual> vList = Base.instance.manager.getActiveVisuals();
		for (int i = 0; i < vList.size(); i++) {
			Visual v = (Visual) vList.get(i);
			v.tickUpdate();
		}
		/*// Sample health values of all entities, if an entity has lost health, then throw a damage event
		try {
			ArrayList<EntityLivingBase> entitiesInWorld = (ArrayList<EntityLivingBase>) Minecraft.getMinecraft().theWorld.getEntities(EntityLivingBase.class, Predicates.alwaysTrue());
			for(int i = 0; i < entitiesInWorld.size(); i++) {
				EntityLivingBase entity = entitiesInWorld.get(i);
				if(!entityHealthMap.containsKey(entity)) {
					if(entity instanceof EntityPlayer) {
						entityHealthMap.put(entity, 0.0F);
					} else {
						entityHealthMap.put(entity, entity.getHealth());
					}
				} else {
					if(entityHealthMap.get(entity) > entity.getHealth()) {
						entityDamaged(entity, entityHealthMap.get(entity) - entity.getHealth());
					}
					entityHealthMap.put(entity, entity.getHealth());
				}
			}
			// A little cleanup for the entityHealthMap, removing missing or dead entities
			Iterator it = entityHealthMap.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry pair = (Entry) it.next();
				EntityLivingBase entity = (EntityLivingBase) pair.getKey();
				if(!entitiesInWorld.contains(entity) || entity.getHealth() <= 0.0F || entity.isDead) {
					it.remove();
				}
			}
		} catch(ConcurrentModificationException e) {
			System.out.println("Caught a possible concurrent modification exception, maybe the client lagged?");
		}*/
		// Check if player has splashed in water
		if(hasSplashed(player)) {
			Shader s = new ShaderBlurFade(VisualType.blur, 200 + rand.nextInt(100), 10.0F + rand.nextFloat() * 5.0F);
			Base.instance.manager.addVisualDirect(s);
		}
		// Check if player is in water, then wash certain splats away
		if(player.isInWater()) {
			for(Visual v : Base.instance.manager.getActiveVisuals()) {
				if(v.getType().equals(VisualType.splatter) || v.getType().equals(VisualType.impact) || v.getType().equals(VisualType.pierce) || v.getType().equals(VisualType.slash) || v.getType().equals(VisualType.dust)) {
					v.subtractTickPercent(2.5F);
				}
			}
		}
		
		// Check if player has less than three hearts, then play heartbeat sound and flash lowhealth screen
		if (player.getHealth() <= 6.0F) {
			if(this.lowHealthBuffer <= 0) {
				float f1 = (7.0F - player.getHealth()) * 0.2F;
				//Base.instance.manager.addVisualsWithShading(VisualType.lowhealth, 1, (int)(6.0F * (6.0F - player.getHealth())), (int)(10.0F * (6.0F - player.getHealth())), new Color(1.0F, 1.0F, 1.0F, f1 <= 1.0F ? f1 : 1.0F));
				this.lowHealthBuffer = (int) (player.getHealth() * 10 + 15);
				Base.instance.manager.addVisualsWithShading(VisualType.lowhealth, 1, this.lowHealthBuffer - 5, this.lowHealthBuffer - 5, new Color(1.0F, 1.0F, 1.0F, Math.min(0.7F, f1)));
				
				mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("sonicvisuals:heartbeatOut"), (float)player.posX, (float)player.posY, (float)player.posZ));
				//Minecraft.getMinecraft().theWorld.playSoundEffect(player.posX, player.posY, player.posZ, "sonicvisuals:heartbeatOut", 1, 1);
			} else if(this.lowHealthBuffer == 5) {
				mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("sonicvisuals:heartbeatIn"), (float)player.posX, (float)player.posY, (float)player.posZ));
				//Minecraft.getMinecraft().theWorld.playSoundEffect(player.posX, player.posY, player.posZ, "sonicvisuals:heartbeatIn", 1, 1);
				this.lowHealthBuffer -= 1;
			} else {
				this.lowHealthBuffer -= 1;
			}
		}
		// Check to see if potions are splashing
		checkRecentPotions();
		// Check surrounding light values and "adjust" eyes(TODO)
		// Check surrounding temperatures and show cold or heat overlays(TODO)
	}

	private void checkRecentPotions() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		AxisAlignedBB axisBox = AxisAlignedBB.fromBounds(Math.floor(player.posX) - 4.5D, Math.floor(player.posY) - 5.0D, Math.floor(player.posZ) - 4.5D, Math.floor(player.posX) + 4.5D, Math.floor(player.posY) + 2.0D, Math.floor(player.posZ) + 4.5D);
		for (EntityPotion entityPotion : (ArrayList<EntityPotion>)Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityPotion.class, axisBox)) {
			if (entityPotion.isDead) {
				double distance = Math.sqrt(Math.pow(Math.floor(player.posX) - entityPotion.posX, 2) + Math.pow(Math.floor(player.posY + player.eyeHeight) - entityPotion.posY, 2) + Math.pow(Math.floor(player.posZ) - entityPotion.posZ, 2));
				double modifier = 1.0D / distance;
				int bitColor = PotionHelper.getLiquidColor(entityPotion.getPotionDamage(), false);
				float r = (bitColor >> 16 & 0xFF) / 255.0F;
				float g = (bitColor >> 8 & 0xFF) / 255.0F;
				float b = (bitColor & 0xFF) / 255.0F;
				float f1 = (float)(modifier * 2.0D);
				Base.instance.manager.addVisualsWithShading(VisualType.potion, 1, 30, 60, new Color(r, g, b, f1 <= 1.0F ? f1 : 1.0F));
			}
		}
	}

	/*private VisualType getOverlayFromSource(DamageSource ds) {
		if (ds.equals(DamageSource.lava)) {
			return VisualType.lavaO;
		}
		if ((ds.equals(DamageSource.cactus)) || (ds.equals(DamageSource.drown)) || (ds.equals(DamageSource.fall)) || (ds.equals(DamageSource.generic)) || (ds.getDamageType().equals("mob")) || (ds.getDamageType().equals("player"))) {
			return VisualType.damaged;
		}
		return null;
	}*/

	private boolean isSharp(Item item)
	{
		return sharpList.contains(item);
	}

	private boolean isBlunt(Item item)
	{
		return bluntList.contains(item);
	}

	private boolean isPierce(Item item)
	{
		return pierceList.contains(item);
	}

	private boolean hasSplashed(EntityPlayer entityPlayer) {
		int x = (int)Math.floor(entityPlayer.posX);
		int y = (int)(entityPlayer.posY + entityPlayer.getDefaultEyeHeight());
		int z = (int)Math.floor(entityPlayer.posZ);

		int prevX = (int)Math.floor(entityPlayer.prevPosX);
		int prevY = (int)(entityPlayer.prevPosY + entityPlayer.getDefaultEyeHeight());
		int prevZ = (int)Math.floor(entityPlayer.prevPosZ);
		if (Minecraft.getMinecraft().theWorld != null) {
			Block currentBlockEyesIn = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
			Block pastBlockEyesIn = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(prevX, prevY, prevZ)).getBlock();
			return (currentBlockEyesIn.equals(Blocks.flowing_water) ^ pastBlockEyesIn.equals(Blocks.flowing_water)) || (currentBlockEyesIn.equals(Blocks.water) ^ pastBlockEyesIn.equals(Blocks.water));
		}
		return false;
	}
}