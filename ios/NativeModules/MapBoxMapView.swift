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
    
    @objc var locationPicker: NSDictionary = [:] {
        didSet{
            if (props.locationPicker == nil) {
                props.locationPicker = RNMBLocationPicker(false)
            }
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
    
    func setCamera(_ location:NSArray){
        let coords = location[0] as! NSDictionary
        let longitude = coords.value(forKey: "longitude") as! Double
        let latitude = coords.value(forKey: "latitude") as! Double
        let duration = ((coords.value(forKey: "duration") as? Double) ?? 500) / 1000 // Android uses millisecs. So for consistency convert to msec
        //let padding = (coords.value(forKey: "padding") as? NSArray) ?? [0, 0, 0, 0]
        var paddingLeft = 0.0, paddingTop = 0.0, paddingRight = 0.0, paddingBottom = 0.0
        if let padding = coords.value(forKey: "padding") as? NSArray {
            paddingLeft = padding[0] as! Double
            paddingTop = padding[1] as! Double
            paddingRight = padding[2] as! Double
            paddingBottom = padding[3] as! Double
        }
        
        DispatchQueue.main.async {
            let onAfterInset = { ()->Void in
                let currentCamera = self.mapView!.camera
                let centerCoordinate = CLLocationCoordinate2D(latitude: CLLocationDegrees(latitude), longitude: CLLocationDegrees(longitude))
                let newCamera = MGLMapCamera(lookingAtCenter: centerCoordinate, acrossDistance: currentCamera.viewingDistance, pitch: currentCamera.pitch, heading: currentCamera.heading)
                let animationTimingFunction = CAMediaTimingFunction(name: CAMediaTimingFunctionName.easeInEaseOut)
                self.mapView!.setCamera(newCamera, withDuration: duration, animationTimingFunction: animationTimingFunction)
                let dict = [
                    "lat": newCamera.centerCoordinate.latitude,
                    "lng": newCamera.centerCoordinate.longitude
                ]
                JSEmitter.eventEmitter.sendEvent(withName: "onSetCamera", body: dict)
            }
            
            let insets = UIEdgeInsets(top: CGFloat(paddingTop), left: CGFloat(paddingLeft), bottom: CGFloat(paddingBottom), right: CGFloat(paddingRight))
            self.mapView!.setContentInset(insets, animated: true, completionHandler: onAfterInset)
        }
    }
    
    func setBounds(_ mapBounds:NSArray){
        if self.mapView == nil {
            return
        }
        let boundParams = mapBounds.object(at: 0) as! NSDictionary
        
        var edgePadding = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        let boundSW = boundParams.object(forKey: "start") as! NSDictionary
        let boundNE = boundParams.object(forKey: "end") as! NSDictionary
        // Optional extra paddings
        if let padding = boundParams.object(forKey: "padding") as? NSDictionary {
            let left = padding.object(forKey: "paddingLeft") ?? 0
            let top = padding.object(forKey: "paddingTop") ?? 0
            let right = padding.object(forKey: "paddingRight") ?? 0
            let bottom = padding.object(forKey: "paddingBottom") ?? 0
            edgePadding = UIEdgeInsets(top: top as! CGFloat, left: left as! CGFloat, bottom: bottom as! CGFloat, right: right as! CGFloat)
            print (padding)
        }
        
        
        
        let bounds = MGLCoordinateBounds(
            sw: CLLocationCoordinate2D(latitude: boundSW.object(forKey: "lat") as! CLLocationDegrees, longitude: boundSW.object(forKey: "lng") as! CLLocationDegrees),
            ne: CLLocationCoordinate2D(latitude: boundNE.object(forKey: "lat") as! CLLocationDegrees, longitude: boundNE.object(forKey: "lng") as! CLLocationDegrees))
        
        let dict = [
            "swLat": bounds.sw.latitude,
            "swLng": bounds.sw.longitude,
            "neLat": bounds.ne.latitude,
            "neLng": bounds.ne.longitude
        ]
        
        let onSetBounds = {
            JSEmitter.eventEmitter.sendEvent(withName: "onSetBounds", body: dict)
        }
        
        self.mapView.setVisibleCoordinateBounds(bounds, edgePadding: edgePadding, animated: true, completionHandler: onSetBounds )
        
    }
    
    func setPadding(_ padding:NSArray){
        if self.mapView == nil {
            return
        }

        let paddingLeft = padding[0] as! Double
        let paddingTop = padding[1] as! Double
        let paddingRight = padding[2] as! Double
        let paddingBottom = padding[3] as! Double
        
        DispatchQueue.main.async {
            let insets = UIEdgeInsets(top: CGFloat(paddingTop), left: CGFloat(paddingLeft), bottom: CGFloat(paddingBottom), right: CGFloat(paddingRight))
            self.mapView!.setContentInset(insets, animated: true)
            let dict = [
                "left": insets.left,
                "top": insets.top,
                "right": insets.right,
                "bottom": insets.bottom,
            ]
            JSEmitter.eventEmitter.sendEvent(withName: "onSetPadding", body: dict)
        }
    }
    
    func getCameraPosition(_ params:NSArray){
        if (self.mapView == nil){
            return
        }
        let dict = [
            "latitude": self.mapView.centerCoordinate.latitude,
            "longitude": self.mapView.centerCoordinate.longitude
        ]
        
        JSEmitter.eventEmitter.sendEvent(withName: "onGetCameraPosition", body: dict)
    }
    
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
}
