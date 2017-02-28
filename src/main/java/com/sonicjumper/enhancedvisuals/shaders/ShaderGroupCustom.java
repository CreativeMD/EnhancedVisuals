package com.sonicjumper.enhancedvisuals.shaders;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

@SideOnly(Side.CLIENT)
public class ShaderGroupCustom
{
    private Framebuffer mainFramebuffer;
    private IResourceManager resourceManager;
    private String shaderGroupName;
    private final List listShaders = Lists.newArrayList();
    private final Map mapFramebuffers = Maps.newHashMap();
    private final List listFramebuffers = Lists.newArrayList();
    private Matrix4f projectionMatrix;
    private int mainFramebufferWidth;
    private int mainFramebufferHeight;
    private float field_148036_j;
    private float field_148037_k;
    private static final String __OBFID = "CL_00001041";

    public ShaderGroupCustom(TextureManager p_i1050_1_, IResourceManager p_i1050_2_, Framebuffer p_i1050_3_, ResourceLocation p_i1050_4_) throws JsonException
    {
        this.resourceManager = p_i1050_2_;
        this.mainFramebuffer = p_i1050_3_;
        this.field_148036_j = 0.0F;
        this.field_148037_k = 0.0F;
        this.mainFramebufferWidth = p_i1050_3_.framebufferWidth;
        this.mainFramebufferHeight = p_i1050_3_.framebufferHeight;
        this.shaderGroupName = p_i1050_4_.toString();
        this.resetProjectionMatrix();
        this.parseGroup(p_i1050_1_, p_i1050_4_);
    }

    public void parseGroup(TextureManager p_152765_1_, ResourceLocation p_152765_2_) throws JsonException
    {
        JsonParser jsonparser = new JsonParser();
        InputStream inputstream = null;

        try
        {
            IResource iresource = this.resourceManager.getResource(p_152765_2_);
            inputstream = iresource.getInputStream();
            JsonObject jsonobject = jsonparser.parse(IOUtils.toString(inputstream, Charsets.UTF_8)).getAsJsonObject();
            JsonArray jsonarray;
            int i;
            Iterator iterator;
            JsonElement jsonelement;
            JsonException jsonexception1;

            if (JsonUtils.isJsonArray(jsonobject, "targets"))
            {
                jsonarray = jsonobject.getAsJsonArray("targets");
                i = 0;

                for (iterator = jsonarray.iterator(); iterator.hasNext(); ++i)
                {
                    jsonelement = (JsonElement)iterator.next();

                    try
                    {
                        this.initTarget(jsonelement);
                    }
                    catch (Exception exception1)
                    {
                        jsonexception1 = JsonException.forException(exception1);
                        jsonexception1.prependJsonKey("targets[" + i + "]");
                        throw jsonexception1;
                    }
                }
            }

            if (JsonUtils.isJsonArray(jsonobject, "passes"))
            {
                jsonarray = jsonobject.getAsJsonArray("passes");
                i = 0;

                for (iterator = jsonarray.iterator(); iterator.hasNext(); ++i)
                {
                    jsonelement = (JsonElement)iterator.next();

                    try
                    {
                        this.parsePass(p_152765_1_, jsonelement);
                    }
                    catch (Exception exception)
                    {
                        jsonexception1 = JsonException.forException(exception);
                        jsonexception1.prependJsonKey("passes[" + i + "]");
                        throw jsonexception1;
                    }
                }
            }
        }
        catch (Exception exception2)
        {
            JsonException jsonexception = JsonException.forException(exception2);
            jsonexception.prependJsonKey(p_152765_2_.getResourcePath());
            throw jsonexception;
        }
        finally
        {
            IOUtils.closeQuietly(inputstream);
        }
    }

    private void initTarget(JsonElement p_148027_1_) throws JsonException
    {
        if (JsonUtils.isString(p_148027_1_))
        {
            this.addFramebuffer(p_148027_1_.getAsString(), this.mainFramebufferWidth, this.mainFramebufferHeight);
        }
        else
        {
            JsonObject jsonobject = JsonUtils.getJsonObject(p_148027_1_, "target");
            String s = JsonUtils.getString(jsonobject, "name");
            int i = JsonUtils.getInt(jsonobject, "width", this.mainFramebufferWidth);
            int j = JsonUtils.getInt(jsonobject, "height", this.mainFramebufferHeight);

            if (this.mapFramebuffers.containsKey(s))
            {
                throw new JsonException(s + " is already defined");
            }

            this.addFramebuffer(s, i, j);
        }
    }

    private void parsePass(TextureManager p_152764_1_, JsonElement p_152764_2_) throws JsonException
    {
        JsonObject jsonobject = JsonUtils.getJsonObject(p_152764_2_, "pass");
        String s = JsonUtils.getString(jsonobject, "name");
        String s1 = JsonUtils.getString(jsonobject, "intarget");
        String s2 = JsonUtils.getString(jsonobject, "outtarget");
        Framebuffer framebuffer = this.getFramebuffer(s1);
        Framebuffer framebuffer1 = this.getFramebuffer(s2);

        if (framebuffer == null)
        {
            throw new JsonException("Input target \'" + s1 + "\' does not exist");
        }
        else if (framebuffer1 == null)
        {
            throw new JsonException("Output target \'" + s2 + "\' does not exist");
        }
        else
        {
            Shader shader = this.addShader(s, framebuffer, framebuffer1);
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "auxtargets", (JsonArray)null);

            if (jsonarray != null)
            {
                int i = 0;

                for (Iterator iterator = jsonarray.iterator(); iterator.hasNext(); ++i)
                {
                    JsonElement jsonelement1 = (JsonElement)iterator.next();

                    try
                    {
                        JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonelement1, "auxtarget");
                        String s4 = JsonUtils.getString(jsonobject1, "name");
                        String s3 = JsonUtils.getString(jsonobject1, "id");
                        Framebuffer framebuffer2 = this.getFramebuffer(s3);

                        if (framebuffer2 == null)
                        {
                            ResourceLocation resourcelocation = new ResourceLocation("textures/effect/" + s3 + ".png");

                            try
                            {
                                this.resourceManager.getResource(resourcelocation);
                            }
                            catch (FileNotFoundException filenotfoundexception)
                            {
                                throw new JsonException("Render target or texture \'" + s3 + "\' does not exist");
                            }

                            p_152764_1_.bindTexture(resourcelocation);
                            ITextureObject itextureobject = p_152764_1_.getTexture(resourcelocation);
                            int j = JsonUtils.getInt(jsonobject1, "width");
                            int k = JsonUtils.getInt(jsonobject1, "height");
                            boolean flag = JsonUtils.getBoolean(jsonobject1, "bilinear");

                            if (flag)
                            {
                                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                            }
                            else
                            {
                                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                            }

                            shader.addAuxFramebuffer(s4, Integer.valueOf(itextureobject.getGlTextureId()), j, k);
                        }
                        else
                        {
                            shader.addAuxFramebuffer(s4, framebuffer2, framebuffer2.framebufferTextureWidth, framebuffer2.framebufferTextureHeight);
                        }
                    }
                    catch (Exception exception1)
                    {
                        JsonException jsonexception = JsonException.forException(exception1);
                        jsonexception.prependJsonKey("auxtargets[" + i + "]");
                        throw jsonexception;
                    }
                }
            }

            JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonobject, "uniforms", (JsonArray)null);

            if (jsonarray1 != null)
            {
                int l = 0;

                for (Iterator iterator1 = jsonarray1.iterator(); iterator1.hasNext(); ++l)
                {
                    JsonElement jsonelement2 = (JsonElement)iterator1.next();

                    try
                    {
                        this.initUniform(jsonelement2);
                    }
                    catch (Exception exception)
                    {
                        JsonException jsonexception1 = JsonException.forException(exception);
                        jsonexception1.prependJsonKey("uniforms[" + l + "]");
                        throw jsonexception1;
                    }
                }
            }
        }
    }

    private void initUniform(JsonElement p_148028_1_) throws JsonException
    {
        JsonObject jsonobject = JsonUtils.getJsonObject(p_148028_1_, "uniform");
        String s = JsonUtils.getString(jsonobject, "name");
        ShaderUniform shaderuniform = ((Shader)this.listShaders.get(this.listShaders.size() - 1)).getShaderManager().getShaderUniform(s);

        if (shaderuniform == null)
        {
            throw new JsonException("Uniform \'" + s + "\' does not exist");
        }
        else
        {
            float[] afloat = new float[4];
            int i = 0;
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "values");

            for (Iterator iterator = jsonarray.iterator(); iterator.hasNext(); ++i)
            {
                JsonElement jsonelement1 = (JsonElement)iterator.next();

                try
                {
                    afloat[i] = JsonUtils.getFloat(jsonelement1, "value");
                }
                catch (Exception exception)
                {
                    JsonException jsonexception = JsonException.forException(exception);
                    jsonexception.prependJsonKey("values[" + i + "]");
                    throw jsonexception;
                }
            }

            switch (i)
            {
                case 0:
                default:
                    break;
                case 1:
                    shaderuniform.set(afloat[0]);
                    break;
                case 2:
                    shaderuniform.set(afloat[0], afloat[1]);
                    break;
                case 3:
                    shaderuniform.set(afloat[0], afloat[1], afloat[2]);
                    break;
                case 4:
                    shaderuniform.set(afloat[0], afloat[1], afloat[2], afloat[3]);
            }
        }
    }

    public Framebuffer getFramebufferRaw(String p_177066_1_)
    {
        return (Framebuffer)this.mapFramebuffers.get(p_177066_1_);
    }

    public void addFramebuffer(String p_148020_1_, int p_148020_2_, int p_148020_3_)
    {
        Framebuffer framebuffer = new Framebuffer(p_148020_2_, p_148020_3_, true);
        framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
        this.mapFramebuffers.put(p_148020_1_, framebuffer);

        if (p_148020_2_ == this.mainFramebufferWidth && p_148020_3_ == this.mainFramebufferHeight)
        {
            this.listFramebuffers.add(framebuffer);
        }
    }

    public void deleteShaderGroup()
    {
        Iterator iterator = this.mapFramebuffers.values().iterator();

        while (iterator.hasNext())
        {
            Framebuffer framebuffer = (Framebuffer)iterator.next();
            framebuffer.deleteFramebuffer();
        }

        iterator = this.listShaders.iterator();

        while (iterator.hasNext())
        {
            Shader shader = (Shader)iterator.next();
            shader.deleteShader();
        }

        this.listShaders.clear();
    }

    public Shader addShader(String p_148023_1_, Framebuffer p_148023_2_, Framebuffer p_148023_3_) throws JsonException
    {
        Shader shader = null;
		try {
			shader = new Shader(this.resourceManager, p_148023_1_, p_148023_2_, p_148023_3_);
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.listShaders.add(shader);
        return shader;
    }

    private void resetProjectionMatrix()
    {
        this.projectionMatrix = new Matrix4f();
        this.projectionMatrix.setIdentity();
        this.projectionMatrix.m00 = 2.0F / (float)this.mainFramebuffer.framebufferTextureWidth;
        this.projectionMatrix.m11 = 2.0F / (float)(-this.mainFramebuffer.framebufferTextureHeight);
        this.projectionMatrix.m22 = -0.0020001999F;
        this.projectionMatrix.m33 = 1.0F;
        this.projectionMatrix.m03 = -1.0F;
        this.projectionMatrix.m13 = 1.0F;
        this.projectionMatrix.m23 = -1.0001999F;
    }
    
    public boolean needNewFrameBuffer(int width, int height)
    {
    	return width != mainFramebufferWidth && height != mainFramebufferHeight;
    }
    
    public void createBindFramebuffers(int p_148026_1_, int p_148026_2_)
    {
        this.mainFramebufferWidth = p_148026_1_;
        this.mainFramebufferHeight = p_148026_2_;
        this.resetProjectionMatrix();
        Iterator iterator = this.listShaders.iterator();

        while (iterator.hasNext())
        {
            Shader shader = (Shader)iterator.next();
            shader.setProjectionMatrix(this.projectionMatrix);
        }

        iterator = this.listFramebuffers.iterator();

        while (iterator.hasNext())
        {
            Framebuffer framebuffer = (Framebuffer)iterator.next();
            framebuffer.createBindFramebuffer(p_148026_1_, p_148026_2_);
        }
    }

    public void loadShaderGroup(float partialTicks)
    {
        if (partialTicks < this.field_148037_k)
        {
            this.field_148036_j += 1.0F - this.field_148037_k;
            this.field_148036_j += partialTicks;
        }
        else
        {
            this.field_148036_j += partialTicks - this.field_148037_k;
        }

        for (this.field_148037_k = partialTicks; this.field_148036_j > 20.0F; this.field_148036_j -= 20.0F)
        {
            ;
        }

        Iterator iterator = this.listShaders.iterator();

        while (iterator.hasNext())
        {
            Shader shader = (Shader)iterator.next();
            shader.loadShader(this.field_148036_j / 20.0F);
        }
    }
    
    public List<Shader> getShaders() {
    	return listShaders;
    }

    public final String getShaderGroupName()
    {
        return this.shaderGroupName;
    }

    private Framebuffer getFramebuffer(String p_148017_1_)
    {
        return p_148017_1_ == null ? null : (p_148017_1_.equals("minecraft:main") ? this.mainFramebuffer : (Framebuffer)this.mapFramebuffers.get(p_148017_1_));
    }
}