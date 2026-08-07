package team.creative.enhancedvisuals.common.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.api.CreativeConfig.DecimalRange;
import team.creative.creativecore.common.config.premade.DecimalMinMax;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.creativecore.common.util.type.Color;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.event.FireParticlesEvent;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeOverlay;
import team.creative.enhancedvisuals.api.type.VisualTypeParticle;
import team.creative.enhancedvisuals.api.type.VisualTypeParticleColored;
import team.creative.enhancedvisuals.client.VisualManager;

public class DamageHandler extends VisualHandler {
    
    public static final ArrayList<Item> sharpList = new ArrayList<>();
    public static final ArrayList<Item> bluntList = new ArrayList<>();
    public static final ArrayList<Item> pierceList = new ArrayList<>();
    
    static {
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
    }
    
    @CreativeConfig
    public Color bloodColor = new Color(0.3F, 0.01F, 0.01F, 0.7F);
    
    @CreativeConfig
    public VisualType damaged = new VisualTypeOverlay("damaged");
    @CreativeConfig
    @DecimalRange(min = 0, max = 1)
    public float hitEffectIntensity = 0F;
    @CreativeConfig
    public IntMinMax hitDuration = new IntMinMax(1, 10);
    
    @CreativeConfig
    public VisualType splatter = new VisualTypeParticle("splatter");
    @CreativeConfig
    public VisualType impact = new VisualTypeParticle("impact");
    @CreativeConfig
    public VisualType slash = new VisualTypeParticle("slash");
    @CreativeConfig
    public VisualType pierce = new VisualTypeParticle("pierce");
    @CreativeConfig
    public IntMinMax bloodDuration = new IntMinMax(500, 1500);
    
    @CreativeConfig
    public VisualType fire = new VisualTypeParticle("fire");
    @CreativeConfig
    public int fireSplashes = 1;
    @CreativeConfig
    public IntMinMax fireDuration = new IntMinMax(100, 1000);
    
    @CreativeConfig
    public int drownSplashes = 4;
    @CreativeConfig
    public IntMinMax drownDuration = new IntMinMax(10, 15);
    @CreativeConfig
    public VisualType waterDrown = new VisualTypeParticleColored("blob", new Color(0, 0, 255)).setIgnoreWater();
    
    @CreativeConfig
    public int lightningSplashes = 10;
    @CreativeConfig
    public IntMinMax lightningDuration = new IntMinMax(10, 15);
    @CreativeConfig
    public VisualType lightning = new VisualTypeParticleColored("shock", new Color(120, 120, 255)).setIgnoreWater();
    
    @CreativeConfig
    public int freezeSplashes = 4;
    @CreativeConfig
    public IntMinMax freezeDuration = new IntMinMax(10, 15);
    @CreativeConfig
    public VisualType freeze = new VisualTypeParticleColored("ice", new Color(200, 255, 255));
    
    @CreativeConfig
    public int flyIntoWallSplashes = 4;
    @CreativeConfig
    public IntMinMax flyIntoWallDuration = new IntMinMax(10, 15);
    @CreativeConfig
    public VisualType flyIntoWall = new VisualTypeParticleColored("break", new Color(255, 255, 255));
    
    @CreativeConfig
    public int heatSplashes = 4;
    @CreativeConfig
    public IntMinMax heatDuration = new IntMinMax(10, 15);
    @CreativeConfig
    public VisualType heat = new VisualTypeParticleColored("heat", new Color(255, 255, 255)) {
        
        @Override
        public boolean canRotate() {
            return false;
        }
        
    };
    
    @CreativeConfig
    public int effectSplashes = 4;
    @CreativeConfig
    public IntMinMax effectDuration = new IntMinMax(10, 15);
    @CreativeConfig
    public VisualType parasites = new VisualTypeParticleColored("parasite", new Color(0, 126, 0)).setIgnoreWater();
    @CreativeConfig
    public VisualType wither = new VisualTypeParticleColored("wither", 0, new DecimalMinMax(1.5, 2.5), new Color(0, 0, 0)).setIgnoreWater();
    
    @CreativeConfig
    public IntMinMax tunnelDuration = new IntMinMax(10, 15);
    @CreativeConfig
    public VisualType tunnel = new VisualTypeOverlay("tunnel", 0).setIgnoreWater();
    
    @CreativeConfig
    public DecimalCurve healthScaler = new DecimalCurve(0, 3, 12, 1.5);
    
    @CreativeConfig
    public float damageScale = 1;
    
    @CreativeConfig
    public List<String> damageBlackList = new ArrayList<>();
    
    public void playerDamaged(Player player, DamageSource source, float damage) {
        if (hitEffectIntensity > 0)
            VisualManager.addVisualFadeOut(damaged, this, new DecimalCurve(VisualManager.RANDOM, hitDuration, hitEffectIntensity * 0.2));
        
        if (source.getDirectEntity() instanceof Arrow)
            createVisualFromDamageAndDistance(pierce, damage, player, bloodDuration);
        else if (source.getDirectEntity() instanceof LivingEntity l) {
            if (!l.getMainHandItem().isEmpty()) {
                if (isSharp(l.getMainHandItem()))
                    createVisualFromDamageAndDistance(slash, damage, player, bloodDuration);
                else if (isBlunt(l.getMainHandItem()))
                    createVisualFromDamageAndDistance(impact, damage, player, bloodDuration);
                else if (isPierce(l.getMainHandItem()))
                    createVisualFromDamageAndDistance(pierce, damage, player, bloodDuration);
                else
                    createVisualFromDamageAndDistance(splatter, damage, player, bloodDuration);
            } else if (l instanceof Zombie || l instanceof Skeleton || l instanceof Ocelot)
                createVisualFromDamageAndDistance(slash, damage, player, bloodDuration);
            else if (l instanceof AbstractGolem || l instanceof Player)
                createVisualFromDamageAndDistance(impact, damage, player, bloodDuration);
            else if (l instanceof Wolf || l instanceof Spider)
                createVisualFromDamageAndDistance(pierce, damage, player, bloodDuration);
            else
                createVisualFromDamageAndDistance(splatter, Math.min(20, damage), player, bloodDuration);
        } else if (source.is(DamageTypes.CACTUS))
            createVisualFromDamageAndDistance(pierce, damage, player, bloodDuration);
        else if (source.is(DamageTypeTags.IS_FALL))
            createVisualFromDamageAndDistance(impact, damage, player, bloodDuration);
        else if (source.is(DamageTypeTags.IS_DROWNING))
            VisualManager.addParticlesFadeOut(waterDrown, this, drownSplashes, drownDuration, true);
        else if (source.is(DamageTypeTags.IS_FREEZING))
            VisualManager.addParticlesFadeOut(freeze, this, freezeSplashes, freezeDuration, true);
        else if (source.is(DamageTypes.WITHER) || source.is(DamageTypes.WITHER_SKULL))
            VisualManager.addParticlesFadeOut(wither, this, effectSplashes, effectDuration, true);
        else if (source.is(DamageTypeTags.IS_LIGHTNING))
            VisualManager.addParticlesFadeOut(lightning, this, lightningSplashes, lightningDuration, true);
        else if (source.is(DamageTypes.FLY_INTO_WALL))
            VisualManager.addParticlesFadeOut(flyIntoWall, this, flyIntoWallSplashes, flyIntoWallDuration, true);
        
        else if (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypes.ON_FIRE)) {
            FireParticlesEvent event = new FireParticlesEvent(fireSplashes, fireDuration.min, fireDuration.max);
            CreativeCore.loader().postForge(event);
            VisualManager.addParticlesFadeOut(fire, this, event.getNewFireSplashes(), new IntMinMax(event.getNewFireDurationMin(), event.getNewFireDurationMax()), true,
                new Color(0, 0, 0));
        } else {
            String registeredName = source.typeHolder().unwrapKey().map(x -> x.location().getPath()).orElse("[unregistered]");
            if (registeredName.contains("hyperthermia"))
                VisualManager.addParticlesFadeOut(heat, this, heatSplashes, heatDuration, true);
            else if (registeredName.contains("parasites"))
                VisualManager.addParticlesFadeOut(parasites, this, effectSplashes, effectDuration, true);
            else if (registeredName.contains("dehydration") || source.is(DamageTypes.STARVE))
                VisualManager.addVisualFadeOut(tunnel, this, tunnelDuration);
            else if (!damageBlackList.contains(registeredName))
                createVisualFromDamageAndDistance(splatter, Math.min(20, damage), player, bloodDuration);
        }
    }
    
    public void createVisualFromDamageAndDistance(VisualType type, float damage, Player player, IntMinMax duration) {
        if (damage <= 0.0F)
            return;
        
        float health = player.getHealth() - damage;
        double rate = Math.max(0, healthScaler.valueAt(health));
        
        VisualManager.addParticlesFadeOut(type, this, Math.min(5000, (int) (damageScale * damage * rate)), new DecimalCurve(0, 1, duration.next(VisualManager.RANDOM), 0), true,
            bloodColor);
    }
    
    private static boolean isSharp(ItemStack item) {
        return sharpList.contains(item.getItem());
    }
    
    private static boolean isBlunt(ItemStack item) {
        return bluntList.contains(item.getItem());
    }
    
    private static boolean isPierce(ItemStack item) {
        return pierceList.contains(item.getItem());
    }
    
}
