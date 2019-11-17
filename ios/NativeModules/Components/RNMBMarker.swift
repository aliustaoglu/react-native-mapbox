import Foundation

@objc(RNMBMarker)
class RNMBMarker: RCTViewManager {
    
    override func view() -> UIView? {
        return UIView()
    }
    
    
    override static func requiresMainQueueSetup() -> Bool {
        return true
    }
}
