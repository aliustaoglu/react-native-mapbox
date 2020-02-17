package biz.aliustaoglu.mapbox.MapBoxModule;


import android.content.Context;
import android.util.DisplayMetrics;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import biz.aliustaoglu.mapbox.Utility.NativeEventsHelper;


public class MapBoxViewController extends SimpleViewManager<MapBoxMapView> {

    private MapboxMap mapboxMap;
    private ThemedReactContext context;
    private MapBoxMapView mapView;
    private NativeEventsHelper nativeEventsHelper = new NativeEventsHelper();

    // Native direct commands
    private static final int SET_CAMERA = 0;

    @Nonnull
    @Override
    public String getName() {
        return "MapBoxViewController";
    }


    @Nonnull
    @Override
    protected MapBoxMapView createViewInstance(@Nonnull ThemedReactContext reactContext) {
        this.context = reactContext;
        nativeEventsHelper.setContext(reactContext);
        return new MapBoxMapView(reactContext);
    }

    // @ReactProp cannot be used for events. All events should be built here and then called using RCTEventEmitter (see reactNativeEvent)
    @Nullable
    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put("onMapReady", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onMapReady")))
                .put("onMarkerClick", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onMarkerClick")))
                .put("onCameraMove", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onCameraMove")))
                .put("onCameraMoveEnd", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onCameraMoveEnd")))
                .build();
    }

    @Override
    public void receiveCommand(MapBoxMapView mapBoxMapView, String commandName, @Nullable ReadableArray args) {
        super.receiveCommand(mapBoxMapView, commandName, args);
        switch (commandName) {
            case "setCamera":
                this.nativeEventsHelper.setCamera(mapBoxMapView, args);
                break;
            case "setBounds":
                this.nativeEventsHelper.setBounds(mapBoxMapView, args);
                break;
            case "getCameraPosition":
                this.nativeEventsHelper.getCameraPosition(mapBoxMapView);
                break;
            case "setPadding":
                this.nativeEventsHelper.setPadding(mapBoxMapView, args);
                break;
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %s received by %s.",
                        commandName,
                        getClass().getSimpleName()));
        }
    }




    @ReactProp(name = "camera")
    public void setCamera(MapBoxMapView mapBoxMapView, @Nullable ReadableMap camera) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mapBoxMapView.camera = new RNMBCamera(camera, displayMetrics);
        if (mapBoxMapView.isMapReady) mapBoxMapView.setCamera();
    }

    @ReactProp(name = "options")
    public void setOptions(MapBoxMapView mapBoxMapView, @Nullable ReadableMap options) {
        mapBoxMapView.options = new RNMBOptions(options);
        if (mapBoxMapView.isMapReady) mapBoxMapView.setOptions();
    }

    @ReactProp(name = "mapStyle")
    public void setMapStyle(MapBoxMapView mapBoxMapView, @Nullable ReadableMap mapStyle) {
        mapBoxMapView.mapStyle = new RNMBMapStyle(mapStyle);
        if (mapBoxMapView.isMapReady) mapBoxMapView.setMapStyle();
    }

    @ReactProp(name = "locationPicker")
    public void setLocationPicker(MapBoxMapView mapBoxMapView, @Nullable Boolean locationPicker) {
        if (mapBoxMapView.locationPicker == null) {
            mapBoxMapView.locationPicker = new RNMBLocationPicker(locationPicker, mapBoxMapView);
        } else {
            if (mapBoxMapView.isMapReady)
                mapBoxMapView.locationPicker.update(mapboxMap, locationPicker);
        }
    }

    @ReactProp(name = "markers")
    public void setMarkers(MapBoxMapView mapBoxMapView, @Nullable ReadableArray markers) {
        mapBoxMapView.markers = new RNMBMarkers(markers);
        if (mapBoxMapView.isStyleLoaded) mapBoxMapView.setMarkers();
    }

    @ReactProp(name = "polylines")
    public void setPolylines(MapBoxMapView mapBoxMapView, @Nullable ReadableArray polylines) {
        mapBoxMapView.polylines = new RNMBPolylines(polylines);
        if (mapBoxMapView.isStyleLoaded) mapBoxMapView.setPolylines();
    }


}
