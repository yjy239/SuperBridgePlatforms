import React from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  Button,
  TouchableHighlight,
  ToastAndroid as Toast,
  NativeModules as module
} from 'react-native';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

const App: () => React$Node = () => {
  const array = [{uri:"www.baidu.com"},{uri:"www.baidu.com"}];
  return (
    <>
      <StatusBar barStyle="dark-content" />
      <SafeAreaView style={styles.container1}>

        <View style={styles.container}>
          <TouchableHighlight style={styles.high}
            onPress={() => module.Sample.readString( "read")}>

            <View style={styles.btn}>
              <Text>{"readString"}</Text>
            </View>
          
          </TouchableHighlight>
            
          <TouchableHighlight style={styles.high}
            onPress={() => module.Sample.promiseTest({ name: "callback" }).then(msg => { 
              Toast.show(msg,Toast.SHORT)
            })}>

            <View style={styles.btn}>
              <Text>{"callback"}</Text>
            </View>
          
          </TouchableHighlight>

           <TouchableHighlight style={styles.high}
            onPress={() => module.Sample.promiseArray(array).then(msg => { 
              Toast.show(msg,Toast.SHORT)
            })}>

            <View style={styles.btn}>
              <Text>{"callbackArray"}</Text>
            </View>
          
          </TouchableHighlight>


          <TouchableHighlight style={styles.high}
            onPress={() => module.Sample.callBackArray(array, (msg) => { 
              Toast.show(msg,Toast.SHORT)
            })}>

            <View style={styles.btn}>
              <Text>{"callbackArray"}</Text>
            </View>
          
          </TouchableHighlight>

          <TouchableHighlight style={styles.high}
            onPress={() => module.Sample.callbackTest({ name: "callback" }, (msg) => { 
              Toast.show(msg,Toast.SHORT)
            })}>

            <View style={styles.btn}>
              <Text>{"callbackTest"}</Text>
            </View>
          
          </TouchableHighlight>          

        </View>          

      </SafeAreaView>
    </>
  );
};

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.yellow,
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.dark,
  },
  highlight: {
    fontWeight: '700',
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
  container: {
    flexDirection: "column",

    width: "auto",
    flexGrow: 1,
    alignItems:"stretch"
  
  },
  container1: {
    flex: 1,
    flexDirection: "column",
  },
  high: {
    marginTop: 20,
    marginStart: 20,
    marginEnd: 20,
  },
  btn: {

    backgroundColor: "#DDDDDD",
    alignSelf: "stretch",
    alignItems: "center",
    justifyContent:"center",
    height: 40
  }
});

export default App;

