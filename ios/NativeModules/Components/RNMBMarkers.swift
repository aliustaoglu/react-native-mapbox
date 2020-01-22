//
//  RNMBMarkers.swift
//  RNMBReactNativeMapbox
//
//  Created by Cüneyt Aliustaoğlu on 12/01/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation
import Mapbox
import Pulsator

class RNMBPointAnnotation: MGLPointAnnotation {
    var lat: CLLocationDegrees
    var lng: CLLocationDegrees
    var icon: NSDictionary?
    //var annotationImage: MGLAnnotationImage?
    var annotationView: MGLAnnotationView?
    var id: String
    var puls: NSDictionary?
    
    
    init(_ marker: NSDictionary){
        self.lat = marker.object(forKey: "lat") as! CLLocationDegrees
        self.lng = marker.object(forKey: "lng") as! CLLocationDegrees
        self.id = marker.object(forKey: "id") as! String
        self.puls = marker.object(forKey: "pulsator") as? NSDictionary
        
        if let ico = marker.object(forKey: "icon") {
            let icon = ico as? NSDictionary
            self.icon = icon
            let uri = icon?.object(forKey: "uri") as! String
            
            let url = URL(string: uri)
            let data = try? Data(contentsOf: url!)
            let img = UIImage(data: data!)!
            let imgView = UIImageView(image: img)
            if (self.puls != nil){
                let pulsator = Pulsator()
                
                let posX = imgView.frame.size.width/2
                let posY = imgView.frame.size.height/2
                pulsator.position = CGPoint(x: posX, y: posY)
                
                if let pulsColor = puls!.object(forKey: "color") {
                    pulsator.backgroundColor = hexStringToCGColor(hex: pulsColor as! String)
                } else {
                    pulsator.backgroundColor = UIColor(red: 0.1, green: 0.24, blue: 1, alpha: 1).cgColor
                }
                
                if let pulsRadius = puls!.object(forKey: "radius") {
                    pulsator.radius = CGFloat(pulsRadius as! Float)
                }
                
                if let pulsDuration = puls!.object(forKey: "duration") {
                    pulsator.duration = CFTimeInterval(pulsDuration as! Float)
                }
                
                imgView.layer.addSublayer(pulsator)
                
                pulsator.start()
            }
            
            self.annotationView = MGLAnnotationView(reuseIdentifier: self.id)
            self.annotationView?.addSubview(imgView)
            
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
    var oldMarkers: NSArray = []
    var imgAnnotation: MGLAnnotationImage!
    var removedIDs: [String] = []
    
    init(_ markers: NSArray, _ oldValue: NSArray) {
        self.markers = markers
        self.oldMarkers = oldValue
        
        self.oldMarkers.forEach{ oldMarker in
            let oldDic = oldMarker as! NSDictionary
            let id = oldDic.object(forKey: "id") as! String
            let containsId = markers.contains { m in
                let marker = m as! NSDictionary
                let containsId = id == (marker.object(forKey: "id") as! String)
                return containsId
            }
            if !containsId {
                self.removedIDs.append(id)
            }
        }
        
        
    }
    
    public func update(_ mapView: MGLMapView){
        for m in self.markers {
            let marker = m as! NSDictionary
            let id = marker.object(forKey: "id") as! String
            
            
            let existingAnnotation = mapView.annotations?.first{ ann in
                let annotation = ann as! RNMBPointAnnotation
                return annotation.id == id
                } as? RNMBPointAnnotation
            
            if (existingAnnotation == nil) {
                mapView.addAnnotation(RNMBPointAnnotation(marker))
            } else {
                let pin = RNMBPointAnnotation(marker)
                existingAnnotation!.coordinate = pin.coordinate
            }
        }
        
        // If removed from props, remove from map as well
        removeMarkers(mapView)
    }
    
    private func removeMarkers(_ mapView: MGLMapView){
        self.removedIDs.forEach{ id in
            let annotationToDelete = mapView.annotations?.first{ann in
                let annotation = ann as! RNMBPointAnnotation
                return annotation.id == id
            }
            mapView.removeAnnotation(annotationToDelete!)
        }
        self.removedIDs = []
    }
}

func hexStringToCGColor (hex:String) -> CGColor {
    return hexStringToUIColor(hex: hex).cgColor
}

func hexStringToUIColor (hex:String) -> UIColor {
    var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
    
    if (cString.hasPrefix("#")) {
        cString.remove(at: cString.startIndex)
    }
    
    if ((cString.count) != 6) {
        return UIColor.gray
    }
    
    var rgbValue:UInt64 = 0
    Scanner(string: cString).scanHexInt64(&rgbValue)
    
    return UIColor(
        red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
        green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
        blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
        alpha: CGFloat(1.0)
    )
}
