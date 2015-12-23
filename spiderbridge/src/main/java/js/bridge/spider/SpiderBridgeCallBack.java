package js.bridge.spider;

import com.eclipsesource.v8.V8Array;

/**
 * Created by stadiko on 12/22/15.
 */
public interface SpiderBridgeCallBack {
    void invokeVoidMethod(String methodName, V8Array parameters);

    Object invokeMethod(String methodName, V8Array parameters);
}
