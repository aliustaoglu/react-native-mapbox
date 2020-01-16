import React from 'react';
import {View, Text, StyleSheet, Image, Button} from 'react-native';
import RNMapBox from 'react-native-mapbox';

import bus from '../assets/bus.png';
import train from '../assets/train.png';
import { commonStyles } from './styles';

export default class Polylines extends React.Component {
  constructor(props) {
    super(props);
    this.state = {

    };
  }

  componentDidMount() {

  }

  render() {
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
        polylines={[
          {
            id: 'poly1',
            properties: {
              name: 'AA'
            },
            coordinates: []
          }
        ]}
      />
      <View style={commonStyles.backButton}>
          <Button title="<Back" onPress={this.props.onGoBack} />
        </View>
      </>
    );
  }
}
