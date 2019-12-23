import Foundation
import UIKit
import Mapbox
import CoreLocation
import UserNotifications

struct RNMBCamera {
    struct Target {
        var lat: Double?
        var lng: Double?
    }
    
    var target: Target?
    var bearing: Double?
    var zoom: Double?
    var tilt: Double?
    
    init(_ dict:NSDictionary){
        let target = dict["target"] as? Dictionary<String, Double>
        self.target = Target(lat:target?["lng"], lng: target?["lng"])
        self.bearing = dict["bearing"] as? Double
        self.zoom = dict["zoom"] as? Double
        self.tilt = dict["tilt"] as? Double
    }
    
}

struct Props {
    var camera: RNMBCamera?
}

class MapBoxMapView: UIView {
    private var mapView: MGLMapView?
    private var util = Utils()
    private var props = Props()
    
    
    @objc var camera: NSDictionary = [:] {
        didSet{
            props.camera = RNMBCamera(camera)
        }
    }
    
    init() {
        super.init(frame: UIScreen.main.bounds)
    }
    
    func initMap(){
        self.mapView = MGLMapView(frame: self.bounds)
        self.addSubview(mapView!)
        
        updateCamera()
    }
    
    override func didMoveToWindow() {
        initMap()
    }
    
    func updateCamera(){
        let center = CLLocationCoordinate2D(latitude: props.camera!.target!.lat!, longitude: props.camera!.target!.lng!)
        if let zoom = props.camera?.zoom {
            mapView?.setCenter(center, zoomLevel: zoom, animated: true)
        } else {
            mapView?.setCenter(center, animated: true)
        }
        
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
}
