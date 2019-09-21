import Foundation
class Utils{
    func returnPromise(success:Bool, response:Any, resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock){
        let successDict = [
            "success": success,
            "response": response
        ]
        if (success) {
            resolve(successDict)
        }
        if (!success) {
            reject(response as? String, nil, nil)
        }
    }
}
