import React, {Component} from 'react';
import {Platform, StyleSheet, Text, ScrollView, View, Image, TouchableOpacity} from 'react-native';
import Btn from './src/Btn';
import Simple from './src/Screens/Simple';
import Options from './src/Screens/Options';

export default class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      activeRoute: 'App',
    };
    this.onBtnClick = this.onBtnClick.bind(this);
  }

  onBtnClick(activeRoute) {
    this.setState({activeRoute});
  }

  render() {
    return (
      <>
        {this.state.activeRoute === 'App' && (
          <View style={{marginTop: 50, height: '100%'}}>
            <ScrollView>
              <Btn text="Simple" onClick={() => this.onBtnClick('Simple')} />
              <Btn text="Options" onClick={() => this.onBtnClick('Options')} />
            </ScrollView>
          </View>
        )}
        {this.state.activeRoute === 'Simple' && <Simple onGoBack={() => this.setState({activeRoute: 'App'})} />}
        {this.state.activeRoute === 'Options' && <Options onGoBack={() => this.setState({activeRoute: 'App'})} />}
      </>
    );
  }
}
