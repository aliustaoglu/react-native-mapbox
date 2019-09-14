# react-native-mapbox

MapBox wrapper for React Native around Android and IOS SDKs.

## Getting started

`$ npm install react-native-mapbox --save`

### Mostly automatic installation

`$ react-native link react-native-mapbox`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-mapbox` and add `RNMBReactNativeMapbox.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNMBReactNativeMapbox.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import biz.aliustaoglu.mapbox.RNMBReactNativeMapboxPackage;` to the imports at the top of the file
  - Add `new RNMBReactNativeMapboxPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-mapbox'
  	project(':react-native-mapbox').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-mapbox/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-mapbox')
  	```


## Usage
```javascript
import RNMBReactNativeMapbox from 'react-native-mapbox';

// TODO: What to do with the module?
RNMBReactNativeMapbox;
```
