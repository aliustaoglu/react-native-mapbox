#import <Foundation/Foundation.h>
#import "React/RCTViewManager.h"

// All maps should export same properties. Only the module name should differ
// Simply copy & paste.

@interface RCT_EXTERN_MODULE(MapBoxViewController, RCTViewManager)
RCT_EXPORT_VIEW_PROPERTY(zoom, double)
RCT_EXPORT_VIEW_PROPERTY(region, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(onMapReady, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(markers, NSArray)
RCT_EXTERN_METHOD(locateUser:(nonnull NSArray *)location resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
@end


// Even though exported, it should NOT be imported from JavaScript
// JS bridge expects module because it contains necessary methods
@interface RCT_EXTERN_MODULE(GenericMapViewController, RCTViewManager)

@end

