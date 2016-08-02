package com.ggi.uparty.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ggi.uparty.NativeController;
import com.ggi.uparty.uParty;

public class DesktopLauncher implements NativeController {
	public DesktopLauncher(){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height=800;
		config.width=450;
		new LwjglApplication(new uParty(this), config);
	}
	
	public static void main (String[] arg) {
		new DesktopLauncher();
	}

	@Override
	public float getLong() {
		// TODO Auto-generated method stub
		return -84.23030830f;
	}

	@Override
	public float getLat() {
		// TODO Auto-generated method stub
		return 34.11953680f;
	}
}
