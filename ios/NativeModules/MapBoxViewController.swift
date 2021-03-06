import Foundation

@objc(MapBoxViewController)
class MapBoxViewController: RCTViewManager {
    var mapBoxMap: MapBoxMapView!
    
    override func view() -> UIView? {
        mapBoxMap = MapBoxMapView()
        return mapBoxMap
    }
    
    
    @objc
    func setCamera(_ node: NSNumber, location:NSArray){
        self.bridge.uiManager.addUIBlock { (uiManager, viewRegistry) in
            let currentMapBoxMap = viewRegistry![node] as! MapBoxMapView
            currentMapBoxMap.setCamera(location)
        }
    }
    
    @objc
    func setPadding(_ node: NSNumber, padding:NSArray){
        self.bridge.uiManager.addUIBlock { (uiManager, viewRegistry) in
            let currentMapBoxMap = viewRegistry![node] as! MapBoxMapView
            currentMapBoxMap.setPadding(padding)
        }
    }
    
    @objc
    func setBounds(_ node: NSNumber, bounds:NSArray){
        self.bridge.uiManager.addUIBlock { (uiManager, viewRegistry) in
            let currentMapBoxMap = viewRegistry![node] as! MapBoxMapView
            currentMapBoxMap.setBounds(bounds)
        }
    }
    
    @objc
    func getCameraPosition(_ node: NSNumber, params:NSArray){
        self.bridge.uiManager.addUIBlock { (uiManager, viewRegistry) in
            let currentMapBoxMap = viewRegistry![node] as! MapBoxMapView
            currentMapBoxMap.getCameraPosition(params)
        }
    }
}

class JSEmitter {

    public static var sharedInstance = JSEmitter()
    public static var eventEmitter: ReactNativeEventEmitter!
    
    init(){
        
    }
    
    func registerEventEmitter(eventEmitter: ReactNativeEventEmitter) {
        JSEmitter.eventEmitter = eventEmitter
    }
    
}

@objc(ReactNativeEventEmitter)
open class ReactNativeEventEmitter: RCTEventEmitter {
    
    override init() {
        super.init()
        JSEmitter.sharedInstance.registerEventEmitter(eventEmitter: self)
    }
    
    // Update this function after adding new listeners
    @objc open override func supportedEvents() -> [String] {
        return ["onGetCameraPosition", "onSetPadding", "onSetBounds", "onSetCamera"]
    }

}
