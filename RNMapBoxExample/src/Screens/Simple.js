import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, Image, Button} from 'react-native';
import RNMapBox, { RNMBMarker } from 'react-native-mapbox';
import marker from '../../img/marker.png';

export default class Simple extends Component {
  
  render() {
    return (
      <>
      <RNMapBox
        zoom={7}
        region={{lat: 40.9175, lng: 38.3927}}
        style={StyleSheet.absoluteFillObject}
        mapStyle="DEFAULT"
      >
        <RNMBMarker />
      </RNMapBox>
      <View style={{ position: 'absolute', top: 30, left: 10 }}>
        <Button title="<Back" onPress={this.props.onGoBack} />
      </View>
      </>
    );
  }
}
