import React from 'react';
import {View, Text, StyleSheet, Image, Button} from 'react-native';
import RNMapBox from 'react-native-mapbox';
import {commonStyles} from './styles';

const poly1 = {
  id: 'poly1',
  properties: {
    name: 'AA',
    lineWidth: 3.0,
    lineColor: '#006b8f',
    lineType: 'dash',
  },
  coordinates: [
    [174, -36],
    [173, -36],
  ],
};

const poly1a = {
  id: 'poly1',
  properties: {
    name: 'AA',
    lineWidth: 3.0,
    lineColor: '#006b8f',
    lineType: 'dash',
  },
  coordinates: [
    [174, -36],
    [173, -36],
    [172, -36],
  ],
};

const poly2 = {
  id: 'poly2',
  properties: {
    name: 'AA',
    lineWidth: 3.0,
    lineColor: '#d21430',
  },
  coordinates: [
    [173.81167, -35.36706],
    [174.55874, -36.52328],
    [175.61343, -38.75019],
    [175.52553, -40.61016],
  ],
};

export default class Polylines extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      polylines: [poly1],
    };
  }

  componentDidMount() {
    const that = this;
    setTimeout(() => {
      that.setState({
        polylines: [poly2],
      });
    }, 2000);
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
          polylines={this.state.polylines}
        />
        <View style={commonStyles.backButton}>
          <Button title="<Back" onPress={this.props.onGoBack} />
        </View>
      </>
    );
  }
}
