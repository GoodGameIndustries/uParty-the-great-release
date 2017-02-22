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
		return -85.48689550f; //Auburn
		//return -84.4906436f; //ATL
	}

	@Override
	public float getLat() {
		// TODO Auto-generated method stub
		return 32.60241910f; //Auburn
		//return 33.7676931f; //ATL
	}
}
