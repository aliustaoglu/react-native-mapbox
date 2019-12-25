//
//  RNMBCamera.swift
//  RNMBReactNativeMapbox
//
//  Created by Cüneyt Aliustaoğlu on 23/12/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation
import Mapbox

struct RNMBCamera {
    struct Target {
        var lat: Double?
        var lng: Double?
    }
    
    var target: Target?
    var bearing: Double?
    var zoom: Double?
    var tilt: Double?
    
    init(_ dict:NSDictionary){
        let target = dict["target"] as? Dictionary<String, Double>
        self.target = Target(lat:target?["lat"], lng: target?["lng"])
        self.bearing = dict["bearing"] as? Double
        self.zoom = dict["zoom"] as? Double
        self.tilt = dict["tilt"] as? Double
    }
    
    public func update(_ mapView: MGLMapView){
        let center = CLLocationCoordinate2D(latitude: target!.lat!, longitude: target!.lng!)
        if (zoom != nil) {
            mapView.setCenter(center, zoomLevel: zoom!, animated: true)
        } else {
            mapView.setCenter(center, animated: true)
        }
    }
    
}
