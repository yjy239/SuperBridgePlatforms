# SuperJsBridge
[![](https://jitpack.io/v/yjy239/SuperJsBridge.svg)](https://jitpack.io/#yjy239/SuperJsBridge)
一个对主流的JsBridge以及DsBridge的抽象合并，自带拦截器功能的js通信库。其实就是为了方便native Java端的开发。

SuperJsBridge 可以使用默认的老式大头鬼的jsbridge，就是通过拦截url协议的jsbridge。
也支持自定义。

# 如何使用：
请依赖：
```
	dependencies {
	        implementation 'com.github.yjy239.SuperJsBridge:superbridge:1.4'
	}
```

如果使用拦截器，需要在根部build.gradle，添加如下classpath：
```
classpath 'com.github.yjy239:AspectJHelper:1.1'
```


并且在每一个模块，主工程中加入
```
apply plugin: 'com.yjy.aspectjhelper'
```
值得一提的是，这个gradle插件本质上就是处理ascpectJ扫描时候需要写的冗余gradle脚本。如果需要使用ascpectJ又觉得写ascpectJ的gradle脚本麻烦，不放使用这个扫描插件

## native 调用js或者native提供方法给js

### native 注册方法提供js调用
首先可以通过声明一个继承BridgeInterface类，里面全部都是注册到jsBridge中的方法:
```
public class JsTest extends BridgeInterface{
    public void test1(String s){
        Log.e("JSTest",s) ;
    }


    public void submitFromWeb(String s2, CallBackFunction function){
       function.onCallBack("response: "+s2);
    }

}

```

记住，这里有校验，返回值只能是void或者String。参数列表必须是String+CallBackFunction，或者是单参数的String，才能注入成功。

接着我们需要声明一个Bridge对象,以供操作:
```
        final Bridge bridge =  new Bridge.Builder(view)
                .registerInterface("JsTest",new JsTest())
                .build();
```

registerInterface 的方法把JsTest中所有符合规则的方法批量注入到jsbridge中，并且为这个对象声明一个名字。

当我们需要单独注册一个提供给js方法，调用如下,和普通的jsBridge使用方式一致：
```
        bridge.registerHandler("test3", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack("response: data");
            }
        });

```
通过function.onCallBack回调结果给js。

### native 调用js注册好的方法
当我们需要调用js注册好的方法:
```
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bridge.callHandler("functionInJs","test test",new CallBackFunction(){
                    @Override
                    public void onCallBack(String data) {
                        Toast.makeText(JsBridgeActivity.this,data,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
```

可以通过callHandler调用js方法，并且在onCallBack获取js返回的对象。

## js 提供方法给native 或者 js调用native方法
js的使用方式和大头鬼的jsBridge使用一致:

### js初始化
```
        function connectWebViewJavascriptBridge(callback) {
            if (window.WebViewJavascriptBridge) {
                callback(WebViewJavascriptBridge)
            } else {
                document.addEventListener(
                    'WebViewJavascriptBridgeReady'
                    , function() {
                        callback(WebViewJavascriptBridge)
                    },
                    false
                );
            }
        }
```

### js注册方法给native调用
```
            bridge.registerHandler("functionInJs", function(data, responseCallback) {
                document.getElementById("show").innerHTML = ("data from Java: = " + data);
                if (responseCallback) {
                    var responseData = "Javascript Says Right back aka!";
                    responseCallback(responseData);
                }
            });

```

### js调用native的方法
```
            window.WebViewJavascriptBridge.callHandler(
                'submitFromWeb'
                , {'param': '中文测试'}
                , function(responseData) {
                    document.getElementById("show").innerHTML = "send get responseData from java, data = " + responseData
                }
            );
```

## 关于拦截器
SuperBridge中借助AspectJ字节码技术，实现了拦截器功能。

拦截器分为2种:
- 1.接受拦截器，是指js调用原生方法之后，在原生方法接受js参数之前进行了拦截，一般加上注解@ReceiverBridge
```
    @ReceiverBridge
    public void submitFromWeb(String s2, CallBackFunction function){
       function.onCallBack("submitFromWeb response: "+s2);
    }
```

- 2.发送拦截器，一般是指原生调用js之后，js获取到原生发送的参数之前进行了拦截,一般加上注解@SendBridge
一般的，在SuperBridge内部已经在Core中的callHandler方法中加上。


可以使用addInterceptor api，增加拦截:
```
new Bridge.Builder(view)
                .registerInterface("JsTest",new JsTest())
                .addInterceptor(new BridgeInterceptor<String,CallBackFunction>() {
                    @Override
                    public boolean receiverInterceptor(String data, CallBackFunction function) {
                        Log.e("calljs1","--------------");
                        return false;
                    }

                    @Override
                    public boolean sendInterceptor(String handlerName, String data, CallBackFunction callBack) {
                        Log.e("callNative1","--------------");
                        return false;
                    }
                })
                .addInterceptor(new BridgeInterceptor() {
                    @Override
                    public boolean receiverInterceptor(Object data, Object function) {
                        Log.e("calljs2","--------------");
                        return false;
                    }

                    @Override
                    public boolean sendInterceptor(String handlerName, String data, CallBackFunction callBack) {
                        Log.e("callNative2","--------------");
                        return true;
                    }
                })
                .build();

```
接受拦截器拦截器将会回调receiverInterceptor方法，发送拦截器回调sendInterceptor。一旦某一个拦截器返回true，将不会继续执行


## 如何自定义
在SuperJsBridge中，允许自定义。在demo中已经有一个自定义的DSBridge。

自定义需要做如下的事情：
- 1.自定义IBridgeCore，这是Bridge的核心操作类
```
public class DSCore extends BaseBridgeCore {
    private DWebView mWebView;
    ArrayList<String> mKeys = new ArrayList<>();

    public DSCore(DWebView webView){
        this.mWebView = webView;
    }

    @Override
    public void registerHandler(String handlerName, BridgeHandler handler, boolean isInterceptor) {
        if(mWebView == null){
            return;
        }



    }

    @Override
    public void unregisterHandler(String handlerName) {
        if(mWebView == null){
            return;
        }
        mWebView.removeJavascriptObject(handlerName);
    }

    public void addJsObject(Object obj,String name){
        if(mWebView == null){
            return;
        }
        mKeys.add(name);
        mWebView.addJavascriptObject(obj,name);
    }

    @Override
    public void callHandler(String handlerName, final String data, final CallBackFunction callBack, boolean isInterceptor) {
        if(mWebView == null){
            return;
        }
        Object[] args = {handlerName,data,callBack};
        if(BridgeHelper.iterSendInterceptor(this,args)){
            return;
        }


        OnReturnValue retValue = new OnReturnValue<Object>() {
            @Override
            public void onValue(Object retValue) {
                if(callBack != null){
                    if(retValue instanceof String){
                        callBack.onCallBack((String)retValue);
                    }else {
                        callBack.onCallBack(retValue == null?null:retValue.toString());
                    }
                }
            }
        };

        if(data == null){
            mWebView.callHandler(handlerName,retValue );
        }else{
            mWebView.callHandler(handlerName,new Object[]{data},retValue);
        }
    }

    @Override
    public void registerObj(String name, Object obj) {
        addJsObject(obj,name);
    }

    @Override
    public void release() {
    }
}
```
- 2.自定义一个IWebView，这是指一个实现了IWebView的WebView。
```
public class DSWebView extends DWebView implements IWebView {
    public DSWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DSWebView(Context context) {
        super(context);
    }
}

```

接着只需要参数为Bridge.IBuilder的build方法构建一个Bridge对象即可:
```
        Bridge bridge =  new Bridge.Builder(dwebView)
                .setClientAndCore(new DSCore(dwebView),new DSWebClient())
                .registerInterface("JsTest",new JsTest())
                .registerInterface(null,new JsApi())
                .registerInterface("echo",new JsEchoApi())
                .build();

```
在这里允许自己在build方法中处理自己的注入逻辑。由于DsBridge的核心原理是构建一个隐藏的_dsbridge的JS对象，
内置了call，return等方法，当js需要调用方法通过这个内置对象的反射注册到原生中对应类的方法。更加接近官方的@JavascriptInterface的方法。

naitve和js调用详情可见demo。

## 原因
因为原来Android端JsBridge出现一种特殊的情况，那就是通过url拦截方法有时候没有拦截到，导致js调用遗漏。
而@JavascriptInterface的方法就可以规避这个问题，
但是@JavascriptInterface的方式注入js对象会有一个问题，就是在4.2的机型下安全性有极大的问题，能够被外来js恶意操作。

因此提供了SuperJsBridge的方法，方便Android端能够快速的根据版本号等策略切换jsBridge

