package team.creative.enhancedvisuals.client.sound;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class PositionedSound extends AbstractSoundInstance {
    
    public PositionedSound(Identifier identifier, SoundSource category, float volume, float pitch) {
        super(identifier, category, RandomSource.create());
        this.volume = volume;
        this.pitch = pitch;
        this.attenuation = Attenuation.NONE;
    }
    
    public PositionedSound(Identifier identifier, SoundSource category, float volume, float pitch, BlockPos pos) {
        this(identifier, category, volume, pitch);
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.attenuation = Attenuation.LINEAR;
    }
    
}
