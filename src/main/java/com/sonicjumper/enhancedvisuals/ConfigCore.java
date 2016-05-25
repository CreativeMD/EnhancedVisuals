package com.sonicjumper.enhancedvisuals;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigCore {
	private Configuration config;
	//public static String currentThemePack;
	//public static String backupThemePack;
	public static String defaultThemePack = "DefaultTheme";
	//public static boolean shouldRenderBlur;
	public static boolean useTrueGlow;
	public static float blurQuality;

	public ConfigCore(File configFile)
	{
		this.config = new Configuration(configFile);
	}

	public void loadConfig()
	{
		this.config.load();

		//currentThemePack = this.config.get("general", "Current Theme Pack", defaultThemePack, "Change this to the file name of any theme pack you have installed").getString();
		//backupThemePack = this.config.get("general", "Backup Theme Pack", defaultThemePack, "This 'fills in' the gaps left by the current theme pack").getString();

		//shouldRenderBlur = this.config.get("general", "Render Blur Flag", false, "Enable this to have blur filters render").getBoolean(false);
		useTrueGlow = this.config.get("general", "True Glow Flag", false, "Enable this to have better glows").getBoolean(false);

		blurQuality = (float)this.config.get("general", "Blur Quality", 0.25D, "Increase only if you think your computer can handle it. Decrease if you want to keep blurs, but are having lag.").getDouble(0.25D);
		
		
		
		this.config.save();
	}
}
