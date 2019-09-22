import React from 'react';
import { StyleProp, ViewStyle } from 'react-native'

type MarkersType = {
  /**
   * Latitude of the marker
   */
  lat: Number;
  /**
   * Longitude of the marker
   */
  lng: Number;
  /**
   * Label of marker
   */
  label: String;
};

type RegionType = {
  /**
   * Latitude of map object
   */
  lat: Number,
  /**
   * Longitude of map object
   */
  lng: Number
}


export interface MapBoxProperties {
  /**
   * Callback when map is ready
   */
  onMapReady?(): void;
  /**
   * Markers array
   */
  markers: Array<MarkersType>;
  /**
   * Zoom level of map
   */
  zoom: Number;
  /**
   * Map center in latLng
   */
  region: RegionType;
  /**
   * Map View style
   */
  style: StyleProp<ViewStyle>
}

interface MapBoxStatic extends React.ComponentClass<MapBoxProperties> {}

declare var MapBox: MapBoxStatic;

type MapBox = MapBoxStatic;

export default MapBox;