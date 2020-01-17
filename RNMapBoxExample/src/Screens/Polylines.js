import React from 'react';
import {View, Text, StyleSheet, Image, Button} from 'react-native';
import RNMapBox from 'react-native-mapbox';

import bus from '../assets/bus.png';
import train from '../assets/train.png';
import {commonStyles} from './styles';

export default class Polylines extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  componentDidMount() {}

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
                name: 'AA',
                lineWidth: 3.0,
                lineColor: '#000000',
                lineType: 'dash'
              },
              coordinates: [
                [174, -36],
                [173, -36],
              ],
            },
            {
              id: 'poly2',
              properties: {
                name: 'AA',
                lineWidth: 3.0,
                lineColor: '#000000'
              },
              coordinates: [
                [174, -33],
                [173, -32],
              ],
            },
          ]}
        />
        <View style={commonStyles.backButton}>
          <Button title="<Back" onPress={this.props.onGoBack} />
        </View>
      </>
    );
  }
}
