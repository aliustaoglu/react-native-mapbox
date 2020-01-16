import React from 'react';
import {View, Text, StyleSheet, Image, Button} from 'react-native';
import RNMapBox from 'react-native-mapbox';

import bus from '../assets/bus.png';
import train from '../assets/train.png';
import { commonStyles } from './styles';

export default class Markers extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      marker1: {
        id: 'Marker1',
        lat: -36,
        lng: 174,
        title: 'Custom',
        subtitle: 'Sub1',
        icon: Image.resolveAssetSource(bus),
        
      },
      marker2: {
        id: 'Marker2',
        lat: -36,
        lng: 173,
        title: 'Custom2',
        subtitle: 'Sub2',
        icon: Image.resolveAssetSource(train),
        pulsator: {
          color: '#00ff00'
        }
      },
    };
  }

  componentDidMount() {
    window.setInterval(() => {
      this.setState({
        marker1: {
          id: 'Marker1',
          lat: this.state.marker1.lat + 0.1,
          lng: this.state.marker1.lng + 0.1,
          title: 'Custom',
          subtitle: 'Sub1',
          icon: Image.resolveAssetSource(bus),
        },
      });
    }, 1000);
  }

  render() {
    // const k = NativeModules.ConvertUtil.getBase64FromImageURL(Image.resolveAssetSource(img))
    // k.then(a=>console.log(a))
    return (
      <>
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
        markers={[this.state.marker1, this.state.marker2]}
      />
      <View style={commonStyles.backButton}>
          <Button title="<Back" onPress={this.props.onGoBack} />
        </View>
      </>
    );
  }
}
