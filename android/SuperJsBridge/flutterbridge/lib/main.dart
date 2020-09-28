import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());



class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Bridge Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or press Run > Flutter Hot Reload in a Flutter IDE). Notice that the
        // counter didn't reset back to zero; the application is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Bridge Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class User{
  String name;
  int age;
}

class _MyHomePageState extends State<MyHomePage> {
  String response = "";
  int index = 0;

  static const sample =
  const MethodChannel("Sample");



  static const native = const MethodChannel("flutter");
  static const def = const MethodChannel("default");

  _MyHomePageState():super(){
    native.setMethodCallHandler(platformCallHandler);
  }

  void router1() {
    index++;
    setState(() {
      response = index.toString();
    });
  }

  void readString()  {
    sample.invokeMethod("readString","test")
    .then((value) => {
      setState(() {
        if(value == null){
          response = "is null";
        }else{
          response = "ok";
        }
      })
    });

  }

  void callbackTest(){
    User user = User();
    user.name = "Tome";
    user.age = 11;
    var array = ["{ \"uri\": \"www.baidu.com\" }", "{ \"uri\": \"www.baidu.com\" }"];
    sample.invokeMethod("callbackTest","{\"name\": \"callback\",\"uri\": [{\"uri\": \"www.baidu.com \"}, {\"uri\": \"www.baidu.com\"}],\"love\": {\"interest\": \"aaa\"}}")
        .then((value) => {
      setState(() {
        response = value;
      })
    });
  }

  void callBackArray()  {
    var arr1 = ["{ \"uri\": \"www.baidu.com\" }", "{ \"uri\": \"www.baidu.com\" }"];
    sample.invokeMethod("callBackArray",arr1)
        .then((value) => {
      setState(() {
        response = value==null?"null":value;
      })
    });

  }

  void showToast(String value)  {
    sample.invokeMethod("showToast",value);
  }

  void calHandlerTest()  {
    def.invokeMethod("calHandlerTest","test").then((value) => {
      setState(()=>{
        if(response != null){
          response = value
        }

      })
    });;
  }

  void registerTest()  {
    def.invokeMethod("registerTest","registerTest")
    .then((value) => {
      setState(()=>{
        if(response != null){
          response = value
        }

      })
    });
  }

  Widget routerButton(String name,Function press){
    return Container(
      padding: EdgeInsets.all(10),
      child: Center(
        child:CupertinoButton(
          child: Text(name),
          onPressed: press,
          color: Color.fromARGB(255, 0, 0, 255),
        ) ,
      ),
    );
  }

  Widget showArea(String name){
    return Container(
      decoration: BoxDecoration(
          color: Colors.white,
          shape: BoxShape.rectangle,
          borderRadius:BorderRadius.all(
              Radius.circular(4.0)
          ),
          border: Border.all(
              color: Colors.grey,
              width: 1
          )

      ),
      padding: EdgeInsets.all(20),
      height: 200,
      child: Text(name),
    );
  }

  Future<dynamic> platformCallHandler(MethodCall call) async {
    switch (call.method) {
      case "callFlutter":
        return "${call.arguments} success";
        break;
    }
  }

  @override
  Widget build(BuildContext context) {

    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),

      body:SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          mainAxisSize: MainAxisSize.min,

          // Column is also a layout widget. It takes a list of children and
          // arranges them vertically. By default, it sizes itself to fit its
          // children horizontally, and tries to be as tall as its parent.
          //
          // Invoke "debug painting" (press "p" in the console, choose the
          // "Toggle Debug Paint" action from the Flutter Inspector in Android
          // Studio, or the "Toggle Debug Paint" command in Visual Studio Code)
          // to see the wireframe for each widget.
          //
          // Column has various properties to control how it sizes itself and
          // how it positions its children. Here we use mainAxisAlignment to
          // center the children vertically; the main axis here is the vertical
          // axis because Columns are vertical (the cross axis would be
          // horizontal).
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Container(
              padding: EdgeInsets.all(20),
              child:showArea(response),
            ),
            routerButton("readString", readString),
            routerButton("callbackArray", callBackArray),
            routerButton("callbackTest", callbackTest),
            routerButton("registerTest", registerTest),
            routerButton("calHandlerTest", calHandlerTest),

          ],
        ),
      ),
    );
  }
}
