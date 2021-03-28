package team.creative.enhancedvisuals.common.handler;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.Tags.Blocks;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeParticle;
import team.creative.enhancedvisuals.client.VisualManager;

public class SandSplatHandler extends VisualHandler {
    
    @CreativeConfig
    public IntMinMax duration = new IntMinMax(100, 100);
    
    @CreativeConfig
    public double sprintModifier = 1.5F;
    
    @CreativeConfig
    public VisualType sand = new VisualTypeParticle("sand", 0, new DecimalMinMax(0.1, 0.5));
    
    @Override
    public void tick(@Nullable PlayerEntity player) {
        if (player != null && player.isOnGround() && isOnSand(player)) {
            double modifier = 0;
            if (player.isSprinting())
                modifier = sprintModifier;
            VisualManager.addParticlesFadeOut(sand, this, (int) (Math.random() * modifier), duration, true);
        }
    }
    
    private boolean isOnSand(PlayerEntity player) {
        BlockPos pos = new BlockPos(player.getPositionVec()).down();
        if (player.world.getBlockState(pos).getBlock().isIn(Blocks.SAND))
            return true;
        return false;
    }
    
}
