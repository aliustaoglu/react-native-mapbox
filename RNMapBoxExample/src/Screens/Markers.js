import React from 'react';
import {View, Text, StyleSheet, Image, Button} from 'react-native';
import RNMapBox from 'react-native-mapbox';

import bus from '../assets/bus.png';
import train from '../assets/train.png';
import { commonStyles } from './styles';

const marker1 = {
  id: 'Marker1',
  lat: -36,
  lng: 174,
  title: 'Custom',
  subtitle: 'Sub1',
  icon: Image.resolveAssetSource(bus),
  pulsator: {
    color: '#00ff00'
  }
}

const marker2 = {
  id: 'Marker2',
  lat: -36,
  lng: 173,
  title: 'Custom2',
  subtitle: 'Sub2',
  icon: Image.resolveAssetSource(train),
  pulsator: {
    color: '#00ff00'
  }
}

export default class Markers extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      markers: [marker1]
    };
  }

  componentDidMount() {
    window.setTimeout(() => {
      this.setState({
        markers: [marker2]
      });
    }, 3000);
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
        markers={this.state.markers}
      />
      <View style={commonStyles.backButton}>
          <Button title="<Back" onPress={this.props.onGoBack} />
        </View>
      </>
    );
  }
}
