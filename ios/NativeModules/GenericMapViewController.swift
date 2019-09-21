import Foundation

@objc(GenericMapViewController)
class GenericMapViewController: RCTViewManager {
    
    
    @objc
    func locateUser(_ location:NSArray, resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock){
        preconditionFailure("locateUser must be overridden")
    }
    
    override static func requiresMainQueueSetup() -> Bool {
        return true
    }
}


