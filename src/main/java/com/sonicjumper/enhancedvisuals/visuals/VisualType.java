package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.compress.archivers.zip.ZipFile;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.ClientProxy;
import com.sonicjumper.enhancedvisuals.ConfigCore;

import net.minecraft.util.ResourceLocation;

public class VisualType {
	public static final VisualType[] visualList = new VisualType[32];
	public static VisualType splatter = new VisualType(0, Visual.VisualCatagory.Splat, "splatter", true);
	public static VisualType impact = new VisualType(1, Visual.VisualCatagory.Splat, "impact", true);
	public static VisualType slash = new VisualType(2, Visual.VisualCatagory.Splat, "slash", true);
	public static VisualType pierce = new VisualType(3, Visual.VisualCatagory.Splat, "pierce", true);
	public static VisualType dust = new VisualType(4, Visual.VisualCatagory.Splat, "dust", true);
	public static VisualType fire = new VisualType(5, Visual.VisualCatagory.Splat, "fire", true);
	public static VisualType lavaS = new VisualType(6, Visual.VisualCatagory.Splat, "lava", true);
	public static VisualType sand = new VisualType(7, Visual.VisualCatagory.Splat, "sand", true);
	public static VisualType waterS = new VisualType(8, Visual.VisualCatagory.Splat, "water");
	public static VisualType snow = new VisualType(9, Visual.VisualCatagory.Splat, "snow");
	public static VisualType lowhealth = new VisualType(10, Visual.VisualCatagory.Overlay, "lowhealth");
	public static VisualType damaged = new VisualType(11, Visual.VisualCatagory.Overlay, "damaged");
	public static VisualType lavaO = new VisualType(12, Visual.VisualCatagory.Overlay, "lava", true);
	public static VisualType potion = new VisualType(13, Visual.VisualCatagory.Overlay, "potion", true);
	public static VisualType waterO = new VisualType(14, Visual.VisualCatagory.Overlay, "water");
	public static VisualType ice = new VisualType(15, Visual.VisualCatagory.Overlay, "ice");
	public static VisualType heat = new VisualType(16, Visual.VisualCatagory.Overlay, "heat");
	public static VisualType colorTemplate = new VisualType(17, Visual.VisualCatagory.Overlay, "colorTemplate");
	public static VisualType slender = new VisualType(18, Visual.VisualCatagory.Animation, "slender");
	public static VisualType crack = new VisualType(19, Visual.VisualCatagory.Animation, "crack");
	public static VisualType blur = new VisualType(20, Visual.VisualCatagory.Shader, "blur");
	public static VisualType defaultShader = new VisualType(21, Visual.VisualCatagory.Shader, "default");
	public static VisualType desaturate = new VisualType(22, Visual.VisualCatagory.Shader, "desaturate");
	private int visualID;
	private Visual.VisualCatagory visualCatagory;
	private String visualName;
	private String themePack;
	private Dimension imageDimensions;
	public ResourceLocation[] resourceArray;
	public boolean substractByTime;
	
	public VisualType(int id, Visual.VisualCatagory catagory, String name)
	{
		this(id, catagory, name, false);
	}
	
	public VisualType(int id, Visual.VisualCatagory catagory, String name, boolean substractByTime)
	{
		this.substractByTime = substractByTime;
		if (visualList[id] != null) {
			throw new IllegalArgumentException("Slot " + id + " is already occupied by " + visualList[id] + " when adding " + this);
		}
		visualList[id] = this;
		this.visualID = id;
		this.visualCatagory = catagory;
		this.visualName = name;
		try {
			createResources();
			for (ResourceLocation resourceLoc : this.resourceArray) {
				Base.log.info(resourceLoc.getResourcePath());
			}
		} catch(Exception e) {
			e.printStackTrace();
			Base.log.error("[Enhanced Visuals] Could not find the directory, make sure you installed the mod correctly: " + ClientProxy.getVisualsDirectory(this.themePack) + this.visualCatagory.toString() + "/" + this.visualName);
		}
		if(resourceArray != null)
			Base.log.info(id + " || " + this.visualCatagory.toString() + " || " + name + " || " + this.resourceArray.length);
	}

	public void createResources()
	{
		this.themePack = ConfigCore.defaultThemePack;
		this.resourceArray = createResourcesForVisualType(this);
		/*if ((this.resourceArray == null) || ((this.resourceArray != null) && (this.resourceArray.length == 0)))
		{
			System.out.println("[Enhanced Visuals] Using backups for:" + this.visualName);
			this.themePack = ConfigCore.backupThemePack;
			try
			{
				this.resourceArray = createResourcesForVisualType(this);
			}
			catch (FileNotFoundException e)
			{
				System.out.println("[Enhanced Visuals] Error finding backup directory, make sure you installed the theme pack correctly: " + ClientProxy.getVisualsDirectory(this.themePack) + this.visualCatagory.toString() + "/" + this.visualName);
			}
			if ((this.resourceArray == null) || ((this.resourceArray != null) && (this.resourceArray.length == 0)))
			{
				System.out.println("[Enhanced Visuals] Using defaults for:" + this.visualName);
				this.themePack = ConfigCore.defaultThemePack;
				try
				{
					this.resourceArray = createResourcesForVisualType(this);
				}
				catch (FileNotFoundException e)
				{
					System.out.println("[Enhanced Visuals] Error finding default directory, make sure you installed the mod correctly: " + ClientProxy.getVisualsDirectory(this.themePack) + this.visualCatagory.toString() + "/" + this.visualName);
				}
			}
		}*/
	}

	public static VisualType addNewType(int id, Visual.VisualCatagory catagory, String name)
	{
		System.out.println("Adding id:" + id + " into catagory:" + catagory.toString() + " with name:" + name);
		return new VisualType(id, catagory, name);
	}

	private ResourceLocation[] createResourcesForVisualType(VisualType vt)
	{
		List<ResourceLocation> result = new LinkedList();
		String visualNamePath = this.themePack + "/visuals/" + vt.visualCatagory.toString() + "/" + vt.visualName + "/";
		
		// Read files normally in developer's environment
		File f = new File(ClientProxy.getVisualsDirectory(this.themePack) + this.visualCatagory.toString() + "/" + this.visualName + "/");

		File[] list = f.listFiles();
		if (list != null) {
			int matchedIndex = 0;
			for (int i = 0; i < list.length; i++) {
				if (list[i].getName().toLowerCase().contains(this.visualName.toLowerCase())) {
					result.add(matchedIndex++, new ResourceLocation(Base.MODID, this.themePack + "/visuals/" + vt.visualCatagory.toString() + "/" + vt.visualName + "/" + list[i].getName()));
				}
			}
			if (matchedIndex > 0)
			{
				File firstImage = new File(ClientProxy.getVisualsDirectory(this.themePack) + vt.visualCatagory.toString() + "/" + vt.visualName + "/" + new File(((ResourceLocation)result.get(0)).getResourcePath()).getName());
				try {
					this.imageDimensions = getDimensionsOfImage(firstImage);
				} catch (Exception e) {
					Base.log.warn("Could not read dimensions of image: " + f.getPath() + "; maybe it isn't an image?");
				}
			}
			else
			{
				this.imageDimensions = new Dimension(0, 0);
			}
		}
		
		if(result.size() == 0) {
			// We may be in a zipped folder, try alternative method
			try {
				URL url = new File(ClientProxy.baseJarPath).toURI().toURL();
				InputStream in = url.openStream();
				ZipFile zipFile = new ZipFile(new File(ClientProxy.baseJarPath));
				ZipInputStream zip = new ZipInputStream(in);
				
				ZipEntry entry = null;
				while((entry = zip.getNextEntry()) != null) {
					String resourcePath = entry.getName().replace("assets/" + Base.MODID + "/", "");
					String[] resourcePathArray = resourcePath.split("/");
					if(resourcePath.contains(visualNamePath) && !resourcePath.equalsIgnoreCase(visualNamePath) && resourcePathArray[resourcePathArray.length - 1].toLowerCase().contains(this.visualName.toLowerCase())) {
						result.add(new ResourceLocation(Base.MODID, resourcePath));
						
						try {
							BufferedImage bi = ImageIO.read(zipFile.getInputStream(zipFile.getEntry(entry.getName())));
							this.imageDimensions = new Dimension(bi.getWidth(), bi.getHeight());
						} catch(Exception e) {
							Base.log.warn("Could not read dimensions of image: " + entry.getName() + "; maybe it isn't an image?");
							this.imageDimensions = new Dimension(0, 0);
						}
					}
				}
				zipFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return (ResourceLocation[])result.toArray(new ResourceLocation[0]);
	}

	private Dimension getDimensionsOfImage(File resourceFile) throws IOException
	{
		ImageInputStream in = ImageIO.createImageInputStream(resourceFile);
		return getDimensionsOfImage(in);
	}
	
	private Dimension getDimensionsOfImage(ImageInputStream in) throws IOException
	{
		try
		{
			Iterator readers = ImageIO.getImageReaders(in);
			if (readers.hasNext())
			{
				ImageReader reader = (ImageReader)readers.next();
				try
				{
					reader.setInput(in);
					Dimension localDimension = new Dimension(reader.getWidth(0), reader.getHeight(0));

					reader.dispose();

					return localDimension;
				}
				finally
				{
					reader.dispose();
				}
			}
		}
		finally
		{
			if (in != null) {
				in.close();
			}
		}
		return new Dimension(0, 0);
	}

	public int getSize()
	{
		return this.imageDimensions != null ? this.imageDimensions.height : 0;
	}

	public Visual.VisualCatagory getCatagory()
	{
		return this.visualCatagory;
	}

	public int getID()
	{
		return this.visualID;
	}

	public String getName() {
		return visualName;
	}
}
