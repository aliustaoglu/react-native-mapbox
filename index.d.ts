import React from "react";
import { StyleProp, ViewStyle, ImageResolvedAssetSource } from "react-native";
import { invokeMapFunction } from './index'

type CameraTarget = {
  /**
   * Latitude of camera
   */
  lat: Number;
  /**
   * Longitude of camera
   */
  lng: Number;
};


type Camera = {
  /**
   * Camera target in {lat, lng} value
   */
  target: CameraTarget;
  /**
   * Bearing angle
   */
  bearing: Number;
  /**
   * Zoom value
   */
  zoom: Number;
  /**
   * Tilt value
   */
  tilt: Number;
};

type Options = {
  /**
   * Whether to show user's orientation on map.
   * IMPORTANT: This requires for you to get location permission first
   */
  showsUserHeadingIndicator: Boolean;
  /**
   * Whether to show map scale
   */
  showsScale: Boolean;
  /**
   * Whether to show heading 
   */
  showsHeading: Boolean;
  /**
   * Whether to show user's location on map
   * IMPORTANT: This requires for you to get location permission first
   */
  showsUserLocation: Boolean;
}

type MapStyle = {
  /**
   * Predefined style name or url to custom style
   */
  styleName: "OUTDOORS" | "LIGHT" | "DARK" | "SATELLITE" | "SATELLITE_STREETS" | "TRAFFIC_DAY" | String,
  /**
   * Whether to show buildings on map
   */
  buildings: Boolean
}

type Pulsator = {
  /**
   * Pulsating color in hex
   */
  color: String;

  /**
   * Radius of pulsator
   */
  radius: Number;

  /**
   * Duration for each pulsating effect
   */
  duration: Number;
}

type Marker = {
  /**
   * Unique id for marker
   */
  id: String;

  /**
   * Latitude of the marker
   */
  lat: Number;

  /**
   * Longitude of the marker
   */
  lng: Number;

  /**
   * Title for the marker
   */
  title: String;

  /**
   * Subtitle for the marker
   */
  subtitle: String;

  /**
   * Resolved asset for the image
   * eg. Image.resolveAssetSource(require('foo.png'))
   */
  icon: ImageResolvedAssetSource;

  /**
   * Pass either pulse parameters. Or just pass boolean to use it with default values
   */
  pulsator: Pulsator | Boolean
}

export interface MapBoxProperties {
  /**
   * Callback when map is ready
   */
  onMapReady?(): void;

  /**
   * Camera settings for map
   */
  camera: Camera;

  /**
   * Map options
   */
  options: Options;

  /**
   * Map style options
   */
  mapStyle: MapStyle;
  
  /**
   * Map View Controller style
   */
  style: StyleProp<ViewStyle>;

  markers: Array<Marker>
}

interface MapBoxStatic extends React.ComponentClass<MapBoxProperties> {}

declare var MapBox: MapBoxStatic;

type MapBox = MapBoxStatic;

export const invokeMapFunction = invokeMapFunction;
export default MapBox;
