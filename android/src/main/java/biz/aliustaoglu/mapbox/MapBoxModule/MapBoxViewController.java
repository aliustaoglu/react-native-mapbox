package biz.aliustaoglu.mapbox.MapBoxModule;


import android.content.Context;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class MapBoxViewController extends SimpleViewManager<MapBoxMapView> {

    private MapboxMap mapboxMap;
    private ThemedReactContext context;
    private MapBoxMapView mapView;

    @Nonnull
    @Override
    public String getName() {
        return "MapBoxViewController";
    }

    @Nonnull
    @Override
    protected MapBoxMapView createViewInstance(@Nonnull ThemedReactContext reactContext) {
        this.context = reactContext;
        return new MapBoxMapView(reactContext);
    }

    // @ReactProp cannot be used for events. All events should be built here and then called using RCTEventEmitter (see reactNativeEvent)
    @Nullable
    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put("onMapReady", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onMapReady")))
                .put("onMarkerClick", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onMarkerClick")))
                .build();
    }

    @ReactProp(name="camera")
    public void setCamera(MapBoxMapView mapBoxMapView, @Nullable ReadableMap camera){
        mapBoxMapView.camera = new RNMBCamera(camera);
        if (mapBoxMapView.isMapReady) mapBoxMapView.setCamera();
    }

    @ReactProp(name="options")
    public void setOptions(MapBoxMapView mapBoxMapView, @Nullable ReadableMap options){
        mapBoxMapView.options = new RNMBOptions(options);
        if (mapBoxMapView.isMapReady) mapBoxMapView.setOptions();
    }

    @ReactProp(name="mapStyle")
    public void setMapStyle(MapBoxMapView mapBoxMapView, @Nullable ReadableMap mapStyle){
        mapBoxMapView.mapStyle = new RNMBMapStyle(mapStyle);
        if (mapBoxMapView.isMapReady) mapBoxMapView.setMapStyle();
    }


}
