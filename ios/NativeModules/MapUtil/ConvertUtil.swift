//
//  ConvertUtil.swift
//  RNMBReactNativeMapbox
//
//  Created by Cuneyt Aliustaoglu on 13/01/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation

@objc(ConvertUtil)
class ConvertUtil: NSObject {
    @objc func getBase64FromImageURL(_ assetSource:NSDictionary, resolve:RCTPromiseResolveBlock, reject:RCTPromiseRejectBlock){
        let utils = Utils()
        let uri = assetSource.object(forKey: "uri")! as! String
        let imageData = NSData.init(contentsOf: NSURL(string : uri)! as URL)!
        let base64String = imageData.base64EncodedString(options: NSData.Base64EncodingOptions(rawValue: 0))
        utils.returnPromise(success: true, response: base64String, resolve: resolve, reject: reject)
    }
}
