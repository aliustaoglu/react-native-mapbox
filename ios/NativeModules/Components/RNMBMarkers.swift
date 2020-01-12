//
//  RNMBMarkers.swift
//  RNMBReactNativeMapbox
//
//  Created by Cüneyt Aliustaoğlu on 12/01/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation
import Mapbox

struct RNMBMarkers{
    var markers: NSArray = []
    
    init(_ markers: NSArray) {
        self.markers = markers
        
    }
    
    public mutating func update(_ mapView: MGLMapView){
        var pointAnnotations = [MGLAnnotationImage]()
        for m in self.markers {
            let marker = m as! NSDictionary
            let lat = marker.object(forKey: "lat") as! CLLocationDegrees
            let lng = marker.object(forKey: "lng") as! CLLocationDegrees
            
            if let ico = marker.object(forKey: "icon") {
                let icon = ico as! NSDictionary
                
                //let iconWidth = icon.object(forKey: "width") as! Double
                //let iconHeight = icon.object(forKey: "height")  as! Double
                let iconUri = icon.object(forKey: "uri") as! String
                
                let url = URL(string: iconUri)
                let data = try? Data(contentsOf: url!)
                let img = UIImage(data: data!)!
                let anno = MGLAnnotationImage(image: img, reuseIdentifier: "data")
                
                
                let pin = MGLPointAnnotation()
                pin.coordinate = CLLocationCoordinate2D(latitude: lat, longitude: lng)
                
                
                mapView.addAnnotation(pin)
                
                
                
            }
        }
        
    }
    
    
}
