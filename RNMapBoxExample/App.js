import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, Image} from 'react-native';
import RNMapBox, { RNMBMarker } from 'react-native-mapbox';
import marker from './img/marker.png';

export default class App extends Component {
  
  render() {
    return (
      <RNMapBox
        zoom={7}
        region={{lat: 40.9175, lng: 38.3927}}
        style={StyleSheet.absoluteFillObject}
        mapStyle="DEFAULT"
        markers={[
          {
            lat: 40.9175,
            lng: 38.3927,
            label: 'Giresun',
            icon: Image.resolveAssetSource(marker),
          },
        ]}
      >
        <RNMBMarker />
      </RNMapBox>
    );
  }
}
