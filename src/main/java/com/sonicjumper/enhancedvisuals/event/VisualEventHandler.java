package com.sonicjumper.enhancedvisuals.event;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.ConfigCore;
import com.sonicjumper.enhancedvisuals.environment.BaseEnvironmentEffect;
import com.sonicjumper.enhancedvisuals.environment.EyeSensitivityHandler;
import com.sonicjumper.enhancedvisuals.environment.PotionSplashHandler;
import com.sonicjumper.enhancedvisuals.render.RenderShaderBlurFade;
import com.sonicjumper.enhancedvisuals.render.RenderShaderDefault;
import com.sonicjumper.enhancedvisuals.render.RenderShaderDesaturate;
import com.sonicjumper.enhancedvisuals.visuals.Blur;
import com.sonicjumper.enhancedvisuals.visuals.BoxBlur;
import com.sonicjumper.enhancedvisuals.visuals.Shader;
import com.sonicjumper.enhancedvisuals.visuals.ShaderBlurFade;
import com.sonicjumper.enhancedvisuals.visuals.ShaderDesaturate;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VisualEventHandler {
	
	private ArrayList<BaseEnvironmentEffect> environmentalEffects;
	//public WetnessHandler wetnessHandler = new WetnessHandler(this);
	public EyeSensitivityHandler eyeSensitivityHandler = new EyeSensitivityHandler(this);
	//public TemperatureHandler temperatureHandler = new TemperatureHandler(this);
	public PotionSplashHandler potionSplashHandler = new PotionSplashHandler(this);
	
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

		sharpList.add(Items.IRON_SWORD);
		sharpList.add(Items.WOODEN_SWORD);
		sharpList.add(Items.STONE_SWORD);
		sharpList.add(Items.DIAMOND_SWORD);
		sharpList.add(Items.GOLDEN_SWORD);
		sharpList.add(Items.IRON_AXE);
		sharpList.add(Items.WOODEN_AXE);
		sharpList.add(Items.STONE_AXE);
		sharpList.add(Items.DIAMOND_AXE);
		sharpList.add(Items.GOLDEN_AXE);

		bluntList.add(Items.IRON_PICKAXE);
		bluntList.add(Items.WOODEN_PICKAXE);
		bluntList.add(Items.STONE_PICKAXE);
		bluntList.add(Items.DIAMOND_PICKAXE);
		bluntList.add(Items.GOLDEN_PICKAXE);
		bluntList.add(Items.IRON_SHOVEL);
		bluntList.add(Items.WOODEN_SHOVEL);
		bluntList.add(Items.STONE_SHOVEL);
		bluntList.add(Items.DIAMOND_SHOVEL);
		bluntList.add(Items.GOLDEN_SHOVEL);

		pierceList.add(Items.IRON_HOE); 
		pierceList.add(Items.WOODEN_HOE);
		pierceList.add(Items.STONE_HOE);
		pierceList.add(Items.DIAMOND_HOE);
		pierceList.add(Items.GOLDEN_HOE);
		pierceList.add(Items.ARROW);

		//entityHealthMap = new HashMap<EntityLivingBase, Float>();

		rand = new Random();
		
		environmentalEffects = new ArrayList<BaseEnvironmentEffect>();
		//environmentalEffects.add(wetnessHandler);
		environmentalEffects.add(eyeSensitivityHandler);
		//environmentalEffects.add(temperatureHandler);
		environmentalEffects.add(potionSplashHandler);
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
		if(event.getEntity() instanceof EntityPlayer)
		{
			entityDamaged(event.getEntityLiving(), event.getSource(), event.getAmount());
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
		if(e.getEntityLiving().equals(mc.thePlayer)) {
			Base.instance.manager.clearAllVisuals();
			//this.playerWetness = 0.5F;
			//this.playerTemp = 1.0F;
			//this.eyeAdjustment = 0.4F;
		}
	}

	/*@SubscribeEvent
	public void onEntityTakeDamage(LivingHurtEvent event) {
		
		//System.out.println("Entity took damage: " + event.toString());
		
	}*/

	/*@SubscribeEvent
	public void onExplosion(ExplosionEvent event) {
		//System.out.println("Explosion occurred: " + event.toString());
	}*/

	private void entityDamaged(EntityLivingBase entity, DamageSource source, float damage) {
		//Base.log.info("Damage amount:" + damage + " called for entity " + entity.toString());
		/*for(Point2D point : SplatUtil.generateRandomSplatStreak(25)) {
			Splat s = new Splat(VisualType.splatter, 200, Color.WHITE, (float) point.getX(), (float) point.getY());
			Base.instance.manager.addVisualDirect(s);
		}*/
		if(source == DamageSource.outOfWorld || damage == 0)
			return ;
		// Check distance to player and use that as splat distance pattern
		double distanceSq = Minecraft.getMinecraft().thePlayer.getDistanceSqToEntity(entity);
		if(distanceSq > 64.0D) {
			return;
		}
		if(entity instanceof EntityPlayer)
		{
			Entity attacker = source.getSourceOfDamage();
			if(attacker instanceof EntityArrow) {
				Base.instance.manager.createVisualFromDamage(VisualType.pierce, damage, entity);
			}
			if(attacker instanceof EntityLivingBase) {
				EntityLivingBase lastAttacker = (EntityLivingBase) attacker;
				// Check weapons
				if(lastAttacker.getHeldItemMainhand() != null) {
					if(isSharp(lastAttacker.getHeldItemMainhand().getItem())) {
						Base.instance.manager.createVisualFromDamageAndDistance(VisualType.slash, damage, entity, distanceSq);
					} else if(isBlunt(lastAttacker.getHeldItemMainhand().getItem())) {
						Base.instance.manager.createVisualFromDamageAndDistance(VisualType.impact, damage, entity, distanceSq);
					} else if(isPierce(lastAttacker.getHeldItemMainhand().getItem())) {
						Base.instance.manager.createVisualFromDamageAndDistance(VisualType.pierce, damage, entity, distanceSq);
					} else {
						// Default to splatter type
						Base.instance.manager.createVisualFromDamageAndDistance(VisualType.splatter, damage, entity, distanceSq);
					}
				} else {
					
					// If player received fall damage
					
					
					if(lastAttacker instanceof EntityZombie || lastAttacker instanceof EntitySkeleton || lastAttacker instanceof EntityOcelot) {
						Base.instance.manager.createVisualFromDamageAndDistance(VisualType.slash, damage, entity, distanceSq);
					} else if(lastAttacker instanceof EntityGolem || lastAttacker instanceof EntityPlayer) {
						Base.instance.manager.createVisualFromDamageAndDistance(VisualType.impact, damage, entity, distanceSq);
					} else if(lastAttacker instanceof EntityWolf || lastAttacker instanceof EntitySpider) {
						Base.instance.manager.createVisualFromDamageAndDistance(VisualType.pierce, damage, entity, distanceSq);
					}

					
				}
			}
			
			if(source == DamageSource.cactus) {
				Base.instance.manager.createVisualFromDamage(VisualType.pierce, damage, entity);
			}
			
			if(source == DamageSource.fall || source == DamageSource.fallingBlock) {
				Base.instance.manager.createVisualFromDamage(VisualType.impact, damage, entity);
			}
			
			if(source.isExplosion()) {
				if(source.getSourceOfDamage() != null && source.getSourceOfDamage().getDistanceToEntity(mc.thePlayer) < 16.0D) {
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.dust, damage, entity, source.getSourceOfDamage().getDistanceSqToEntity(mc.thePlayer));
					Blur b = new BoxBlur(VisualType.blur, (int) (damage * 10), new Color(1.0F, 1.0F, 1.0F, 0.8F), true, ConfigCore.blurQuality, 10, 1);
					Base.instance.manager.addVisualDirect(b);
				} else {
					Base.instance.manager.createVisualFromDamage(VisualType.dust, damage, entity);
					Blur b = new BoxBlur(VisualType.blur, (int) (damage * 10), new Color(1.0F, 1.0F, 1.0F, 0.8F), true, ConfigCore.blurQuality, 10, 1);
					Base.instance.manager.addVisualDirect(b);
				}
			}
			
			
			
			if(source.equals(DamageSource.drown)) {
				Base.instance.manager.addRandomNumVisualsWithColor(VisualType.waterS, 4, 8, (int) (damage * 10), (int) (damage * 15), new Color(1.0F, 1.0F, 1.0F, 1.0F));
			}
			
			
			
			if(source.isFireDamage() || source == DamageSource.onFire)
			{
				Base.instance.manager.addVisualsWithShading(VisualType.fire, (int) damage, 100, 1000, new Color(1, 1, 1));
		      //if (event.source.n() == "lava") {
		        //burnOverlay = new Overlay(Overlay.OverlayType.Burn, 0.5F, 25 + rand.nextInt(25));
		      //}
			}
			
		}else{
			if(mc.thePlayer.isBurning())
				Base.instance.manager.addVisualsWithShading(VisualType.fire, (int) damage, 100, 1000, new Color(1, 1, 1));
			
			// For now, just assume damage was another source(falling, drowning, cactus, etc.) and use splatter
			if(source == DamageSource.anvil || source == DamageSource.fall || source == DamageSource.fallingBlock
					|| source.getDamageType().equals("mob") || source.getDamageType().equals("player")) 
				if(source.getEntity().getDistanceToEntity(mc.thePlayer) < 8.0D)
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.splatter, damage, entity, distanceSq);
		}
	}

	public void onTickInGame() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		if(player == null)
			return ;
		
		// Tick all visuals
		boolean hasBlurShader = false;
		boolean hasSaturationShader = false;
		ArrayList<Visual> vList = Base.instance.manager.getActiveVisuals();
		for (int i = 0; i < vList.size(); i++) {
			Visual v = vList.get(i);
			if(v instanceof ShaderBlurFade)
				hasBlurShader = true;
			if(v instanceof ShaderDesaturate)
				hasSaturationShader = true;
			v.tickUpdate();
		}
		//RenderShaderBlurFade.resetBlur();
		if(!hasBlurShader && RenderShaderBlurFade.lastBlurRadius != 0)
			RenderShaderBlurFade.resetBlur();
		
		if(!hasSaturationShader && RenderShaderDesaturate.lastSaturation != 1)
			RenderShaderDesaturate.resetSaturation();
		
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
		for(BaseEnvironmentEffect ee : environmentalEffects) {
			// Keep track of Player's "wetness"
			// Adjust the player's vision depending on sudden increases in light
			// Keep track of Player's "temperature"
			ee.onTick();
		}
		
		// Check if player has splashed in water
		if(hasSplashed(player)) {
			Shader s = new ShaderBlurFade(VisualType.blur, 30 + rand.nextInt(30), 10.0F + rand.nextFloat() * 5.0F);
			Base.instance.manager.addVisualDirect(s);
		}
		// Check if player is in water, then wash certain splats away
		if(player.isInWater()) {
			for(Visual v : Base.instance.manager.getActiveVisuals()) {
				if(v.getType().substractByTime) {
					v.subtractTickPercent(2.5F);
				}
			}
		}
		
		//Saturation
		float aimedSaturation = 1;
		if(player.getFoodStats().getFoodLevel() <= 8)
			aimedSaturation = (player.getFoodStats().getFoodLevel()/8F)*0.875F;
		
		float fadeAmount = 0.01F;
		if(ShaderDesaturate.Saturation < aimedSaturation)
			ShaderDesaturate.Saturation = Math.min(ShaderDesaturate.Saturation+fadeAmount, aimedSaturation);
		else if(ShaderDesaturate.Saturation > aimedSaturation)
			ShaderDesaturate.Saturation = Math.max(ShaderDesaturate.Saturation-fadeAmount, aimedSaturation);
			
		
		// Check if player has less than three hearts, then play heartbeat sound and flash lowhealth screen
		if (player.getHealth() <= 6.0F) {
			if(this.lowHealthBuffer <= 0) {
				float f1 = (7.0F - player.getHealth()) * 0.2F;
				//Base.instance.manager.addVisualsWithShading(VisualType.lowhealth, 1, (int)(6.0F * (6.0F - player.getHealth())), (int)(10.0F * (6.0F - player.getHealth())), new Color(1.0F, 1.0F, 1.0F, f1 <= 1.0F ? f1 : 1.0F));
				this.lowHealthBuffer = (int) (player.getHealth() * 10 + 15);
				Base.instance.manager.addVisualsWithShading(VisualType.lowhealth, 1, this.lowHealthBuffer - 5, this.lowHealthBuffer - 5, new Color(1.0F, 1.0F, 1.0F, Math.min(0.7F, f1)));
				
				Shader s = new ShaderBlurFade(VisualType.blur, 10, Math.min(0.7F, f1)*50F);
				Base.instance.manager.addVisualDirect(s);
				
				mc.getSoundHandler().playSound(new PositionedSoundRecord(new SoundEvent(new ResourceLocation("sonicvisuals:heartbeatOut")), SoundCategory.MASTER, 1, 1, new BlockPos(player)));
				//Minecraft.getMinecraft().theWorld.playSoundEffect(player.posX, player.posY, player.posZ, "sonicvisuals:heartbeatOut", 1, 1);
			} else if(this.lowHealthBuffer == 5) {
				mc.getSoundHandler().playSound(new PositionedSoundRecord(new SoundEvent(new ResourceLocation("sonicvisuals:heartbeatIn")), SoundCategory.MASTER, 1, 1, new BlockPos(player)));

				Shader s = new ShaderBlurFade(VisualType.blur, 10, 50F);
				Base.instance.manager.addVisualDirect(s);
				//Minecraft.getMinecraft().theWorld.playSoundEffect(player.posX, player.posY, player.posZ, "sonicvisuals:heartbeatIn", 1, 1);
				this.lowHealthBuffer -= 1;
			} else {
				this.lowHealthBuffer -= 1;
			}
		}
		// Check to see if potions are splashing
		checkRecentPotions();
		
		//Slender
		checkSlender();
		
		//Sand
		addSandSplatFromTick();
	}
	
	public void addSandSplatFromTick()
	{
		float modifier = 0.5F;
		boolean sand = false;
		boolean onSand = isOnSand(mc.thePlayer);
		if (mc.thePlayer.isSprinting())
		{
			modifier *= 1.5F;
			sand = true;
	    }
	    if (mc.thePlayer.onGround)
	    {
	    	modifier *= 1.5F;
	    	sand = true;
	    }
	    //if (mc.thePlayer.)
	    //{
	    //	modifier *= 2.0F;
	    //	sand = true;
	    //}
	    if ((sand) && (onSand))
	    {
	    	int small = (int)(rand.nextInt(3) * modifier);
	    	int medium = 0;int large = 0;
	    	if (rand.nextInt(16) == 1) {
	    		medium = (int)(1.0F * modifier);
	    	}
	    	if (rand.nextInt(32) == 1) {
	    		large = (int)(1.0F * modifier);
	    	}
	    	Base.instance.manager.addVisuals(VisualType.sand, (int) modifier, 100, 100);
	    	//addSplat(small, medium, large, Splat.SplatType.Sand)
	    }
	}
	
	private boolean isOnSand(EntityPlayer entityPlayer)
	{
		int posX = (int)entityPlayer.posX;
		int posY = (int)(entityPlayer.posY - 2.0D);
		int posZ = (int)entityPlayer.posZ;
	    if (mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ)).getBlock() == Blocks.SAND && mc.theWorld.getBlockState(new BlockPos(posX, posY+1, posZ)).getBlock() == Blocks.SAND) {
	    	return true;
	    }
	    return false;
	}
	
	private void checkSlender()
	{
		boolean angryNearby = false;
	    double modifier = 0.0D;
	    double d0 = mc.thePlayer.posX;
	    double d1 = mc.thePlayer.posY;
	    double d2 = mc.thePlayer.posZ;
	    
	    AxisAlignedBB box = mc.thePlayer.getEntityBoundingBox();
	    box = box.expand(16, 16, 16);
	    
	    EntityEnderman mob = (EntityEnderman) mc.theWorld.findNearestEntityWithinAABB(EntityEnderman.class, box, mc.thePlayer);
	    if(mob != null)
	    {
	    	angryNearby = true;
	    	double distModifier = 1.0D / Math.pow(Math.sqrt(Math.pow(d0 - mob.posX, 2) + Math.pow(d1 - mob.posY, 2) + Math.pow(d2 - mob.posZ, 2)) / 3.0D, 2);
	    	if (distModifier > modifier)
	    	{
	    		modifier = distModifier;
	    		if (modifier > 3.5D) {
	    			modifier = 3.5D;
	    		}
	    	}
	    }
	    
	    if (angryNearby) {
	    	//Base.instance.manager.slenderOverlay.setActive(true);
			if (0.25D * modifier < 0.6000000238418579D) {
				Base.instance.manager.slenderOverlay.intensity = (float)(0.25D * modifier);
			} else {
				Base.instance.manager.slenderOverlay.intensity = 0.6F;
			}
	    }else
	    	Base.instance.manager.slenderOverlay.intensity = 0;
	}
	
	private void checkRecentPotions() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		AxisAlignedBB axisBox = new AxisAlignedBB(Math.floor(player.posX) - 4.5D, Math.floor(player.posY) - 5.0D, Math.floor(player.posZ) - 4.5D, Math.floor(player.posX) + 4.5D, Math.floor(player.posY) + 2.0D, Math.floor(player.posZ) + 4.5D);
		for (EntityPotion entityPotion : (ArrayList<EntityPotion>)Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityPotion.class, axisBox)) {
			if (entityPotion.isDead) {
				double distance = Math.sqrt(Math.pow(Math.floor(player.posX) - entityPotion.posX, 2) + Math.pow(Math.floor(player.posY + player.eyeHeight) - entityPotion.posY, 2) + Math.pow(Math.floor(player.posZ) - entityPotion.posZ, 2));
				double modifier = 1.0D / distance;
				int bitColor = PotionUtils.getPotionColor(PotionUtils.getPotionFromItem(entityPotion.getPotion()));
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
			return (currentBlockEyesIn.equals(Blocks.FLOWING_WATER) ^ pastBlockEyesIn.equals(Blocks.FLOWING_WATER)) || (currentBlockEyesIn.equals(Blocks.WATER) ^ pastBlockEyesIn.equals(Blocks.WATER));
		}
		return false;
	}
}