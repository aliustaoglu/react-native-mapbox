package biz.aliustaoglu.mapbox.MapBoxModule;


import android.content.Context;

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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class MapBoxViewController extends SimpleViewManager<MapBoxMapView> {

    private MapboxMap mapboxMap;
    private ThemedReactContext context;
    private MapBoxMapView mapView;

    // Native direct commands
    private static final int LOCATE_USER = 0;

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
                .put("onCameraMove", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onCameraMove")))
                .put("onCameraMoveEnd", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onCameraMoveEnd")))
                .build();
    }

    @Override
    public Map<String, Integer> getCommandsMap() {
        Map<String, Integer> commandsMap = new HashMap();
        commandsMap.put("locateUser", LOCATE_USER);
        return commandsMap;
    }

    @Override
    public void receiveCommand(MapBoxMapView mapLayout, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(mapLayout, commandId, args);
    }

    @Override
    public void receiveCommand(MapBoxMapView mapLayout, String commandName, @Nullable ReadableArray args) {
        super.receiveCommand(mapLayout, commandName, args);
        switch (commandName) {
            case "locateUser":
                locateUser(mapLayout, args);
                break;
        }
    }

    private void locateUser(MapBoxMapView mapLayout, @Nullable ReadableArray args) {
        Double lat = args.getMap(0).getDouble("latitude");
        Double lng = args.getMap(0).getDouble("longitude");

        mapLayout.mapboxMap.easeCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
    }


    @ReactProp(name = "camera")
    public void setCamera(MapBoxMapView mapBoxMapView, @Nullable ReadableMap camera) {
        mapBoxMapView.camera = new RNMBCamera(camera);
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
