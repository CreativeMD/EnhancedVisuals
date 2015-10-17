package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.render.RenderShader;
import com.sonicjumper.enhancedvisuals.render.RenderShaderDefault;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;
import com.sonicjumper.enhancedvisuals.shaders.util.ShaderProgram;
import com.sonicjumper.enhancedvisuals.shaders.util.SpriteBatch;
import com.sonicjumper.enhancedvisuals.shaders.util.Texture;

public class Shader extends Visual {
	public Shader(VisualType type, int lifeTime) {
		super(type, lifeTime);
	}

	@Override
	public RenderVisual getRenderer() {
		return Base.instance.renderer.getRendererForClass(RenderShaderDefault.class);
	}
}
