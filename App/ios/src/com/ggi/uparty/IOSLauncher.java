package com.ggi.uparty;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.ggi.uparty.uParty;

public class IOSLauncher extends IOSApplication.Delegate implements NativeController {
    @Override
    protected IOSApplication createApplication() {
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
		return 0;
	}

	@Override
	public float getLat() {
		// TODO Auto-generated method stub
		return 0;
	}
}