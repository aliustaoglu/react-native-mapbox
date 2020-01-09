import React, {Component} from 'react';
import {StyleSheet, Text, View, Button, TextInput} from 'react-native';
import RNMapBox from 'react-native-mapbox';
import {commonStyles} from './styles';

export default class LocationPicker extends Component {
  constructor(props) {
    super(props);
    this.state = {
      lat: 40.9175,
      lng: 38.3927,
      zoom: 6,
      bearing: 1,
      tilt: 7,
    };
  }

  componentDidMount() {}

  render() {
    return (
      <>
        <RNMapBox
          camera={{
            target: {
              lat: this.state.lat,
              lng: this.state.lng,
            },
            bearing: this.state.bearing,
            zoom: this.state.zoom || 1,
            tilt: this.state.tilt,
          }}
          style={StyleSheet.absoluteFillObject}
          locationPicker={true}
          onMapReady={() => console.log('onMapReady callback')}
          onCameraMoveEnd={e=>console.log(e.nativeEvent)}
          onCameraMove={e=>console.log(e.nativeEvent)}
        />
        <View style={commonStyles.backButton}>
          <Button title="<Back" onPress={this.props.onGoBack} />
        </View>
      </>
    );
  }
}
