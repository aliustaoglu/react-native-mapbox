import React from 'react'
import { requireNativeComponent, Image } from 'react-native';

const mapRef = React.createRef()

const NativeMap = requireNativeComponent('MapBoxViewController', null);

class RNMBReactNativeMapbox extends React.Component{
  constructor(props){
    super(props)
  }


  render(){
    return <NativeMap {...this.props} />
  }
}

export default RNMBReactNativeMapbox;
