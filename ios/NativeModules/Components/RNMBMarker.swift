import Foundation


@objc(RNMBMarker)
class RNMBMarker: RCTViewManager {
    override func view() -> UIView? {
        let uvv = UIView()
        return uvv
    }
    
    
    override static func requiresMainQueueSetup() -> Bool {
        return true
    }
}
