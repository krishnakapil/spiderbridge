package js.bridge.test.bridge;

import android.content.Context;
import android.util.Log;

import com.eclipsesource.v8.V8Array;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import js.bridge.spider.SpiderBridge;
import js.bridge.spider.SpiderBridgeCallBack;

/**
 * Created by stadiko on 12/22/15.
 */
public class ExampleBridge extends SpiderBridge implements SpiderBridgeCallBack {
    private static ExampleBridge sInstance;

    private String mJsScript;

    private ExampleBridge() {
    }

    public static ExampleBridge getBridge() {
        return sInstance;
    }

    public static void initBridge(Context context) {
        if (sInstance == null) {
            sInstance = new ExampleBridge();
            sInstance.initJsScript(context);
            sInstance.initV8();
            sInstance.registerMethods();
        }
    }

    @Override
    protected String getJsScript() {
        return mJsScript;
    }

    private void initJsScript(Context context) {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = context.getAssets().open("test.js");
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }

            in.close();
            mJsScript = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            //Throw Exception
        }
    }

    private void registerMethods() {
        registerVoidCallBack("processEvent", this);
    }

    /**
     * This callBack method is used for JS to JAVA Function call without any return value.
     *
     * @param methodName
     * @param parameters
     */
    @Override
    public void invokeVoidMethod(String methodName, V8Array parameters) {
        if (methodName.equals("processEvent")) {
            if (parameters == null || parameters.length() == 0) {
                return;
            }
            String event = parameters.getString(0);
            processEvent(event);
        }
    }

    private void processEvent(String event) {
        Log.v(ExampleBridge.class.getSimpleName(), "Response - " + event);
    }

    /**
     * This callBack method is used for JS to JAVA Function Call. JS Expects return value.
     *
     * @param methodName
     * @param parameters
     * @return Object
     */
    @Override
    public Object invokeMethod(String methodName, V8Array parameters) {
        return null;
    }

    public void notifyTouch(String viewName) {
        V8Array params = getNewV8Array().push(viewName);
        executeVoidFunction("notifyTouchEvent", params);
    }

    public void notifySwipe(String viewName) {
        V8Array params = getNewV8Array().push(viewName);
        executeVoidFunction("notifySwipeEvent", params);
    }

    public void notifyClick(String viewName) {
        V8Array params = getNewV8Array().push(viewName);
        executeVoidFunction("notifyClickEvent", params);
    }
}
