package team.creative.enhancedvisuals.client.sound;

import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;

public class TickedSound extends PositionedSound implements TickableSoundInstance {
    
    public int tick = 0;
    public DecimalCurve volumeGraph;
    
    public TickedSound(Identifier identifier, SoundSource category, float pitch, DecimalCurve volumeGraph) {
        super(identifier, category, (float) volumeGraph.valueAt(0), pitch);
        this.volumeGraph = volumeGraph;
        this.looping = true;
    }
    
    public TickedSound(Identifier identifier, SoundSource category, float pitch, BlockPos pos, DecimalCurve volumeGraph) {
        super(identifier, category, (float) volumeGraph.valueAt(0), pitch, pos);
        this.volumeGraph = volumeGraph;
        this.looping = true;
    }
    
    @Override
    public boolean isStopped() {
        return volume == 0;
    }
    
    public void stop() {
        volume = 0;
        tick = (int) Math.ceil(volumeGraph.max);
    }
    
    @Override
    public void tick() {
        tick++;
        volume = (float) volumeGraph.valueAt(tick);
    }
    
}
