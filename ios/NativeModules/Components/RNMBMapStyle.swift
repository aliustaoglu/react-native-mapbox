//
//  RNMBMapStyle.swift
//  RNMBReactNativeMapbox
//
//  Created by Cüneyt Aliustaoğlu on 25/12/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation
import Mapbox

struct RNMBMapStyle{
    var styleName: String
    
    public func update(_ mapView: MGLMapView){
        
    }
    
    public func getStyle() -> URL {
        switch styleName {
        case "DARK":
            return MGLStyle.darkStyleURL
        case "LIGHT":
            return MGLStyle.lightStyleURL
        case "OUTDOORS":
            return MGLStyle.outdoorsStyleURL
        case "SATELLITE":
            return MGLStyle.satelliteStyleURL
        case "SATELLITE_STREETS":
            return MGLStyle.satelliteStreetsStyleURL
        default:
            return MGLStyle.streetsStyleURL
        }
        
    }
}
