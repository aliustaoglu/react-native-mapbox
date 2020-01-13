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
        lat: 40.9175,
        lng: 38.3927,
        title: 'Custom',
        subtitle: 'Sub1',
        icon: Image.resolveAssetSource(bus),
      },
      marker2: {
        id: 'Marker2',
        lat: 40.9175,
        lng: 39.3927,
        title: 'Custom2',
        subtitle: 'Sub2',
        icon: Image.resolveAssetSource(train),
      },
    };
  }

  componentDidMount() {
    window.setInterval(() => {
      this.setState({
        marker1: {
          id: 'Marker1',
          lat: this.state.marker1.lat + 1,
          lng: this.state.marker1.lng + 1,
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
            lat: 40.9175,
            lng: 38.3927,
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
