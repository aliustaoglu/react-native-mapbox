package biz.aliustaoglu.mapbox.MapBoxModule;


import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.FillManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;


public class MapBoxMapView extends LinearLayout implements OnMapReadyCallback, Style.OnStyleLoaded, MapboxMap.OnCameraMoveListener, MapboxMap.OnCameraIdleListener {
    public boolean isMapReady = false;
    public boolean isStyleLoaded = false;

    public MapView mapView;
    public MapboxMap mapboxMap;
    public MarkerViewManager markerViewManager;
    public SymbolManager symbolManager;
    public CircleManager pulsatorManager;
    public FillManager fillManager;
    public LineManager lineManager;

    public Style style;


    // Props
    public RNMBOptions options;
    public RNMBCamera camera;
    public RNMBMapStyle mapStyle;
    public RNMBLocationPicker locationPicker;
    public RNMBMarkers markers;
    public RNMBPolylines polylines;

    public MapBoxMapView(@NonNull ReactContext context) {
        super(context);
        this.mapView = new MapView(context);
        this.addView(mapView);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        this.markerViewManager = new MarkerViewManager(this.mapView, mapboxMap);

        isMapReady = true;

        mapboxMap.getStyle(this);
        mapboxMap.addOnCameraMoveListener(this);
        mapboxMap.addOnCameraIdleListener(this);


        if (mapStyle == null) mapStyle = new RNMBMapStyle();
        setMapStyle();
        reactNativeEvent("onMapReady", null);

        setLocationPicker();
    }

    public void setOptions(){
        if (this.options != null) this.options.update(mapboxMap, mapView, this.getContext());
    }

    public void setCamera(){
        if (this.camera != null) this.camera.update(mapboxMap);
    }

    public void setMapStyle(){
        this.mapStyle.update(mapboxMap);
    }
    public void setLocationPicker(){
        if (this.locationPicker == null) return;
        
        this.locationPicker.update(mapboxMap);
    }

    public void setMarkers(){
        if (this.markers != null) this.markers.update(this);
    }

    public void setPolylines(){
        if (this.polylines != null) this.polylines.update(this);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
        this.isStyleLoaded = true;
        this.symbolManager = new SymbolManager(mapView, mapboxMap, style);
        this.pulsatorManager = new CircleManager(mapView, mapboxMap, style);
        this.lineManager = new LineManager(mapView, mapboxMap, style);
        this.fillManager= new FillManager(mapView, mapboxMap, style);

        this.style = style;

        if (this.mapStyle != null) this.mapStyle.update(mapboxMap, mapView, style);

        setCamera();
        setOptions();
        setMarkers();
        setPolylines();
    }


    protected void reactNativeEvent(String eventName, WritableMap eventParams) {
        ReactContext reactContext = (ReactContext) this.getContext();
        reactContext
                .getJSModule(RCTEventEmitter.class)
                .receiveEvent(this.getId(), eventName, eventParams);
    }


    @Override
    public void onCameraMove() {
        WritableMap location = getLocation();
        reactNativeEvent("onCameraMove", location);
    }

    @Override
    public void onCameraIdle() {
        WritableMap location = getLocation();
        reactNativeEvent("onCameraMoveEnd", location);
    }

    private WritableMap getLocation(){
        CameraPosition position = mapboxMap.getCameraPosition();
        WritableMap location = Arguments.createMap();
        location.putDouble("lat", position.target.getLatitude());
        location.putDouble("lng", position.target.getLongitude());
        return location;
    }

}