package com.sonicjumper.enhancedvisuals.render;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class BlurHelper {
	private static IntBuffer intBuffer;
	private static ByteBuffer byteBuffer;
    private static int[] intDataArray;
    private static byte[] byteDataArray;

	public static BufferedImage scaleImage(BufferedImage image, float scale) {
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return scaleOp.filter(image, new BufferedImage((int) (image.getWidth() * scale), (int) (image.getHeight() * scale), BufferedImage.TYPE_INT_RGB));
	}
	
	public static BufferedImage captureScreenAsImage() {
		return captureScreenAsImage(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	}

	public static BufferedImage captureScreenAsImage(int par1, int par2) {
		int k = par1 * par2;

        if (intBuffer == null || intBuffer.capacity() < k)
        {
            intBuffer = BufferUtils.createIntBuffer(k);
            intDataArray = new int[k];
        }
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        
        long time = Minecraft.getSystemTime();
        intBuffer.clear();
        GL11.glReadPixels(0, 0, par1, par2, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, intBuffer);
        System.out.println("Read Pixels Time: " + (Minecraft.getSystemTime() - time));
        
        intBuffer.get(intDataArray);
        flipImage(intDataArray, par1, par2);
        
        time = Minecraft.getSystemTime();
        BufferedImage bufferedImage = new BufferedImage(par1, par2, 1);
        setRGB(bufferedImage, 0, 0, par1, par2, intDataArray);
        System.out.println("New Image Creation Time: " + (Minecraft.getSystemTime() - time));
        
        return bufferedImage;
	}
	
	/*public static BufferedImage captureScreenAsGrayscaleImage(int par1, int par2) {
		int k = par1 * par2;

        if (intBuffer == null || intBuffer.capacity() < k)
        {
            intBuffer = BufferUtils.createIntBuffer(k);
            intDataArray = new int[k];
        }
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        
        long time = System.nanoTime();
        intBuffer.clear();
        GL11.glPixelTransferf(GL11.GL_RED_SCALE, 0.299f);
        GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, 0.587f);
        GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, 0.114f);
        GL11.glReadBuffer(GL11.GL_BACK);
        GL11.glReadPixels(0, 0, par1, par2, GL11.GL_LUMINANCE, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, intBuffer);
        GL11.glPixelTransferf(GL11.GL_RED_SCALE, 1f);
        GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, 1f);
        GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, 1f);
        System.out.println("Read Pixels Time: " + (Minecraft.getSystemTime() - time));
        
        intBuffer.get(intDataArray);
        flipImage(intDataArray, par1, par2);
        
        time = System.nanoTime();
        BufferedImage bufferedImage = new BufferedImage(par1, par2, 1);
        setRGB(bufferedImage, 0, 0, par1, par2, intDataArray);
        System.out.println("New Image Creation Time: " + (Minecraft.getSystemTime() - time));
        
        return bufferedImage;
	}
	
	public static ByteBuffer captureScreenAsGrayscaleBuffer(int par1, int par2) {
		int k = par1 * par2;
		
        if (byteBuffer == null || byteBuffer.capacity() < k)
        {
        	byteBuffer = BufferUtils.createByteBuffer(k);
            byteDataArray = new byte[k];
        }
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        
        long time = Minecraft.getSystemTime();
        byteBuffer.clear();
        GL11.glPixelTransferf(GL11.GL_RED_SCALE, 0.299f);
        GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, 0.587f);
        GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, 0.114f);
        GL11.glReadBuffer(GL11.GL_BACK);
        GL11.glReadPixels(0, 0, par1, par2, GL11.GL_LUMINANCE, GL11.GL_UNSIGNED_BYTE, byteBuffer);
        GL11.glPixelTransferf(GL11.GL_RED_SCALE, 1f);
        GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, 1f);
        GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, 1f);
        System.out.println("Read Pixels Time: " + (Minecraft.getSystemTime() - time));
        
        byteBuffer.get(byteDataArray);
        flipImage(byteDataArray, par1, par2);
        
        return byteBuffer;
	}*/

    private static void flipImage(int[] par0ArrayOfInteger, int par1, int par2)
    {
        int[] aint1 = new int[par1];
        int k = par2 / 2;

        for (int l = 0; l < k; ++l)
        {
            System.arraycopy(par0ArrayOfInteger, l * par1, aint1, 0, par1);
            System.arraycopy(par0ArrayOfInteger, (par2 - 1 - l) * par1, par0ArrayOfInteger, l * par1, par1);
            System.arraycopy(aint1, 0, par0ArrayOfInteger, (par2 - 1 - l) * par1, par1);
        }
    }

    private static void flipImage(byte[] par0ArrayOfByte, int par1, int par2)
    {
        byte[] aint1 = new byte[par1];
        int k = par2 / 2;

        for (int l = 0; l < k; ++l)
        {
            System.arraycopy(par0ArrayOfByte, l * par1, aint1, 0, par1);
            System.arraycopy(par0ArrayOfByte, (par2 - 1 - l) * par1, par0ArrayOfByte, l * par1, par1);
            System.arraycopy(aint1, 0, par0ArrayOfByte, (par2 - 1 - l) * par1, par1);
        }
    }
    
    private static void setRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
		int type = image.getType();
		if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB)
			image.getRaster().setDataElements( x, y, width, height, pixels );
		else
			image.setRGB( x, y, width, height, pixels, 0, width );
    }
}
