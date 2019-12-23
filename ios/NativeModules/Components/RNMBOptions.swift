//
//  RNMBOptions.swift
//  RNMBReactNativeMapbox
//
//  Created by Cüneyt Aliustaoğlu on 23/12/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation
import Mapbox

struct RNMBOptions {
    var mapView: MGLMapView!
    var showsUserHeadingIndicator: Bool!
    var showsScale: Bool!
    var showsHeading: Bool!
    var showsUserLocation: Bool!
    
    init(_ dict:NSDictionary){
        let options = dict as! Dictionary<String, Bool>
        
        self.showsUserHeadingIndicator = options["showsUserHeadingIndicator"] ?? false
        self.showsScale = options["showsScale"] ?? false
        self.showsHeading = options["showsHeading"] ?? false
        self.showsUserLocation = options["showsUserLocation"] ?? false
    }
    
    public func update(_ mapView: MGLMapView){
        mapView.showsUserHeadingIndicator = self.showsUserHeadingIndicator
        mapView.showsHeading = self.showsHeading
        mapView.showsScale = self.showsScale
        mapView.showsUserLocation = self.showsUserLocation
    }
}
