package com.mygdx.ifh.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Screens.GameContainer;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		
	    cfg.title = "IFH Debug Build";

	    //cfg.width = 1920;
	    //cfg.height = 1080;
	    cfg.vSyncEnabled = true;
	    cfg.foregroundFPS = 60;
	    //cfg.backgroundFPS = 0;
	    cfg.resizable = false;

		new LwjglApplication(new GameContainer(), cfg);
	}
}
