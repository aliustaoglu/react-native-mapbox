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
    
    // =========================================
    // PROPS start
    
    // Property Props Start
    @objc var camera: NSDictionary = [:] {
        didSet{
            props.camera = RNMBCamera(camera)
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
            if (self.isMapReady){
                props.mapStyle?.update(self.mapView)
            }
        }
    }
    
    @objc var locationPicker: Bool = false {
        didSet{
            props.locationPicker = RNMBLocationPicker(locationPicker)
            if (self.isMapReady){
                props.options?.update(self.mapView)
            }
        }
    }
    
    @objc var markers: NSArray = [] {
        didSet{
            props.markers = RNMBMarkers(markers)
            if (self.isMapReady){
                props.markers?.update(self.mapView)
            }
        }
    }
    
    @objc var polylines: NSDictionary = [:] {
        didSet{
            props.polylines = RNMBPolylines(polylines)
        }
    }
    // Property Props End
    
    // Event Props Start
    @objc var onMapReady: RCTDirectEventBlock?
    @objc var onCameraMove: RCTDirectEventBlock?
    @objc var onCameraMoveEnd: RCTDirectEventBlock?
    // Event Props End
    
    // PROPS End
    // =========================================
    
    
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
        props.locationPicker?.update(mapView)
        props.markers?.update(mapView)
        props.polylines?.update(mapView)
    }
    
    func mapView(_ mapView: MGLMapView, didFinishLoading style: MGLStyle) {
        if self.onMapReady != nil {
            self.onMapReady!([:])
        }
        props.mapStyle?.updateBuildings(style: style)
    }
    
    func mapViewRegionIsChanging(_ mapView: MGLMapView) {
        if (self.onCameraMove == nil) {
            return
        }
        self.onCameraMove!(["lat": mapView.latitude, "lng": mapView.longitude])
    }
    
    func mapView(_ mapView: MGLMapView, regionDidChangeAnimated animated: Bool) {
        if (self.onCameraMoveEnd == nil) {
            return
        }
        self.onCameraMoveEnd!(["lat": mapView.latitude, "lng": mapView.longitude])
    }
    
    
    func mapView(_ mapView: MGLMapView, viewFor annotation: MGLAnnotation) -> MGLAnnotationView? {
        if let rnmbAnnotation = annotation as? RNMBPointAnnotation {
            return rnmbAnnotation.annotationView
        } else {
            return nil
        }
    }
    
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
}
