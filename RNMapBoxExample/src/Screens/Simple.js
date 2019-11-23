import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, Image, Button} from 'react-native';
import RNMapBox from 'react-native-mapbox';
import marker from '../../img/marker.png';

export default class Simple extends Component {
  
  render() {
    return (
      <>
      <RNMapBox
        zoom={7}
        markers={[
          {lat: 40.9175, lng: 38.3927, title: 'Giresun', subTitle: 'Giresun City'}
        ]}
        region={{lat: 40.9175, lng: 38.3927}}
        style={StyleSheet.absoluteFillObject}
        options={{
          showsUserHeadingIndicator: true,
          showsScale: true,
          showsHeading: true,
          showsUserLocation: true
        }}
        mapStyle="DEFAULT"
      >
      </RNMapBox>
      <View style={{ position: 'absolute', top: 30, left: 10 }}>
        <Button title="<Back" onPress={this.props.onGoBack} />
      </View>
      </>
    );
  }
}
