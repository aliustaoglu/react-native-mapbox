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
            if (props.locationPicker == nil) {
                props.locationPicker = RNMBLocationPicker(locationPicker)
            }
            print(locationPicker)
            if (self.isMapReady){
                props.locationPicker?.update(self.mapView, locationPicker)
            }
        }
    }
    
    @objc var markers: NSArray = [] {
        didSet{
            props.markers = RNMBMarkers(markers, oldValue)
            if (self.isMapReady){
                props.markers?.update(self.mapView)
            }
        }
    }
    
    @objc var polylines: NSArray = [] {
        didSet{
            props.polylines = RNMBPolylines(polylines, oldValue)
            if (self.isMapReady){
                props.polylines?.update(self.mapView)
            }
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
    }
    
    func mapView(_ mapView: MGLMapView, didFinishLoading style: MGLStyle) {
        if self.onMapReady != nil {
            self.onMapReady!([:])
        }
        props.mapStyle?.updateBuildings(style: style)
        props.polylines?.update(mapView)
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
    
    func setCamera(_ location:NSArray, resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock){
        let coords = location[0] as! NSDictionary
        let longitude = coords.value(forKey: "longitude") as! Double
        let latitude = coords.value(forKey: "latitude") as! Double
        
        self.mapView!.setCenter(CLLocationCoordinate2D(latitude: CLLocationDegrees(latitude), longitude: CLLocationDegrees(longitude)), animated: true)
        let currentCamera = mapView!.camera
        let newCamera = MGLMapCamera(lookingAtCenter: currentCamera.centerCoordinate, acrossDistance: currentCamera.viewingDistance, pitch: currentCamera.pitch, heading: currentCamera.heading)
        self.mapView!.setCamera(newCamera, animated: true)
    }
    
    func setBounds(_ bounds:NSArray, resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock){
        let edgePadding = UIEdgeInsets(top: 220, left: 20, bottom: 100, right: 20)
        let boundSW = bounds[0] as! NSDictionary
        let boundNE = bounds[1] as! NSDictionary
        let bounds = MGLCoordinateBounds(
            sw: CLLocationCoordinate2D(latitude: boundSW.object(forKey: "lat") as! CLLocationDegrees, longitude: boundSW.object(forKey: "lng") as! CLLocationDegrees),
            ne: CLLocationCoordinate2D(latitude: boundNE.object(forKey: "lat") as! CLLocationDegrees, longitude: boundNE.object(forKey: "lng") as! CLLocationDegrees))
        self.mapView.setVisibleCoordinateBounds(bounds, edgePadding: edgePadding, animated: true, completionHandler: {} )
        
    }
    
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
}
