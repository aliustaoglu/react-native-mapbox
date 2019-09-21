import React, { Component } from 'react'
import { Platform, StyleSheet, Text, View, Image } from 'react-native'
import RNMapBox from 'react-native-mapbox'
import marker from './img/marker.png'

export default class App extends Component {
  render () {
    return <RNMapBox zoom={7} region={{ lat: -36, lng: 174 }} style={StyleSheet.absoluteFillObject} mapStyle='DEFAULT' markers={[
      {
        lat: -36,
        lng: 174,
        label: 'deneme',
        icon: Image.resolveAssetSource(marker)
      }
    ]} />
  }
}
