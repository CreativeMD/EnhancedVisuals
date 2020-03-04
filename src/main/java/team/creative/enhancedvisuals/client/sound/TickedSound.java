package team.creative.enhancedvisuals.client.sound;

import com.creativemd.creativecore.common.config.premade.curve.DecimalCurve;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class TickedSound extends PositionedSound implements ITickableSound {
	
	public int tick = 0;
	public DecimalCurve volumeGraph;
	
	public TickedSound(ResourceLocation location, SoundCategory category, float pitch, DecimalCurve volumeGraph) {
		super(location, category, (float) volumeGraph.valueAt(0), pitch);
		this.volumeGraph = volumeGraph;
		this.repeat = true;
	}
	
	public TickedSound(ResourceLocation location, SoundCategory category, float pitch, BlockPos pos, DecimalCurve volumeGraph) {
		super(location, category, (float) volumeGraph.valueAt(0), pitch, pos);
		this.volumeGraph = volumeGraph;
		this.repeat = true;
	}
	
	@Override
	public boolean isDonePlaying() {
		return volume == 0;
	}
	
	@Override
	public void update() {
		tick++;
		volume = (float) volumeGraph.valueAt(tick);
	}
	
}
