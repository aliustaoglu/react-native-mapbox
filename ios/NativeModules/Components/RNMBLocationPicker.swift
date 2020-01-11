//
//  RNMBLocationPicker.swift
//  RNMBReactNativeMapbox
//
//  Created by Cüneyt Aliustaoğlu on 10/01/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation
import Mapbox

struct RNMBLocationPicker{
    var isPickerEnabled = false
    var locationPickerView: UIView?
    
    init(_ pickerEnabled:Bool){
        self.isPickerEnabled = pickerEnabled
        
        
        
    }
    
    public mutating func update(_ mapView: MGLMapView){
        if (self.locationPickerView == nil) {
            mountLocationPickerView(mapView)
        }
        
        if (self.isPickerEnabled) {
            self.locationPickerView?.isHidden = !self.isPickerEnabled
        }
        

    }
    
    private mutating func mountLocationPickerView(_ mapView: MGLMapView){
        let bundle = Bundle(for: MapBoxViewController.self)
        let mapAssets = Bundle(url: bundle.url(forResource: "MapAssets",
                                      withExtension: "bundle")!)!
        
        
        let imgPicker = UIImage.init(named: "MapPin", in: mapAssets, compatibleWith: nil)!
        let locationPickerViewImage = UIImageView(image: imgPicker)
        self.locationPickerView = UIView()
        self.locationPickerView?.addSubview(locationPickerViewImage)
        
        self.locationPickerView?.center = mapView.center
        mapView.addSubview(self.locationPickerView!)
    }
}
