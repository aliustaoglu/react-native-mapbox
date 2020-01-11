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
    var styleName = "DEFAULT"
    var buildings = false
    
    init(_ dict:NSDictionary){
        self.styleName = dict["styleName"] as! String
        self.buildings = dict["buildings"] as? Bool ?? false
    }
    
    public func update(_ mapView: MGLMapView){
        mapView.styleURL = getStyle()
    }
    
    public func updateBuildings(style:MGLStyle){
        if (!buildings) {
            return
        }
        if let source = style.source(withIdentifier: "composite") {
            let layer = MGLFillExtrusionStyleLayer(identifier: "buildings", source: source)
            layer.sourceLayerIdentifier = "building"
            
            // Filter out buildings that should not extrude.
            layer.predicate = NSPredicate(format: "extrude == 'true'")
            
            // Set the fill extrusion height to the value for the building height attribute.
            layer.fillExtrusionHeight = NSExpression(forKeyPath: "height")
            layer.fillExtrusionOpacity = NSExpression(forConstantValue: 0.75)
            layer.fillExtrusionColor = NSExpression(forConstantValue: UIColor.white)
            
            // Insert the fill extrusion layer below a POI label layer. If you aren’t sure what the layer is called, you can view the style in Mapbox Studio or iterate over the style’s layers property, printing out each layer’s identifier.
            if let symbolLayer = style.layer(withIdentifier: "poi-scalerank3") {
                style.insertLayer(layer, below: symbolLayer)
            } else {
                style.addLayer(layer)
            }
        }
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
