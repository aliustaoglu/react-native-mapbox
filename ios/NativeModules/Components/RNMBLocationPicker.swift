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
    var locationPickerViewImage: UIImageView!
    var mapPadding: NSArray?
    var markerPadding: NSArray?
    
    init(_ pickerEnabled:Bool){
        self.isPickerEnabled = pickerEnabled
    }
    
    public mutating func update(_ mapView: MGLMapView, _ locationPicker:NSDictionary){
        self.isPickerEnabled = locationPicker.object(forKey: "pickerEnabled") as! Bool
        self.mapPadding = locationPicker.object(forKey: "mapPadding") as? NSArray
        self.markerPadding = locationPicker.object(forKey: "markerPadding") as? NSArray
        if (self.locationPickerView == nil) {
            mountLocationPickerView(mapView)
        }
        setCenter(mapView)
        self.locationPickerView?.isHidden = !self.isPickerEnabled
    }
    
    public mutating func update(_ mapView: MGLMapView){
        if (self.locationPickerView == nil) {
            mountLocationPickerView(mapView)
        }
        setCenter(mapView)
        self.locationPickerView?.isHidden = !self.isPickerEnabled
    }
    
    private mutating func mountLocationPickerView(_ mapView: MGLMapView){
        let bundle = Bundle(for: MapBoxViewController.self)
        let mapAssets = Bundle(url: bundle.url(forResource: "MapAssets",
                                      withExtension: "bundle")!)!
        
        
        let imgPicker = UIImage.init(named: "MapPin", in: mapAssets, compatibleWith: nil)!
        self.locationPickerViewImage = UIImageView(image: imgPicker)
        self.locationPickerView = UIView()
        self.locationPickerView?.addSubview(self.locationPickerViewImage)
        
        setCenter(mapView)
        mapView.addSubview(self.locationPickerView!)
        self.locationPickerView?.isHidden = !self.isPickerEnabled
    }
    
    private func setCenter(_ mapView: MGLMapView){
        var newCenter = mapView.center
        if (mapPadding != nil) {
            newCenter = CGPoint(x: mapView.center.x, y: mapView.center.y - ((mapPadding![3] as! CGFloat) - (mapPadding![1] as! CGFloat)) / 2)
            let screenVerticalPadding = ((mapPadding![3] as! CGFloat) - (mapPadding![1] as! CGFloat)) / 2
            let screenHorizontalPadding = ((mapPadding![0] as! CGFloat) - (mapPadding![2] as! CGFloat)) / 2
            let imageVerticalPadding = self.locationPickerViewImage.frame.height / 2
            let imageHorizontalPadding = self.locationPickerViewImage.frame.width / 2
               
            newCenter = CGPoint(x: mapView.center.x - screenHorizontalPadding - imageHorizontalPadding, y: mapView.center.y - screenVerticalPadding - imageVerticalPadding)
        }
        if (markerPadding != nil){
            let xPad = markerPadding!.object(at: 0) as! CGFloat
            let yPad = markerPadding!.object(at: 1) as! CGFloat
            newCenter = CGPoint(x: newCenter.x + xPad, y: newCenter.y + yPad)
        }
        
        self.locationPickerView?.center = newCenter
    }
}
