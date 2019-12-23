/* eslint-disable react/no-did-mount-set-state */
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, PermissionsAndroid, Button} from 'react-native';
import RNMapBox from 'react-native-mapbox';

const styleSheet = StyleSheet.create({
  backButton: {
    position: 'absolute',
    top: 30,
    left: 10,
  },
});

export default class Simple extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showsUserHeadingIndicator: false,
      showsScale: false,
      showsHeading: false,
      showsUserLocation: false,
    };
  }

  async componentDidMount() {
    if (Platform.OS === 'android') {
      PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION)
        .then(a => {
          this.setState({
            showsUserHeadingIndicator: true,
            showsScale: true,
            showsHeading: true,
            showsUserLocation: true,
          });
        })
        .catch(a => alert('You need to give necessary permissions to use these options.'));
    } else {
      this.setState({
        showsUserHeadingIndicator: true,
        showsScale: true,
        showsHeading: true,
        showsUserLocation: true,
      });
    }
  }

  render() {
    return (
      <>
        <RNMapBox
          camera={{
            target: {lat: 40.9175, lng: 38.3927},
            bearing: 1,
            zoom: 6,
            tilt: 7,
          }}
          style={StyleSheet.absoluteFillObject}
          options={{
            showsUserHeadingIndicator: this.state.showsUserHeadingIndicator,
            showsScale: this.state.showsScale,
            showsHeading: this.state.showsHeading,
            showsUserLocation: this.state.showsUserLocation,
          }}
          mapStyle="DEFAULT"
        />
        <View style={styleSheet.backButton}>
          <Button title="<Back" onPress={this.props.onGoBack} />
        </View>
      </>
    );
  }
}
