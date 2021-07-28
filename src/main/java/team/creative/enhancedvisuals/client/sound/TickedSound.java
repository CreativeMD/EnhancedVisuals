package team.creative.enhancedvisuals.client.sound;

import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;

public class TickedSound extends PositionedSound implements TickableSoundInstance {
    
    public int tick = 0;
    public DecimalCurve volumeGraph;
    
    public TickedSound(ResourceLocation location, SoundSource category, float pitch, DecimalCurve volumeGraph) {
        super(location, category, (float) volumeGraph.valueAt(0), pitch);
        this.volumeGraph = volumeGraph;
        this.looping = true;
    }
    
    public TickedSound(ResourceLocation location, SoundSource category, float pitch, BlockPos pos, DecimalCurve volumeGraph) {
        super(location, category, (float) volumeGraph.valueAt(0), pitch, pos);
        this.volumeGraph = volumeGraph;
        this.looping = true;
    }
    
    @Override
    public boolean isStopped() {
        return volume == 0;
    }
    
    @Override
    public void tick() {
        tick++;
        volume = (float) volumeGraph.valueAt(tick);
    }
    
}
