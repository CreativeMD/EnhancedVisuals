package com.sonicjumper.enhancedvisuals.event;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.ConfigCore;
import com.sonicjumper.enhancedvisuals.environment.BaseEnvironmentEffect;
import com.sonicjumper.enhancedvisuals.environment.PotionSplashHandler;
import com.sonicjumper.enhancedvisuals.render.RenderShaderBlurFade;
import com.sonicjumper.enhancedvisuals.render.RenderShaderDesaturate;
import com.sonicjumper.enhancedvisuals.visuals.Shader;
import com.sonicjumper.enhancedvisuals.visuals.ShaderBlurFade;
import com.sonicjumper.enhancedvisuals.visuals.ShaderDesaturate;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.client.gui.GuiGameOver;
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
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.client.event.sound.SoundEvent.SoundSourceEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import paulscode.sound.Source;

public class VisualEventHandler {
	
	private ArrayList<BaseEnvironmentEffect> environmentalEffects;
	public PotionSplashHandler potionSplashHandler = new PotionSplashHandler(this);
	
	public Minecraft mc = Minecraft.getMinecraft();
	
	// Swords and Axes are slashing
	// Shovels and Pickaxes are impact
	// Hoes and Arrows are piercing
	private ArrayList<Item> sharpList;
	private ArrayList<Item> bluntList;
	private ArrayList<Item> pierceList;

	private Random rand;
	
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
		
		rand = new Random();
		
		environmentalEffects = new ArrayList<BaseEnvironmentEffect>();
		environmentalEffects.add(potionSplashHandler);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerDamage(LivingAttackEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer)
		{
			entityDamaged(event.getEntityLiving(), event.getSource(), event.getAmount());
		}
	}

	@SubscribeEvent
	public void playerTickEvent(PlayerTickEvent event) {
		if(event.phase.equals(Phase.END) && !(Minecraft.getMinecraft().currentScreen instanceof GuiGameOver)) {
			onTickInGame(Minecraft.getMinecraft().thePlayer != null);
		}
	}

	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent e) {
		if(e.getEntityLiving().equals(mc.thePlayer)) {
			Base.instance.manager.clearAllVisuals();
		}
	}

	private void entityDamaged(EntityLivingBase entity, DamageSource source, float damage) {
		
		if(source == DamageSource.outOfWorld || damage == 0)
			return ;
		
		// Check distance to player and use that as splat distance pattern
		double distanceSq = Minecraft.getMinecraft().thePlayer.getDistanceSqToEntity(entity);
		if(distanceSq > 64.0D) {
			return;
		}
		if(entity instanceof EntityPlayer)
		{
			if(ConfigCore.hitEffect)
				Base.instance.manager.addVisualsWithShading(VisualType.lowhealth, 1, 15, 20, new Color(1.0F, 1.0F, 1.0F, 0.2F));
			
			Entity attacker = source.getSourceOfDamage();
			if(attacker instanceof EntityArrow) 
				Base.instance.manager.createVisualFromDamage(VisualType.pierce, damage, entity);
			
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
				Base.instance.manager.addVisuals(VisualType.dust, (int) Math.min(40, damage*VisualType.dust.multiplier), VisualType.dust.minDuration, VisualType.dust.maxDuration);
				
				float volume = Math.max(ConfigCore.minExplosionVolume, 1-(damage/ConfigCore.explosionVolumeModifier));
				int time = (int) Math.min(ConfigCore.maxExplosionTime, damage*ConfigCore.explosionTimeModifier);
				
				if(!SoundMuteHandler.isMuting)
					playSound(new ResourceLocation("enhancedvisuals:ringing"), null, (1-volume)*ConfigCore.maxBeepVolume);
				SoundMuteHandler.startMuting(time, volume);
				Base.instance.manager.addVisualDirect(new ShaderBlurFade(VisualType.blur, (int) (time/ConfigCore.blurTimeFactor), ConfigCore.maxBlur));
				
			}
			
			
			
			if(source.equals(DamageSource.drown)) {
				Base.instance.manager.addRandomNumVisualsWithColor(VisualType.waterS, VisualType.waterS.minSplashes, VisualType.waterS.maxSplashes, VisualType.waterS.minDuration, VisualType.waterS.maxDuration, new Color(1.0F, 1.0F, 1.0F, 1.0F));
			}
			
			
			
			if(source.isFireDamage() || source == DamageSource.onFire)
				Base.instance.manager.addVisualsWithShading(VisualType.fire, VisualType.fire.splashes, VisualType.fire.minDuration, VisualType.fire.maxDuration, new Color(1, 1, 1));
			
		}else{			
			// For now, just assume damage was another source(falling, drowning, cactus, etc.) and use splatter
			if(source == DamageSource.anvil || source == DamageSource.fall || source == DamageSource.fallingBlock
					|| source.getDamageType().equals("mob") || source.getDamageType().equals("player")) 
				if(source.getEntity().getDistanceToEntity(mc.thePlayer) < 8.0D)
					Base.instance.manager.createVisualFromDamageAndDistance(VisualType.splatter, damage, entity, distanceSq);
		}
	}

	public synchronized void onTickInGame(boolean isInGame) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		// Tick all visuals
		boolean hasBlurShader = false;
		ArrayList<Visual> vList = Base.instance.manager.getActiveVisuals();
		for (int i = 0; i < vList.size(); i++) {
			Visual v = vList.get(i);
			if(v instanceof ShaderBlurFade)
				hasBlurShader = true;
			if(v != null)
				v.tickUpdate();
		}
		//RenderShaderBlurFade.resetBlur();
		if(!hasBlurShader && RenderShaderBlurFade.lastBlurRadius != 0)
			RenderShaderBlurFade.resetBlur();
		
		//Saturation
		float aimedSaturation = VisualType.desaturate.defaultSaturation;
		
		if(isInGame)
		{
			if(player.getFoodStats().getFoodLevel() <= VisualType.desaturate.maxFoodLevelEffected)
			{
				float leftFoodInSpan = player.getFoodStats().getFoodLevel()-VisualType.desaturate.minFoodLevelEffected;
				float spanLength = VisualType.desaturate.maxFoodLevelEffected-VisualType.desaturate.minFoodLevelEffected;
				aimedSaturation = Math.max(VisualType.desaturate.minSaturation, (leftFoodInSpan/spanLength)*VisualType.desaturate.defaultSaturation);
			}
		}
		
		if(ShaderDesaturate.Saturation < aimedSaturation)
			ShaderDesaturate.Saturation = Math.min(ShaderDesaturate.Saturation+VisualType.desaturate.fadeFactor, aimedSaturation);
		else if(ShaderDesaturate.Saturation > aimedSaturation)
			ShaderDesaturate.Saturation = Math.max(ShaderDesaturate.Saturation-VisualType.desaturate.fadeFactor, aimedSaturation);
		
		if(!isInGame)
			return ;
		
		for(BaseEnvironmentEffect ee : environmentalEffects) {
			ee.onTick();
		}
		
		// Check if player has splashed in water
		if(hasSplashed(player)) {
			Shader s = new ShaderBlurFade(VisualType.blur, (int) (VisualType.blur.splashMinDuration + rand.nextFloat()*VisualType.blur.splashAdditionalDuration), VisualType.blur.splashMinIntensity + rand.nextFloat() * VisualType.blur.splashAdditionalIntensity);
			Base.instance.manager.addVisualDirect(s);
		}
		
		// Check if player is in water, then wash certain splats away
		if(player.isInWater()) {
			for(Visual v : Base.instance.manager.getActiveVisuals()) {
				if(v.getType().substractByTime) {
					v.subtractTickPercent(ConfigCore.waterSubstractFactor);
				}
			}
		}
		
		// Check if player has less than three hearts, then play heartbeat sound and flash lowhealth screen
		if (player.getHealth() <= ConfigCore.maxHearts) {
			if(this.lowHealthBuffer <= 0) {
				float f1 = (7.0F - player.getHealth()) * 0.2F;
				this.lowHealthBuffer = (int) (player.getHealth() * ConfigCore.heartBeatHeartFactor + ConfigCore.minHeartBeatLength);
				Base.instance.manager.addVisualsWithShading(VisualType.lowhealth, 1, this.lowHealthBuffer - 5, this.lowHealthBuffer - 5, new Color(1.0F, 1.0F, 1.0F, Math.min(0.7F, f1)));
				
				Shader s = new ShaderBlurFade(VisualType.blur, (int) VisualType.blur.heartBeatDuration, Math.min(0.7F, f1)*VisualType.blur.heartBeatIntensity);
				Base.instance.manager.addVisualDirect(s);
				
				playSound(new ResourceLocation(Base.MODID + ":heartbeatOut"), new BlockPos(player));
			} else if(this.lowHealthBuffer == 5) {
				playSound(new ResourceLocation(Base.MODID + ":heartbeatIn"), new BlockPos(player));
				Shader s = new ShaderBlurFade(VisualType.blur, (int) VisualType.blur.heartBeatDuration, VisualType.blur.heartBeatIntensity);
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
		
		SoundMuteHandler.tick();
	}
	
	public synchronized void playSound(ResourceLocation location, BlockPos pos)
	{
		playSound(location, pos, 1.0F);
	}
	
	public synchronized void playSound(ResourceLocation location, BlockPos pos, float volume)
	{
		if(pos != null)
			mc.getSoundHandler().playDelayedSound(new PositionedSoundRecord(new SoundEvent(location), SoundCategory.MASTER, volume, 1, pos), 0);
		else
			mc.getSoundHandler().playDelayedSound(new PositionedSoundRecord(location, SoundCategory.MASTER, volume, 1, false, 0, AttenuationType.NONE, 0, 0, 0), 0);
	}
	
	public void addSandSplatFromTick()
	{		
	    if(mc.thePlayer.onGround && isOnSand(mc.thePlayer))
	    {
	    	float modifier = VisualType.sand.defaultmodifier;
			if (mc.thePlayer.isSprinting())
				modifier = VisualType.sand.sprintingmodifier;
	    	Base.instance.manager.addVisuals(VisualType.sand, (int) (Math.random()*modifier), VisualType.sand.minDuration, VisualType.sand.maxDuration);
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
	    float modifier = 0.0F;
	    double d0 = mc.thePlayer.posX;
	    double d1 = mc.thePlayer.posY;
	    double d2 = mc.thePlayer.posZ;
	    
	    AxisAlignedBB box = mc.thePlayer.getEntityBoundingBox();
	    box = box.expand(16, 16, 16);
	    
	    EntityEnderman mob = (EntityEnderman) mc.theWorld.findNearestEntityWithinAABB(EntityEnderman.class, box, mc.thePlayer);
	    if(mob != null)
	    {
	    	angryNearby = true;
	    	float distModifier = (float) (1.0F / Math.pow(Math.sqrt(Math.pow(d0 - mob.posX, 2) + Math.pow(d1 - mob.posY, 2) + Math.pow(d2 - mob.posZ, 2)) / 3.0D, 2));
	    	if (distModifier > modifier)
	    	{
	    		modifier = distModifier;
	    		if (modifier > 3.5F) {
	    			modifier = 3.5F;
	    		}
	    	}
	    }
	    
	    if (angryNearby)
	    	Base.instance.manager.slenderOverlay.intensity = Math.max(VisualType.slender.defaultIntensity, Math.min(VisualType.slender.maxIntensity, VisualType.slender.slenderDistanceFactor * modifier));
	    else
	    	Base.instance.manager.slenderOverlay.intensity = VisualType.slender.defaultIntensity;
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
	
	@SubscribeEvent
	public void onSoundPlayed(SoundSourceEvent event)
	{
		if(SoundMuteHandler.isMuting && SoundMuteHandler.ignoredSounds != null)
		{
			if(event.getSound().getSoundLocation().toString().equals("enhancedvisuals:ringing"))
				SoundMuteHandler.ignoredSounds.add(event.getUuid());
			//else{
				//SoundMuteHandler.soundsToBeAdded.add(event.getUuid());
			//}
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