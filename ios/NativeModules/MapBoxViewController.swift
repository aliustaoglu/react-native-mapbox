import Foundation

@objc(MapBoxViewController)
class MapBoxViewController: RCTViewManager {
    var mapBoxMap: MapBoxMapView!
    public static var sharedInstance = MapBoxViewController()
    public static var eventEmitter: ReactNativeEventEmitter!
    
    func registerEventEmitter(eventEmitter: ReactNativeEventEmitter) {
        MapBoxViewController.eventEmitter = eventEmitter
    }
    
    override func view() -> UIView? {
        mapBoxMap = MapBoxMapView()
        return mapBoxMap
    }
    
    @objc
    func setCamera(_ node: NSNumber, location:NSArray){
        mapBoxMap.setCamera(location)
    }
    
    @objc
    func setPadding(_ node: NSNumber, padding:NSArray){
        mapBoxMap.setPadding(padding)
    }
    
    @objc
    func setBounds(_ node: NSNumber, bounds:NSArray){
        mapBoxMap.setBounds(bounds)
    }
    
    @objc
    func getCameraPosition(_ node: NSNumber, params:NSArray){
        mapBoxMap.getCameraPosition(params)
    }
}

@objc(ReactNativeEventEmitter)
open class ReactNativeEventEmitter: RCTEventEmitter {
    
    override init() {
        super.init()
        MapBoxViewController.sharedInstance.registerEventEmitter(eventEmitter: self)
    }
    
    /// Base overide for RCTEventEmitter.
    ///
    /// - Returns: all supported events
    @objc open override func supportedEvents() -> [String] {
        return ["onGetCameraPosition"]
    }

}
