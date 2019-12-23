package biz.aliustaoglu.mapbox.MapBoxModule;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class MapBoxMapView extends LinearLayout implements OnMapReadyCallback, Style.OnStyleLoaded {
    public boolean isMapReady = false;
    public final MapView mapView;
    public MapboxMap mapboxMap;


    // Props
    public ReadableMap camera;
    public ReadableMap options;

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

            if (this.camera != null) this.setCamera(this.camera);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
    }


    public void setCamera(ReadableMap camera){
        CameraPosition.Builder cameraBuilder = new CameraPosition.Builder();

        if (camera.hasKey("target")) {
            ReadableMap target = camera.getMap("target");
            cameraBuilder.target(new LatLng(target.getDouble("lat"), target.getDouble("lng")));
        }
        if (camera.hasKey("zoom")) cameraBuilder = cameraBuilder.zoom(camera.getDouble("zoom"));
        if (camera.hasKey("bearing")) cameraBuilder = cameraBuilder.bearing(camera.getDouble("bearing"));
        if (camera.hasKey("tilt")) cameraBuilder = cameraBuilder.tilt(camera.getDouble("tilt"));

        CameraPosition position = cameraBuilder.build();
        mapboxMap.setCameraPosition(position);
    }

    protected void reactNativeEvent(String eventName, WritableMap eventParams) {
        ReactContext reactContext = (ReactContext) this.getContext();
        reactContext
                .getJSModule(RCTEventEmitter.class)
                .receiveEvent(this.getId(), eventName, eventParams);
    }

    public Activity getActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }
}