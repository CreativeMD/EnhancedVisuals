package team.creative.enhancedvisuals.api;

public enum VisualCategory {
    
    overlay {
        
        @Override
        public boolean isAffectedByWater() {
            return false;
        }
    },
    particle {
        
        @Override
        public boolean isAffectedByWater() {
            return true;
        }
        
    },
    shader {
        @Override
        public boolean isAffectedByWater() {
            return false;
        }
    };
    
    public abstract boolean isAffectedByWater();
}