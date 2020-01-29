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
    

}
