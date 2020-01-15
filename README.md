# react-native-mapbox

Unofficial MapBox wrapper for React Native around Android and IOS SDKs. There's no official MapBox library. This one covers some basic functionalities like adding markers etc.

## Getting started

`$ yarn add react-native-mapbox`

React Native now automatically links dependencies so you don't need to do anything else other than adding your mapbox key. Grab your key from https://account.mapbox.com/ and follow instrunctions below:

Then in MainApplication.java file need to replace api key with `API_KEY` below.

```java
import com.mapbox.mapboxsdk.Mapbox;
....

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    Mapbox.getInstance(getApplicationContext(), "YOUR_API_KEY"); // Add this line
  }

```

Add your API key to MGLMapboxAccessToken in info.plist file.

<key>MGLMapboxAccessToken</key>
<string>YOUR_API_KEY</string>

And you're done with installation.


## Usage
```javascript
import {Image,StyleSheet} from 'react-native'
import RNMapBox from 'react-native-mapbox';
import bus from '../images/bus.png'
....

<RNMapBox
  camera={{
    target: {
      lat: -36,
      lng: 174,
    },
    zoom: 6,
  }}
  style={StyleSheet.absoluteFillObject}
  onMapReady={() => console.log('onMapReady callback')}
  markers={[
    {
        id: 'Marker1',
        lat: -36,
        lng: 174,
        title: 'Custom',
        subtitle: 'Sub1',
        icon: Image.resolveAssetSource(bus),
        pulsator: {
          color: '#ff00ff',
          radius: 20,
          duration: 1500
        }
      }
  ]}
/>
```

## Screenshots
![MapBox](https://raw.githubusercontent.com/aliustaoglu/react-native-mapbox/master/RNMapBoxExample/screenshots/screenshot.png)
