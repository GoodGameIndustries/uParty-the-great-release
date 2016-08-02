package com.ggi.uparty;

import java.util.ArrayList;
import java.util.List;

import org.robovm.apple.corelocation.CLLocation;
import org.robovm.apple.corelocation.CLLocationManager;
import org.robovm.apple.corelocation.CLLocationManagerDelegateAdapter;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.ggi.uparty.uParty;

public class IOSLauncher extends IOSApplication.Delegate implements NativeController {
    public CLLocationManager c;
    public List<CLLocation> locationMeasurements = new ArrayList<CLLocation>();
	
	@Override
    protected IOSApplication createApplication() {
		
		c = new CLLocationManager();
		if(c.isLocationServicesEnabled()){
		c.setDelegate(new CLLocationManagerDelegateAdapter(){
			
			public void didUpdateLocations(CLLocationManager manager, NSArray<CLLocation> locations){
				CLLocation newLocation = locations.last();
				locationMeasurements.add(newLocation);
			}
			
		});
		c.setDesiredAccuracy(10);
		c.requestWhenInUseAuthorization();
		c.startUpdatingLocation();}
		
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new uParty(this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

	@Override
	public float getLong() {
		// TODO Auto-generated method stub
		return (float) locationMeasurements.get(locationMeasurements.size()-1).getCoordinate().getLongitude();
	}

	@Override
	public float getLat() {
		// TODO Auto-generated method stub
		return (float) locationMeasurements.get(locationMeasurements.size()-1).getCoordinate().getLatitude();
	}
}