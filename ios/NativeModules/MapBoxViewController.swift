import Foundation

@objc(MapBoxViewController)
class MapBoxViewController: GenericMapViewController {
    var mapBoxMap: MapBoxMapView!
    override func view() -> UIView? {
        mapBoxMap = MapBoxMapView(frame: CGRect(x:0, y:0, width: 0, height: 0))
        return mapBoxMap
    }
    
    
    override
    func locateUser(_ location:NSArray, resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock){
        mapBoxMap.locateUser(location: location, resolve: resolve, reject: reject)
    }
}
