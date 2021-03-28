package team.creative.enhancedvisuals.common.handler;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.premade.DecimalMinMax;
import com.creativemd.creativecore.common.config.premade.IntMinMax;

import net.minecraft.block.BlockSand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
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
    public void tick(@Nullable EntityPlayer player) {
        if (player != null && player.onGround && isOnSand(player)) {
            double modifier = 0;
            if (player.isSprinting())
                modifier = sprintModifier;
            VisualManager.addParticlesFadeOut(sand, this, (int) (Math.random() * modifier), duration, true);
        }
    }
    
    private boolean isOnSand(EntityPlayer player) {
        BlockPos pos = player.getPosition().down();
        if (player.world.getBlockState(pos).getBlock() instanceof BlockSand)
            return true;
        return false;
    }
    
}
