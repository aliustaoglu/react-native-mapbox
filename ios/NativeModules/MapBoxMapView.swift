import Foundation
import UIKit
import Mapbox
import CoreLocation
import UserNotifications


class MapBoxMapView: UIView, MGLMapViewDelegate {
    private var mapView: MGLMapView!
    private var isMapReady = false
    
    private var util = Utils()
    private var props = Props()
    
    @objc var camera: NSDictionary = [:] {
        didSet{
            props.camera = RNMBCamera(camera)
            // Probably no need this
            if (self.isMapReady){
                props.camera?.update(mapView)
            }
        }
    }
    
    @objc var options: NSDictionary = [:] {
        didSet{
            props.options = RNMBOptions(options)
            if (self.isMapReady){
                props.options?.update(self.mapView)
            }
        }
    }
    
    @objc var mapStyle: NSDictionary = [:] {
        didSet{
            props.mapStyle = RNMBMapStyle(mapStyle)
        }
    }
    
    @objc var onMapReady: RCTDirectEventBlock?
    
    init() {
        super.init(frame: UIScreen.main.bounds)
    }
    
    override func didMoveToWindow() {
        initMap()
    }
    
    func initMap(){
        self.mapView = props.mapStyle == nil ?
            MGLMapView(frame: self.bounds) :
            MGLMapView(frame: self.bounds, styleURL: props.mapStyle?.getStyle())
        self.addSubview(mapView!)
        self.isMapReady = true
        self.mapView.delegate = self
        
        props.camera?.update(mapView)
        props.options?.update(mapView)
    }
    
    func mapView(_ mapView: MGLMapView, didFinishLoading style: MGLStyle) {
        if self.onMapReady != nil {
          self.onMapReady!([:])
        }
        props.mapStyle?.updateBuildings(style: style)
    }
    
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
}
