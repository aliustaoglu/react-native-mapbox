import Foundation

@objc(MapBoxViewController)
class MapBoxViewController: RCTViewManager {
    var mapBoxMap: MapBoxMapView!
    
    override func view() -> UIView? {
        mapBoxMap = MapBoxMapView()
        return mapBoxMap
    }
    
    @objc
    func locateUser(_ location:NSArray, resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock){
        mapBoxMap.locateUser(location, resolve: resolve, reject: reject)
    }
    

}
