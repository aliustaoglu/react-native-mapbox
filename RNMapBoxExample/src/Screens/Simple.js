import React, {Component} from 'react';
import {StyleSheet, Text, View, Button, TextInput} from 'react-native';
import RNMapBox from 'react-native-mapbox';
import {commonStyles} from './styles';

export default class Simple extends Component {
  constructor(props) {
    super(props);
    this.state = {
      lat: 40.9175,
      lng: 38.3927,
      zoom: 6,
      bearing: 1,
      tilt: 7,
      padding: [0, 0, 0, 0]
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
            padding: this.state.padding
          }}
          style={StyleSheet.absoluteFillObject}
          onMapReady={() => console.log('onMapReady callback')}
        />
        <View style={commonStyles.backButton}>
          <Button title="<Back" onPress={this.props.onGoBack} />
        </View>
        <View style={commonStyles.bottomView}>
          <View style={commonStyles.bottomRow}>
            <Text>Lat: </Text>
            <TextInput
              onChange={e => this.setState({lat: parseFloat(e.nativeEvent.text)})}
              value={this.state.lat.toString()}
              style={commonStyles.textInput}
            />
          </View>
          <View style={commonStyles.bottomRow}>
            <Text>Lng: </Text>
            <TextInput
              onChange={e => this.setState({lng: parseFloat(e.nativeEvent.text)})}
              value={this.state.lng.toString()}
              style={commonStyles.textInput}
            />
          </View>
          <View style={commonStyles.bottomRow}>
            <Text>Zoom: </Text>
            <TextInput
              onChange={e => this.setState({zoom: parseFloat(e.nativeEvent.text)})}
              value={(this.state.zoom || 1).toString()}
              style={commonStyles.textInput}
            />
          </View>
          <View style={commonStyles.bottomRow}>
            <Text>Padding: </Text>
            <TextInput
              onChange={e => this.setState({padding: e.nativeEvent.text.split(',').map( p => parseFloat(p) ) })}
              value={(this.state.padding || 1).toString()}
              style={commonStyles.textInput}
            />
          </View>
        </View>
      </>
    );
  }
}
