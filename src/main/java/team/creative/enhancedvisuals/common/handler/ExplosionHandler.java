package team.creative.enhancedvisuals.common.handler;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.windcharge.BreezeWindCharge;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.phys.Vec3;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.creativecore.common.config.premade.curve.IntCurve;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeBlur;
import team.creative.enhancedvisuals.api.type.VisualTypeParticle;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.client.sound.SoundMuteHandler;

public class ExplosionHandler extends VisualHandler {
    
    @CreativeConfig
    public VisualType dust = new VisualTypeParticle("dust");
    @CreativeConfig
    public IntMinMax dustDuration = new IntMinMax(500, 1000);
    @CreativeConfig
    public IntCurve dustAmount = new IntCurve(0, 0, 20, 30);
    
    @CreativeConfig
    public float maxExplosionVolume = 0.5F;
    @CreativeConfig
    public IntCurve explosionSoundTime = new IntCurve(0, 50, 20, 200);
    
    @CreativeConfig
    public VisualType blur = new VisualTypeBlur("blur");
    @CreativeConfig
    public DecimalCurve maxBlur = new DecimalCurve(0, 50, 10, 100);
    @CreativeConfig
    public IntCurve explosionBlurTime = new IntCurve(0, 10, 20, 20);
    
    @CreativeConfig
    public boolean ignoreBreeze = true;
    
    @CreativeConfig
    public boolean ignoreMace = true;
    
    @CreativeConfig
    public boolean ignoreSelfWindCharge = true;
    
    private Holder<Enchantment> windBurst;
    
    private boolean isMace(RegistryAccess access, ItemStack stack) {
        if (windBurst == null)
            windBurst = access.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.WIND_BURST);
        return stack.getItem() == Items.MACE && stack.getEnchantments().getLevel(windBurst) > 0;
    }
    
    public void onExploded(Player player, Vec3 pos, float size, Explosion.BlockInteraction blockInteraction, @Nullable Entity source, @Nullable Class sourceClass) {
        if (ignoreBreeze && sourceClass != null && BreezeWindCharge.class.isAssignableFrom(sourceClass))
            return;
        
        if (ignoreSelfWindCharge && source instanceof WindCharge w && w.getOwner() == player)
            return;
        
        if (ignoreMace && source == null && blockInteraction == BlockInteraction.TRIGGER_BLOCK && (isMace(player.registryAccess(), player.getMainHandItem()) || isMace(player
                .registryAccess(), player.getOffhandItem())))
            return;
        
        float f3 = size * 2.0F;
        double d12 = Math.sqrt(player.distanceToSqr(pos)) / f3;
        
        double d14 = Explosion.getSeenPercent(pos, player);
        double d10 = (1.0D - d12) * d14;
        
        float damage = ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * f3 + 1.0D));
        if (damage > 0) {
            VisualManager.addParticlesFadeOut(dust, this, (int) dustAmount.valueAt(damage), dustDuration, true);
            
            DecimalCurve explosionSoundVolume = new DecimalCurve(0, maxExplosionVolume, explosionSoundTime.valueAt(damage), 0);
            DecimalCurve explosionSoundMuteVolume = new DecimalCurve(0, 1, explosionSoundTime.valueAt(damage), 0);
            if (SoundMuteHandler.startMuting(explosionSoundMuteVolume))
                playSoundFadeOut(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "ringing"), null, explosionSoundVolume);
            
            VisualManager.addVisualFadeOut(blur, this, new DecimalCurve(0, maxBlur.valueAt(damage), (int) (explosionBlurTime.valueAt(damage)), 0));
        }
    }
}
