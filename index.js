import React from "react";
import { requireNativeComponent, Image, CameraRoll } from "react-native";
import { mergePolylines } from "./lib/polylines";

const mapRef = React.createRef();

const NativeMap = requireNativeComponent("MapBoxViewController", null);

class RNMBReactNativeMapbox extends React.Component {
  constructor(props) {
    super(props);
  }

  mergeProps(props) {
    let newProps = mergePolylines(props);
    return newProps;
  }

  render() {
    const merged = this.mergeProps(this.props);
    return <NativeMap {...merged} />;
  }
}

export default RNMBReactNativeMapbox;
