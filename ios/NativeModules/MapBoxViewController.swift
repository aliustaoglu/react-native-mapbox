import Foundation

@objc(MapBoxViewController)
class MapBoxViewController: RCTViewManager {
    var mapBoxMap: MapBoxMapView!
    
    override func view() -> UIView? {
        mapBoxMap = MapBoxMapView()
        return mapBoxMap
    }
    

}
