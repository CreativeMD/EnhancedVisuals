package team.creative.enhancedvisuals.common.handler;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.enhancedvisuals.api.Particle;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeParticle;
import team.creative.enhancedvisuals.client.VisualManager;

public class HealthHandler extends VisualHandler {
    
    @CreativeConfig
    public VisualType impact = new VisualTypeParticle("impact");
    
    @CreativeConfig
    public double particlesPerDamage = 1;
    
    private List<Particle> particles = new ArrayList<>();
    
    public HealthHandler() {
        enabled = false;
    }
    
    @Override
    public void tick(@Nullable Player player) {
        float damage = player == null ? 0 : player.getMaxHealth() - player.getHealth();
        int amount = Math.max(0, (int) Math.ceil(particlesPerDamage * damage));
        if (amount == particles.size())
            return;
        while (particles.size() < amount)
            particles.add(VisualManager.addParticle(impact, this, true, DamageHandler.BLOOD_COLOR));
        while (particles.size() > amount) {
            VisualManager.remove(particles.remove(particles.size() > 1 ? VisualManager.RANDOM.nextInt(particles.size() - 1) : 0));
        }
    }
    
}
