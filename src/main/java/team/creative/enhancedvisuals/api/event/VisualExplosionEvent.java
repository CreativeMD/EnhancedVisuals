package team.creative.enhancedvisuals.api.event;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class VisualExplosionEvent extends Event implements ICancellableEvent {
    
    public final float damage;
    
    protected float newDamage;
    protected boolean disableBlur = false;
    
    public VisualExplosionEvent(float damage) {
        this.damage = this.newDamage = damage;
    }
    
    public boolean isBlurDisabled() {
        return disableBlur;
    }
    
    public void setDisableBlur(boolean disabled) {
        this.disableBlur = disabled;
    }
    
    public void setNewDamage(float damage) {
        newDamage = damage;
    }
    
    public float getNewDamage() {
        return newDamage;
    }
    
}
