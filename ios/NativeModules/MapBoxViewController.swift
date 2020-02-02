import Foundation

@objc(MapBoxViewController)
class MapBoxViewController: RCTViewManager {
    var mapBoxMap: MapBoxMapView!
    
    override func view() -> UIView? {
        mapBoxMap = MapBoxMapView()
        return mapBoxMap
    }
    
    @objc
    func setCamera(_ location:NSArray, resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock){
        mapBoxMap.setCamera(location, resolve: resolve, reject: reject)
    }
    
    @objc
    func setBounds(_ bounds:NSArray, resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock){
        mapBoxMap.setBounds(bounds, resolve: resolve, reject: reject)
    }
    
    @objc
    func getCameraPosition(_ params:NSArray, resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock){
        mapBoxMap.getCameraPosition(params, resolve: resolve, reject: reject)
    }
    

}
