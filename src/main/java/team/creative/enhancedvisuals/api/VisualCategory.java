package team.creative.enhancedvisuals.api;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.enhancedvisuals.api.property.ParticleProperties;
import team.creative.enhancedvisuals.api.property.VisualProperties;

public enum VisualCategory {
	
	overlay {
		@Override
		public boolean supportsProperties(VisualProperties properties) {
			return properties.getClass() == VisualProperties.class;
		}
		
		@Override
		public boolean isAffectedByWater() {
			return false;
		}
	},
	particle {
		@Override
		public boolean supportsProperties(VisualProperties properties) {
			return properties.getClass() == VisualProperties.class || properties.getClass() == ParticleProperties.class;
		}
		
		@Override
		public boolean isAffectedByWater() {
			return true;
		}
		
		@Override
		public int getWidth(VisualProperties properties, int screenWidth) {
			if (properties instanceof ParticleProperties)
				return ((ParticleProperties) properties).width;
			return screenWidth;
		}
		
		@Override
		public int getHeight(VisualProperties properties, int screenHeight) {
			if (properties instanceof ParticleProperties)
				return ((ParticleProperties) properties).height;
			return screenHeight;
		}
		
		@Override
		@OnlyIn(value = Dist.CLIENT)
		public void translate(VisualProperties properties) {
			if (properties instanceof ParticleProperties) {
				GlStateManager.translatef(((ParticleProperties) properties).x + ((ParticleProperties) properties).width / 2, ((ParticleProperties) properties).y + ((ParticleProperties) properties).height / 2, 0);
				GlStateManager.rotatef(((ParticleProperties) properties).rotation, 0, 0, 1);
			}
		}
	},
	shader {
		@Override
		public boolean supportsProperties(VisualProperties properties) {
			return properties.getClass() == VisualProperties.class;
		}
		
		@Override
		public boolean isAffectedByWater() {
			return false;
		}
	};
	
	@OnlyIn(value = Dist.CLIENT)
	public void translate(VisualProperties properties) {
		
	}
	
	public int getWidth(VisualProperties properties, int screenWidth) {
		return screenWidth;
	}
	
	public int getHeight(VisualProperties properties, int screenHeight) {
		return screenHeight;
	}
	
	public abstract boolean supportsProperties(VisualProperties properties);
	
	public abstract boolean isAffectedByWater();
}