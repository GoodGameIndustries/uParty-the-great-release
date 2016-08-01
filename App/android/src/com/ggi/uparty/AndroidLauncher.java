package com.ggi.uparty;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication implements NativeController{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new uParty(this), config);
	}

	@Override
	public float getLong() {
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		return (float) location.getLongitude();
	}

	@Override
	public float getLat() {
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		return (float) location.getLatitude();
	}
}
