#import <Foundation/Foundation.h>
#import "React/RCTViewManager.h"

// All maps should export same properties. Only the module name should differ
// Simply copy & paste.

@interface RCT_EXTERN_MODULE(MapBoxViewController, RCTViewManager)
RCT_EXPORT_VIEW_PROPERTY(camera, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(options, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(mapStyle, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(locationPicker, BOOL)
RCT_EXPORT_VIEW_PROPERTY(markers, NSArray)
RCT_EXPORT_VIEW_PROPERTY(polylines, NSArray)

RCT_EXPORT_VIEW_PROPERTY(onMapReady, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onCameraMove, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onCameraMoveEnd, RCTDirectEventBlock)

RCT_EXTERN_METHOD(locateUser:(nonnull NSArray *)location resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
@end


@interface RCT_EXTERN_MODULE(ConvertUtil, NSObject)
   RCT_EXTERN_METHOD(getBase64FromImageURL: (NSDictionary)assetSource resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
@end
