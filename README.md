# SuperBridgePlatforms
I'm Super Class of All of Native Bridge

# How To use
- Firstly, create a Bridge:
```java
public class JsEchoApi {

    @BridgeMethod(interceptor = true)
    public Object syn(@BridgeField(name = "msg") String msg,
                      @BridgeField(name = "tag") String tag) throws JSONException {
        Log.e("msg",msg);
        Log.e("tag",tag);
        return  msg;
    }

    @BridgeMethod
    public void asyn(@BridgeField(name = "msg") String msg,
                     @BridgeField(name = "tag") String tag, CallBackHandler handler){
        Log.e("msg",msg);
        Log.e("tag",tag);
        handler.complete(tag);
    }
}
```

  - @BridgeMethod means this Java Method is a bridge of ReactNaive/flutter/webview
  - @BridgeField will be format the Object and get the named parameters 。for example ，when js/RN/flutter send a Object which like below：
  ```
  {
    msg: "aaa",
    tag: "bbb"
  }
  ```
  @BridgeField will get the content of tag and inject to syn method.
  
  #### if you didn't used `@BridgeField`,you must set the first parameter as String Type.
  
  


- Secondly, create a Total Bridge and register Obj:
```java
        final Bridge bridge =  new Bridge.Builder()
                .registerInterface("JsTest",new JsTest())
                .registerInterface(null,new JsApi())
                .registerInterface("echo",new JsEchoApi())
                .setClientFactory(new DefaultJsBridgeFactory(view))
                .setConvertFactory(GsonConvertFactory.create(new Gson()))
                .build();
```

#### be careful: you can set the ConvertFactory. this factory will tell the BridgeCore how to format the Object form webview/rn/flutter

registerInterface will register a Object into Bridge. if js want to invoke `sync` Method in JsEchoApi,must call `echo.sync`.

SuperBridge will find namespace at first.

if you don't want to set namespace,you can set `null` or `"default"`，this means thie register Obj is a Whole Bridge.

#### be careful: don't set same key of register Object

you can also register a single method to Bridge:
```java
        bridge.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackHandler<String> function) {
                function.complete("response: data");
            }
        },true);
```

At the sameTime, we can invoke js/RN/Flutter method：
```java
                bridge.callHandler("functionInJs","test test",new CallBackFunction(){
                    @Override
                    public void complete(String data) {
                        Toast.makeText(JsBridgeActivity.this,data,Toast.LENGTH_SHORT).show();
                    }
                },false);
```


and Enjoy it !!!

## Change the bridge mode
There are 4 Bridges in SuperBirdge:

- DefaultJsBridgeFactory: [JsBridge https://github.com/lzyzsd/JsBridge](https://github.com/lzyzsd/JsBridge)
- DefaultDsBridgeFactory: [DsBridge https://github.com/wendux/DSBridge-Android](https://github.com/wendux/DSBridge-Android)
- DefaultRnFactory : [React native 0.62](https://github.com/facebook/react-native)
- DefaultFlutterBridgeFactory: [flutter](https://github.com/flutter/engine)

if you want to create different mode of Bridge:
### JsBridge
```java
 final Bridge bridge =  new Bridge.Builder()
                .setClientFactory(new DefaultJsBridgeFactory(view))
                .setConvertFactory(GsonConvertFactory.create(new Gson()))
                .registerInterface("echo",new JsEchoApi())
                .registerInterface(null,new JsTest())
                .build();
```

- In js/ts,you can invoke Android method,like[JsBridge https://github.com/lzyzsd/JsBridge](https://github.com/lzyzsd/JsBridge).

if js want to invoke Android Method in JsEchoApi, must be use it:
```javascript
            window.WebViewJavascriptBridge.callHandler(
                'submitFromWeb'
                , {'param': '中文测试'}
                , function(responseData) {
                    document.getElementById("show").innerHTML = "send get responseData from java, data = " + responseData
                }
            );
```


- but it can't support the sync method（return method）.



### DsBridge
```java
        Bridge bridge =  new Bridge.Builder()
                .setWebView(dwebView)
                .setClientFactory(new DefaultDsBridgeFactory(dwebView))
                .setConvertFactory(GsonConvertFactory.create(new Gson()))
                .registerInterface("JsTest",new JsTest())
                .registerInterface(null,new JsApi())
                .registerInterface("echo",new JsEchoApi())
                .build();
```

- In js/ts,you can invoke Android method,like[DsBridge https://github.com/wendux/DSBridge-Android](https://github.com/wendux/DSBridge-Android)

if js want to invoke Android Method in JsEchoApi, must be use it:
```javascript
var ret=dsBridge.call("echo.syn",{msg:" I am echoSyn call", tag:1})
alert(JSON.stringify(ret))  

dsBridge.call("echo.asyn",{msg:" I am echoAsyn call",tag:2},function (ret) {
      alert(JSON.stringify(ret));
})
```

- support sync method（return method）.

### React Native bridge
```java
        Bridge bridge =  new Bridge.Builder()
                .setWebView(dwebView)
                .setClientFactory(new DefaultRnFactory(getReactInstanceManager()))
                .setConvertFactory(GsonConvertFactory.create(new Gson()))
                .registerInterface("JsTest",new JsTest())
                .registerInterface(null,new JsApi())
                .registerInterface("echo",new JsEchoApi())
                .build();
```

- In React native, can use like React Native

- support RN method. be careful, if you want to use return method ,you must implement ReactContextBaseJavaModule
```java
public class JsTest extends ReactContextBaseJavaModule
```
In the future version ，we will not worry about this problem

### Flutter Bridge
```java
        final Bridge bridge =  new Bridge.Builder()
                .setClientFactory(new DefaultFlutterBridgeFactory(getFlutterView()))
                .setConvertFactory(GsonConvertFactory.create(new Gson()))
                .registerInterface("Sample",new JsTest())
                .build();
```

