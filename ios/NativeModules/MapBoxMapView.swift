import Foundation
import UIKit
import Mapbox
import CoreLocation
import UserNotifications


class MapBoxMapView: UIView {
    private var mapView: MGLMapView!
    private var isMapReady = false
    
    private var util = Utils()
    private var props = Props()
    
    @objc var camera: NSDictionary = [:] {
        didSet{
            props.camera = RNMBCamera(camera)
            if (self.isMapReady){
                updateCamera()
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
    
    init() {
        super.init(frame: UIScreen.main.bounds)
    }
    
    override func didMoveToWindow() {
        initMap()
    }
    
    func initMap(){
        self.mapView = MGLMapView(frame: self.bounds)
        self.addSubview(mapView!)
        self.isMapReady = true
        
        updateCamera()
        props.options?.update(mapView)
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
