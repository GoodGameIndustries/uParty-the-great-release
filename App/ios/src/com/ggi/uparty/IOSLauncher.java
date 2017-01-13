package com.ggi.uparty;

import java.util.ArrayList;
import java.util.List;

import org.robovm.apple.corelocation.CLAuthorizationStatus;
import org.robovm.apple.corelocation.CLBeaconRegion;
import org.robovm.apple.corelocation.CLHeading;
import org.robovm.apple.corelocation.CLLocation;
import org.robovm.apple.corelocation.CLLocationManager;
import org.robovm.apple.corelocation.CLLocationManagerDelegate;
import org.robovm.apple.corelocation.CLLocationManagerDelegateAdapter;
import org.robovm.apple.corelocation.CLRegion;
import org.robovm.apple.corelocation.CLRegionState;
import org.robovm.apple.corelocation.CLVisit;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.objc.Selector;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.ggi.uparty.uParty;

public class IOSLauncher extends IOSApplication.Delegate implements NativeController, CLLocationManagerDelegate {
    public CLLocationManager manager;
    public List<CLLocation> locationMeasurements = new ArrayList<CLLocation>();
	
	@Override
    protected IOSApplication createApplication() {
		//check if GPS is active
		boolean gpsAvailable = CLLocationManager.isLocationServicesEnabled() && CLLocationManager.getAuthorizationStatus() != CLAuthorizationStatus.Denied;

		manager = new CLLocationManager();
		manager.setDelegate(this);
		locationMeasurements = new ArrayList<CLLocation>();
		//Needed for ios8+
				if (manager.respondsToSelector(Selector.register("requestWhenInUseAuthorization"))) {
				   manager.requestWhenInUseAuthorization();
				}
		
		manager.startUpdatingLocation();

		
		
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
		if(locationMeasurements!=null && locationMeasurements.size()>0){
		return (float) locationMeasurements.get(locationMeasurements.size()-1).getCoordinate().getLongitude();
		}
		createApplication();
		manager.startUpdatingLocation();
		return 0;
	}

	@Override
	public float getLat() {
		if(locationMeasurements!=null && locationMeasurements.size()>0){
			return (float) locationMeasurements.get(locationMeasurements.size()-1).getCoordinate().getLatitude();
			}
			createApplication();
			manager.startUpdatingLocation();
			return 0;
		}

	@Override
	public void didChangeAuthorizationStatus(CLLocationManager arg0, CLAuthorizationStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didDetermineState(CLLocationManager arg0, CLRegionState arg1, CLRegion arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didEnterRegion(CLLocationManager arg0, CLRegion arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didExitRegion(CLLocationManager arg0, CLRegion arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didFail(CLLocationManager arg0, NSError arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didFinishDeferredUpdates(CLLocationManager arg0, NSError arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didPauseLocationUpdates(CLLocationManager arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didRangeBeacons(CLLocationManager arg0, NSArray<?> arg1, CLBeaconRegion arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didResumeLocationUpdates(CLLocationManager arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didStartMonitoring(CLLocationManager arg0, CLRegion arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didUpdateHeading(CLLocationManager arg0, CLHeading arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didUpdateLocations(CLLocationManager arg0, NSArray<CLLocation> arg1) {
		if(arg0!=null && arg1!=null &&locationMeasurements!=null){
		locationMeasurements.addAll(arg1);
		}else{
			locationMeasurements = new ArrayList<CLLocation>();
			locationMeasurements.addAll(arg1);
		}
	}

	@Override
	public void didUpdateToLocation(CLLocationManager arg0, CLLocation arg1, CLLocation arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didVisit(CLLocationManager arg0, CLVisit arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void monitoringDidFail(CLLocationManager arg0, CLRegion arg1, NSError arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rangingBeaconsDidFail(CLLocationManager arg0, CLBeaconRegion arg1, NSError arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldDisplayHeadingCalibration(CLLocationManager arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}