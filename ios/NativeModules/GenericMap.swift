//
//  GenericMap.swift
//  ChoiceGo
//
//  Created by Cuneyt Aliustaoglu on 13/09/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation

class GenericMap: UIView{
    @objc var zoom: Double = 0
    @objc var region: NSDictionary = [:]
    @objc var onMapReady: RCTDirectEventBlock?
    @objc var markers: NSArray = []
    @objc var options: NSDictionary = [:]
    
    var lat: Double = 0
    var lng: Double = 0
    var isMapReady: Bool = false
    
    public var util: Utils?
    
    /// Map must be initialized after View moved to Window
    func initMap(){
        preconditionFailure("Map must be initialized from the main map class and overridden")
    }
    
    func updateRegion(){
        preconditionFailure("updateRegion must be overridden")
    }
    
    func updateZoom(){
        preconditionFailure("updateZoom must be overridden")
    }
    
    func updateMarkers(){
        preconditionFailure("updateMarkers must be overridden")
    }
    
    func updateOptions(){
        preconditionFailure("updateOptions must be overridden")
    }
    
    override func didSetProps(_ changedProps: [String]!) {
        if self.isMapReady == false {
            return
        }
        
        if (changedProps!.contains("region")) {
            updateRegion()
        }
        
        if (changedProps!.contains("zoom")) {
            updateZoom()
        }
        
        if (changedProps!.contains("markers")) {
            updateZoom()
        }
    }
    
    override func didMoveToWindow() {
        lat = region.object(forKey: "lat") as! Double
        lng = region.object(forKey: "lng") as! Double
        
        initMap()
    }
    
}

