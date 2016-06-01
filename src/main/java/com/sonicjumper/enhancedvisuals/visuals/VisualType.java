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
import java.util.ArrayList;
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
import com.sonicjumper.enhancedvisuals.visuals.types.BlurType;
import com.sonicjumper.enhancedvisuals.visuals.types.DrownType;
import com.sonicjumper.enhancedvisuals.visuals.types.DustType;
import com.sonicjumper.enhancedvisuals.visuals.types.FireType;
import com.sonicjumper.enhancedvisuals.visuals.types.SandType;
import com.sonicjumper.enhancedvisuals.visuals.types.SaturationType;
import com.sonicjumper.enhancedvisuals.visuals.types.SlenderType;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class VisualType {
	//public static final VisualType[] visualList = new VisualType[32];
	
	public static ArrayList<VisualType> visuals = new ArrayList<>();
	
	public static VisualType splatter = new VisualType(Visual.VisualCatagory.Splat, "splatter", "blood splatter", true);
	public static VisualType impact = new VisualType(Visual.VisualCatagory.Splat, "impact", "blunt impact", true);
	public static VisualType slash = new VisualType(Visual.VisualCatagory.Splat, "slash", "sharp slash", true);
	public static VisualType pierce = new VisualType(Visual.VisualCatagory.Splat, "pierce", "arrow pierce", true);
	public static DustType dust = new DustType();
	public static FireType fire = new FireType();
	public static SandType sand = new SandType();
	public static DrownType waterS = new DrownType();
	
	public static VisualType lowhealth = new VisualType(Visual.VisualCatagory.Overlay, "lowhealth", "heartbeat overlay");
	public static VisualType potion = new VisualType(Visual.VisualCatagory.Overlay, "potion", "splash potion effect", true);
	
	public static SlenderType slender = new SlenderType();
	
	public static BlurType blur = new BlurType();
	public static SaturationType desaturate = new SaturationType();
	
	//Config
	public boolean enabled = true;
	public float alpha = 1.0F;
	
	private Visual.VisualCatagory visualCatagory;
	private String visualName;
	public String comment;
	private String themePack;
	private Dimension imageDimensions;
	public ResourceLocation[] resourceArray;
	public boolean substractByTime;
	
	public VisualType(Visual.VisualCatagory catagory, String name, String comment)
	{
		this(catagory, name, comment, false);
	}
	
	public VisualType(Visual.VisualCatagory catagory, String name, String comment, boolean substractByTime)
	{
		this.substractByTime = substractByTime;
		this.comment = comment;
		/*f (visualList[id] != null) {
			throw new IllegalArgumentException("Slot " + id + " is already occupied by " + visualList[id] + " when adding " + this);
		}
		visualList[id] = this;*/
		visuals.add(this);
		//this.visualID = id;
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
			Base.log.info(this.visualCatagory.toString() + " || " + name + " || " + this.resourceArray.length);
	}
	
	public void loadConfig(Configuration config)
	{
		this.enabled = config.getBoolean("enabled", this.getName(), true, this.comment);
		this.alpha = config.getFloat("alphaFactor", this.getName(), 1, 0, 1, "alpha = normalAlpha * alphaFactor");
	}

	public void createResources()
	{
		this.themePack = ConfigCore.defaultThemePack;
		this.resourceArray = createResourcesForVisualType(this);
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

	public String getName() {
		return visualName;
	}
}
