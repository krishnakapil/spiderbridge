package js.bridge.test;

import android.app.Application;

import js.bridge.test.bridge.ExampleBridge;

/**
 * Created by stadiko on 12/22/15.
 */
public class JSBridgeApplication extends Application {
    public JSBridgeApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ExampleBridge.initBridge(getApplicationContext());
    }
}
