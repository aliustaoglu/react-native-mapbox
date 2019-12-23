import React, {Component} from 'react';
import {StyleSheet, Text, View, Button, TextInput} from 'react-native';
import RNMapBox from 'react-native-mapbox';

const styleSheet = StyleSheet.create({
  bottomView: {
    position: 'absolute',
    width: 100,
    bottom: 30,
    right: 10,
    backgroundColor: '#fff',
  },
  bottomRow: {
    display: 'flex',
    flexDirection: 'row',
  },
  backButton: {
    position: 'absolute',
    top: 30,
    left: 10,
  },
  textInput: {
    backgroundColor: 'lightgray',
    width: 90,
    height: 35,
  },
});

export default class Simple extends Component {
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
        />
        <View style={styleSheet.backButton}>
          <Button title="<Back" onPress={this.props.onGoBack} />
        </View>
        <View style={styleSheet.bottomView}>
          <View style={styleSheet.bottomRow}>
            <Text>Lat: </Text>
            <TextInput
              onChange={e => this.setState({lat: parseFloat(e.nativeEvent.text)})}
              value={this.state.lat.toString()}
              style={styleSheet.textInput}
            />
          </View>
          <View style={styleSheet.bottomRow}>
            <Text>Lng: </Text>
            <TextInput
              onChange={e => this.setState({lng: parseFloat(e.nativeEvent.text)})}
              value={this.state.lng.toString()}
              style={styleSheet.textInput}
            />
          </View>
          <View style={styleSheet.bottomRow}>
            <Text>Zoom: </Text>
            <TextInput
              onChange={e => this.setState({zoom: parseFloat(e.nativeEvent.text)})}
              value={(this.state.zoom || 1).toString()}
              style={styleSheet.textInput}
            />
          </View>
        </View>
      </>
    );
  }
}
