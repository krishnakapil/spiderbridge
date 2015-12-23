package js.bridge.spider;


import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;

/**
 * Created by stadiko on 12/17/15.
 */
public abstract class SpiderBridge {
    protected V8 mV8;

    public SpiderBridge() {

    }

    protected void initV8() {
        mV8 = V8.createV8Runtime();
        String jsScript = getJsScript();
        if (jsScript == null || jsScript.length() == 0) {
            //Throw Error
            return;
        }
        mV8.executeVoidScript(getJsScript());
    }

    protected abstract String getJsScript();

    public int executeIntegerFunction(String name, V8Array parameters) {
        return mV8.executeIntegerFunction(name, parameters);
    }

    public double executeDoubleFunction(String name, V8Array parameters) {
        return mV8.executeDoubleFunction(name, parameters);
    }

    public String executeStringFunction(String name, V8Array parameters) {
        return mV8.executeStringFunction(name, parameters);
    }

    public boolean executeBooleanFunction(String name, V8Array parameters) {
        return mV8.executeBooleanFunction(name, parameters);
    }

    public V8Array executeArrayFunction(String name, V8Array parameters) {
        return mV8.executeArrayFunction(name, parameters);
    }

    public V8Object executeObjectFunction(String name, V8Array parameters) {
        return mV8.executeObjectFunction(name, parameters);
    }

    public Object executeFunction(String name, V8Array parameters) {
        return mV8.executeFunction(name, parameters);
    }

    public void executeVoidFunction(String name, V8Array parameters) {
        mV8.executeVoidFunction(name, parameters);
    }


    public void registerVoidCallBack(final String methodName, final SpiderBridgeCallBack callBack) {
        JavaVoidCallback v8CallBack = new JavaVoidCallback() {
            @Override
            public void invoke(V8Object v8Object, V8Array v8Array) {
                if (callBack != null) {
                    callBack.invokeVoidMethod(methodName, v8Array);
                }
            }
        };
        mV8.registerJavaMethod(v8CallBack, methodName);
    }

    public void registerCallBack(final String methodName, final SpiderBridgeCallBack callBack) {
        JavaCallback v8CallBack = new JavaCallback() {
            @Override
            public Object invoke(V8Object v8Object, V8Array v8Array) {
                if (callBack != null) {
                    return callBack.invokeMethod(methodName, v8Array);
                }
                return null;
            }
        };
        mV8.registerJavaMethod(v8CallBack, methodName);
    }

    public void releaseThread() {
        mV8.getLocker().release();
    }

    public void acquireThread() {
        mV8.getLocker().acquire();
    }

    /**
     * This method should be called to release the v8 JS Bridge
     */
    public void destroyBridge() {
        if (mV8 != null) {
            mV8.release();
        }
    }

    protected V8Array getNewV8Array() {
        return new V8Array(mV8);
    }
}
