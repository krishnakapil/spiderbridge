package js.bridge.test.bridge;

import android.content.Context;
import android.util.Log;

import com.eclipsesource.v8.V8Array;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import js.bridge.spider.SpiderBridge;
import js.bridge.spider.SpiderBridgeCallBack;

/**
 * Created by stadiko on 12/22/15.
 */
public class ExampleBridge implements SpiderBridgeCallBack {
    private static ExampleBridge sInstance;
    private SpiderBridge mSpiderBridge;

    private ExampleBridge() {
    }

    public static ExampleBridge getBridge() {
        return sInstance;
    }

    public static void initBridge(Context context) {
        if (sInstance == null) {
            sInstance = new ExampleBridge();
            sInstance.initJsScript(context);
            sInstance.registerMethods();
        }
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
            String source = buf.toString();
            mSpiderBridge = new SpiderBridge(source);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerMethods() {
        mSpiderBridge.registerVoidCallBack("processEvent", this);
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

    public void releaseThread() {
        mSpiderBridge.releaseThread();
    }

    public void acquireThread() {
        mSpiderBridge.acquireThread();
    }

    public void notifyTouch(String viewName) {
        V8Array params = mSpiderBridge.getNewV8Array().push(viewName);
        mSpiderBridge.executeVoidFunction("notifyTouchEvent", params);
    }

    public void notifySwipe(String viewName) {
        V8Array params = mSpiderBridge.getNewV8Array().push(viewName);
        mSpiderBridge.executeVoidFunction("notifySwipeEvent", params);
    }

    public void notifyClick(String viewName) {
        V8Array params = mSpiderBridge.getNewV8Array().push(viewName);
        mSpiderBridge.executeVoidFunction("notifyClickEvent", params);
    }

    public void destroyBridge() {
        mSpiderBridge.destroyBride();
    }
}
