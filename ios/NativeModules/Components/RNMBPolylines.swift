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
    let polylines: NSArray!
    var mapView: MGLMapView!
    
    init(_ polylines: NSArray) {
        self.polylines = polylines
    }
    
    public func update(_ mapView: MGLMapView){
        self.mapView = mapView
        guard let style = self.mapView.style else { return }
        
        self.polylines.forEach{ p in
            let polyline = p as! NSDictionary
            let properties = polyline.object(forKey: "properties") as! NSDictionary
            let id = properties.object(forKey: "id") as! String
            let jsonData = try? JSONSerialization.data(withJSONObject: polyline, options: [])
            guard let shapeFromGeoJSON = try? MGLShape(data: jsonData!, encoding: String.Encoding.utf8.rawValue) else {
                fatalError("Could not generate MGLShape")
            }
            let source = MGLShapeSource(identifier: "shape\(id)", shape: shapeFromGeoJSON, options: nil)
            style.addSource(source)
            addLayer(id, polyline, properties, source)
        }
    }
    
    private func addLayer(_ id: String, _ dict: NSDictionary, _ properties: NSDictionary, _ source: MGLShapeSource){
        let style = self.mapView.style
        let layer = MGLLineStyleLayer(identifier: "polyline\(id)", source: source)
        let color = properties.object(forKey: "lineColor") as? String ?? "#336cca"
        let width = properties.object(forKey: "lineWidth") as? Float ?? 1.0
        if let pattern = properties.object(forKey: "lineType") {
            if (pattern as! String) == "dash" {
                layer.lineDashPattern = NSExpression(forConstantValue: [1, 1.5])
            }
        }
        
        layer.lineWidth = NSExpression(forConstantValue: width)
        layer.lineColor = NSExpression(forConstantValue: hexStringToUIColor(hex: color) )

        style?.addLayer(layer)
        
    }
    
    
}
