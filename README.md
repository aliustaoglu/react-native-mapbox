# react-native-mapbox

MapBox wrapper for React Native around Android and IOS SDKs.

This library is in alpha release and may not be stable for production environments. I am actively developing a project that uses this library. So, there'll be a production ready release soon.

## Getting started

`$ yarn add react-native-mapbox`

### Mostly automatic installation

Library is auto linked by React Native. But MapBox is a dependency and in order to enter our API_KEY, we need to add below library to build.gradle file:

```
implementation 'com.mapbox.mapboxsdk:mapbox-android-sdk:8.2.1'
```

Then in MainApplication.java file need to replace api key with `API_KEY` below.

```java
import com.mapbox.mapboxsdk.Mapbox;
....

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    Mapbox.getInstance(getApplicationContext(), "API_KEY"); // Add this line
  }

```

IOS dependency automatically works from podfile. But, API_KEY needs to be added to info.plist file. Follow instructions from MapBox IOS SDK below:

https://www.mapbox.com/install/ios/cocoapods-permission/

### Manual installation
You should not need manual installation. Only for React Native versions lesser than 0.60

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

<RNMBReactNativeMapbox
        zoom={7}
        region={{ lat: 40.9175, lng: 38.3927 }}
        style={StyleSheet.absoluteFillObject}
        mapStyle='DEFAULT'
        markers={[
          {
            lat: 40.9175,
            lng: 38.3927,
            label: 'Giresun',
            icon: Image.resolveAssetSource(marker)
          }
        ]}
      />
```

## Screenshots
![MapBox](https://raw.githubusercontent.com/aliustaoglu/react-native-mapbox/master/RNMapBoxExample/screenshots/screenshot.png)
