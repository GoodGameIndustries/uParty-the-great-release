package com.ggi.uparty.android;


import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ggi.uparty.NativeController;
import com.ggi.uparty.uParty;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class AndroidLauncher extends AndroidApplication implements NativeController, LocationListener{
	public LocationManager lm;
	public float lastLat=0,lastLong=0;
	
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		while(!checkPermission(this)){
		showPermissionDialog();
		try{
		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,0,this);
		}catch(Exception e){}
		}
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new uParty(this), config);
		
		
		
	}

	@Override
	public float getLong() { 
		//Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		//return location==null?0:(float) location.getLongitude();
		if(lm==null){lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);}
		try{
			return (float) lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
			}catch(Exception e){
				try{
				return (float) lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
				}catch(Exception e2){}
			}
		
		return 0;
		
		}

	@Override
	public float getLat() {
		//Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		//return location==null?0:(float) location.getLongitude();
		if(lm==null){lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);}
		try{
		return (float) lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
		}catch(Exception e){
			try{
			return (float) lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
			}catch(Exception e2){
				
			}
			}
	return 0;
	}
	
	 public static boolean checkPermission(final Context context) {
		 return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
		         && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
		  }
	 private void showPermissionDialog() {
		    //if (checkPermission(this)) {
		        ActivityCompat.requestPermissions(
		            this,
		            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
		            123);
		    //}
		}

	@Override
	public void onLocationChanged(Location location) {
		//lastLong=(float) location.getLongitude();
		//lastLat=(float) location.getLatitude();
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
