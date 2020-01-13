//
//  RNMBMarkers.swift
//  RNMBReactNativeMapbox
//
//  Created by Cüneyt Aliustaoğlu on 12/01/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation
import Mapbox

class RNMBPointAnnotation: MGLPointAnnotation {
    var lat: CLLocationDegrees
    var lng: CLLocationDegrees
    var icon: NSDictionary?
    var annotationImage: MGLAnnotationImage?
    
    init(_ marker: NSDictionary){
        self.lat = marker.object(forKey: "lat") as! CLLocationDegrees
        self.lng = marker.object(forKey: "lng") as! CLLocationDegrees
        
        if let ico = marker.object(forKey: "icon") {
            let icon = ico as? NSDictionary
            self.icon = icon
            let uri = icon?.object(forKey: "uri") as! String
                        
            let url = URL(string: uri)
            let data = try? Data(contentsOf: url!)
            let img = UIImage(data: data!)!
            self.annotationImage = MGLAnnotationImage(image: img, reuseIdentifier: "data")
        }
        
        super.init()
        
        self.title = marker.object(forKey: "title") as? String
        self.subtitle = marker.object(forKey: "subtitle") as? String
        self.coordinate = CLLocationCoordinate2D(latitude: self.lat, longitude: self.lng)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

class RNMBMarkers{
    var markers: NSArray = []
    var imgAnnotation: MGLAnnotationImage!
    
    init(_ markers: NSArray) {
        self.markers = markers
        
    }
    
    public func update(_ mapView: MGLMapView){
        for m in self.markers {
            let marker = m as! NSDictionary
            let pin = RNMBPointAnnotation(marker)
            mapView.addAnnotation(pin)
        }
        
    }
    
    
}
