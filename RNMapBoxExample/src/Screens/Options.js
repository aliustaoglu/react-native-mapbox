/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react/no-did-mount-set-state */
import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, PermissionsAndroid, Button, Picker} from 'react-native';
import RNMapBox from 'react-native-mapbox';

const styleSheet = StyleSheet.create({
  backButton: {
    position: 'absolute',
    top: 30,
    left: 10,
  },
  bottomView: {
    position: 'absolute',
    width: 200,
    bottom: 30,
    right: 10,
    backgroundColor: '#fff',
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
      mapStyleName: 'OUTDOORS',
    };
    this.onMapStyleChange = this.onMapStyleChange.bind(this);
  }

  onMapStyleChange(val, ind) {
    this.setState({mapStyleName: val});
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
            zoom: 16,
            tilt: 7,
          }}
          style={StyleSheet.absoluteFillObject}
          options={{
            showsUserHeadingIndicator: this.state.showsUserHeadingIndicator,
            showsScale: this.state.showsScale,
            showsHeading: this.state.showsHeading,
            showsUserLocation: this.state.showsUserLocation,
          }}
          mapStyle={{
            styleName: this.state.mapStyleName,
            buildings: true,
          }}
        />
        <View style={styleSheet.backButton}>
          <Button title="<Back" onPress={this.props.onGoBack} />
        </View>
        <View style={styleSheet.bottomView}>
          <Picker mode="dropdown" onValueChange={this.onMapStyleChange} selectedValue={this.state.mapStyleName}>
            <Picker.Item label="OUTDOORS" value="OUTDOORS" />
            <Picker.Item label="LIGHT" value="LIGHT" />
            <Picker.Item label="DARK" value="DARK" />
            <Picker.Item label="SATELLITE" value="SATELLITE" />
            <Picker.Item label="SATELLITE_STREETS" value="SATELLITE_STREETS" />
            <Picker.Item label="TRAFFIC_DAY" value="TRAFFIC_DAY" />
          </Picker>
        </View>
      </>
    );
  }
}
