package com.mycompany.adventure;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher 
{
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
                //config.vSyncEnabled = true;
		config.setTitle("Small Adventure");
                config.setResizable(false);
                config.setWindowedMode(1000, 800);
                
                config.setWindowIcon("UI/icon.png");
		new Lwjgl3Application(new Main(), config);
	}
}
