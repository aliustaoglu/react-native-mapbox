import React from 'react';
import {View, Text, StyleSheet, Image, NativeModules} from 'react-native';
import RNMapBox from 'react-native-mapbox';
import img from '../assets/marker.png'

//const img = require('../../screenshots/screenshot.png')

export default class Markers extends React.Component {
  render() {
    // const k = NativeModules.ConvertUtil.getBase64FromImageURL(Image.resolveAssetSource(img))
    // k.then(a=>console.log(a))
    return (
      <RNMapBox
        camera={{
          target: {
            lat: 40.9175,
            lng: 38.3927,
          },
          zoom: 6,
        }}
        style={StyleSheet.absoluteFillObject}
        onMapReady={() => console.log('onMapReady callback')}
        markers={[
          {id: 'Marker1', lat: 40.9175, lng: 38.3927, title: 'Custom', subtitle: 'Sub1', icon: Image.resolveAssetSource(img)},
          {id: 'Marker2', lat: 40.9175, lng: 39.3927, title: 'Custom2', subtitle: 'Sub2', icon: Image.resolveAssetSource(img)}
        ]}
      />
    );
  }
}
