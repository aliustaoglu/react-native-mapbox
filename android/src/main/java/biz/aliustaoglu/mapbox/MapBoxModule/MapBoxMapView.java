package biz.aliustaoglu.mapbox.MapBoxModule;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

import biz.aliustaoglu.mapbox.R;
import biz.aliustaoglu.mapbox.Utility.SystemUtility;

public class MapBoxMapView extends LinearLayout implements OnMapReadyCallback, Style.OnStyleLoaded {
    public boolean isMapReady = false;
    public final MapView mapView;
    public MapboxMap mapboxMap;


    // Props
    public RNMBOptions options;
    public RNMBCamera camera;

    public MapBoxMapView(@NonNull ReactContext context) {
        super(context);
        mapView = new MapView(context);
        this.addView(mapView);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        isMapReady = true;
        mapboxMap.setStyle(Style.MAPBOX_STREETS);
        mapboxMap.getStyle(this);
        reactNativeEvent("onMapReady", null);


    }

    public void setOptions(){
        this.options.update(mapboxMap, mapView, this.getContext());
    }

    public void setCamera(){
        this.camera.update(mapboxMap);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {

        if (this.camera != null) this.camera.update(mapboxMap);
        if (this.options != null) this.options.update(mapboxMap, mapView, this.getContext());
    }


    protected void reactNativeEvent(String eventName, WritableMap eventParams) {
        ReactContext reactContext = (ReactContext) this.getContext();
        reactContext
                .getJSModule(RCTEventEmitter.class)
                .receiveEvent(this.getId(), eventName, eventParams);
    }

}