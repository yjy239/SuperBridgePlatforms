package com.yjy.superjsbridgedemo.bridge;

import android.os.CountDownTimer;
import android.util.Log;

import com.yjy.superbridge.internal.BridgeField;
import com.yjy.superbridge.internal.BridgeInterface;
import com.yjy.superbridge.internal.BridgeMethod;
import com.yjy.superbridge.internal.CallBackHandler;
import com.yjy.superjsbridgedemo.model.TestObj;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by du on 16/12/31.
 */

public class JsApi  {
    @BridgeMethod
    public String testSyn(String msg)  {
        Log.e("Type",msg.getClass().toString());
        return msg + "syn call";
    }

//    @BridgeMethod
//    public void testAsyn(Object msg, CallBackHandler<String> handler){
//        Log.e("Type",msg.getClass().toString());
//        handler.complete(msg+" [ asyn call]");
//    }

    @BridgeMethod
    public void testAsyn(@BridgeField(name = "msg") String msg,
                         @BridgeField(name = "object") TestObj obj, CallBackHandler<String> handler){
        Log.e("object",obj.name);
        handler.complete(msg+" asyn call");
    }

    @BridgeMethod
    public String testNoArgSyn() throws JSONException {
        return  "testNoArgSyn called [ syn call]";
    }

    @BridgeMethod
    public void testNoArgAsyn(CallBackHandler<String> handler) {
        handler.complete( "testNoArgAsyn   called [ asyn call]");
    }


    @BridgeMethod
    //without @JavascriptInterface annotation can't be called
    public String testNever(Object arg) throws JSONException {
        JSONObject jsonObject= (JSONObject) arg;

        return jsonObject.getString("msg") + "[ never call]";
    }

    @BridgeMethod
    public void callProgress(Object args, final CallBackHandler<Integer> handler) {
        new CountDownTimer(11000, 1000) {
            int i=10;
            @Override
            public void onTick(long millisUntilFinished) {
                //setProgressData can be called many times util complete be called.
                handler.setProgressData((i--));

            }
            @Override
            public void onFinish() {
                //complete the js invocation with data; handler will be invalid when complete is called
                handler.complete(0);

            }
        }.start();
    }
}