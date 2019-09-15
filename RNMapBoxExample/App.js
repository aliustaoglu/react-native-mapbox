import React, { Component } from 'react'
import { Platform, StyleSheet, Text, View } from 'react-native'
import RNMapBox from 'react-native-mapbox'

export default class App extends Component {
  render () {
    return <RNMapBox zoom={7} region={{ lat: -36, lng: 174 }} style={StyleSheet.absoluteFillObject} mapStyle='DEFAULT' markers={[]} />
  }
}
