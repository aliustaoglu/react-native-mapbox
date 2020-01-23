import React from 'react';
import {View, Text, StyleSheet, Image, Button} from 'react-native';
import RNMapBox from 'react-native-mapbox';

import bus from '../assets/bus.png';
import train from '../assets/train.png';
import {commonStyles} from './styles';

const marker1 = {
  id: 'Marker1',
  lat: -36.8485,
  lng: 174.7633,
  title: 'Custom',
  subtitle: 'Sub1',
  icon: Image.resolveAssetSource(bus),
  
};

const marker2 = {
  id: 'Marker2',
  lat: -37.787,
  lng: 175.2793,
  title: 'Custom2',
  subtitle: 'Sub2',
  icon: Image.resolveAssetSource(train),
  pulsator: {
    color: '#3089d3',

  },
};

export default class Markers extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      markers: [marker1],
    };
    this.updateMarkers = this.updateMarkers.bind(this);
  }

  updateMarkers() {
    window.setInterval(() => {
      const marker = this.state.markers[0];

      this.setState({
        markers: [
          {
            id: 'Marker2',
            lat: marker.lat + 0.01,
            lng: marker.lng + 0.01,
            title: 'Custom',
            subtitle: 'Sub1',
            icon: Image.resolveAssetSource(train),
            pulsator: {
              color: '#3089d3',
            },
          },
        ],
      });
    }, 1000);
  }

  componentDidMount() {
    window.setTimeout(() => {
      this.setState({
        markers: [marker2],
      });
      this.updateMarkers()
    }, 3000);
  }

  render() {
    return (
      <>
        <RNMapBox
          camera={{
            target: {
              lat: -37.787,
              lng: 175.2793,
            },
            zoom: 7,
          }}
          style={StyleSheet.absoluteFillObject}
          onMapReady={() => console.log('onMapReady callback')}
          markers={this.state.markers}
        />
        <View style={commonStyles.backButton}>
          <Button title="<Back" onPress={this.props.onGoBack} />
        </View>
      </>
    );
  }
}
