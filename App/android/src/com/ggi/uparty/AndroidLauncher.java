package com.ggi.uparty;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ggi.uparty.uParty;

public class AndroidLauncher extends AndroidApplication implements NativeController{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new uParty(this), config);
	}

	@Override
	public float getLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLat() {
		// TODO Auto-generated method stub
		return 0;
	}
}
