import React from 'react';
import {View, Text, StyleSheet, Image} from 'react-native';
import RNMapBox from 'react-native-mapbox';
import img from '../../screenshots/screenshot.png'

//const img = require('../../screenshots/screenshot.png')

export default class Markers extends React.Component {
  render() {
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
        markers={[{lat: 40.9175, lng: 38.3927, label: 'Custom', icon: Image.resolveAssetSource(img)}]}
      />
    );
  }
}
