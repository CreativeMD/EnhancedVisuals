package team.creative.enhancedvisuals.common.handler;

import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import team.creative.creativecore.Side;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.DecimalMinMax;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.creativecore.common.util.type.Color;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeParticleColored;
import team.creative.enhancedvisuals.client.VisualManager;

public class RainHandler extends VisualHandler {
    
    @CreativeConfig
    public IntMinMax duration = new IntMinMax(100, 100);
    
    @CreativeConfig
    public VisualType drop = new VisualTypeParticleColored("drop", 0, new DecimalMinMax(0.1, 0.5), new Color(41, 76, 149));
    
    @CreativeConfig
    public IntMinMax delay = new IntMinMax(0, 0);
    
    @CreativeConfig
    public IntMinMax amount = new IntMinMax(1, 1);
    
    private int timer = 0;
    private int nextDelay = -1;
    private Random rand = new Random();
    
    @Override
    public void tick(@Nullable Player player) {
        if (player != null) {
            BlockPos blockpos = player.blockPosition();
            if (player.level().isRainingAt(blockpos) || player.level().isRainingAt(BlockPos.containing(blockpos.getX(), player.getBoundingBox().maxY, blockpos.getZ()))) {
                
                timer++;
                if (nextDelay == -1)
                    nextDelay = delay.next(rand);
                if (timer >= nextDelay) {
                    VisualManager.addParticlesFadeOut(drop, this, amount.next(rand), duration, true);
                    timer = 0;
                    nextDelay = delay.next(rand);
                }
            } else
                timer = 0;
        }
    }
    
    @Override
    public void configured(Side side) {
        super.configured(side);
        nextDelay = -1;
    }
    
}
