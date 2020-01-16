//
//  RNMBPolylines.swift
//  RNMBReactNativeMapbox
//
//  Created by Cuneyt Aliustaoglu on 16/01/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation
import Mapbox

class RNMBPolylines {
    let polylines: NSDictionary!
    var mapView: MGLMapView!
    
    init(_ polylines: NSDictionary) {
        self.polylines = polylines
    }
    
    public func update(_ mapView: MGLMapView){
        self.mapView = mapView
    }
    
    func drawPolyline(geoJson: Data, idx: Int) {
    }
       
}
