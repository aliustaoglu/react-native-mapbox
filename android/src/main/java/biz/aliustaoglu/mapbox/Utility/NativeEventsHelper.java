package biz.aliustaoglu.mapbox.Utility;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import biz.aliustaoglu.mapbox.MapBoxModule.MapBoxMapView;

public class NativeEventsHelper {

    ThemedReactContext context;

    public NativeEventsHelper() {

    }

    public NativeEventsHelper(ThemedReactContext reactContext) {
        this.context = reactContext;
    }

    public void setContext(ThemedReactContext context) {
        this.context = context;
    }

    public void getCameraPosition(MapBoxMapView mapBoxMapView){
        if (mapBoxMapView.mapboxMap == null){
            sendJSEvent("onGetCameraPosition", null);
            return;
        }

        CameraPosition cameraPosition = mapBoxMapView.mapboxMap.getCameraPosition();
        Double latitude = cameraPosition.target.getLatitude();
        Double longitude = cameraPosition.target.getLongitude();
        Double altitude = cameraPosition.target.getAltitude();
        Double bearing = cameraPosition.bearing;
        Double tilt = cameraPosition.tilt;
        Double zoom = cameraPosition.zoom;

        WritableMap mapPosition = Arguments.createMap();
        mapPosition.putDouble("latitude", latitude);
        mapPosition.putDouble("longitude", longitude);
        mapPosition.putDouble("altitude", altitude);
        mapPosition.putDouble("bearing", bearing);
        mapPosition.putDouble("tilt", tilt);
        mapPosition.putDouble("zoom", zoom);
        sendJSEvent("onGetCameraPosition", mapPosition);

    }

    public void setCamera(MapBoxMapView mapBoxMapView, @Nullable ReadableArray args) {
        Double lat = args.getMap(0).getDouble("latitude");
        Double lng = args.getMap(0).getDouble("longitude");

        mapBoxMapView.mapboxMap.easeCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
    }

    public void setBounds(MapBoxMapView mapBoxMapView, @Nullable ReadableArray args) {
        List<LatLng> latLngList = new ArrayList<>();

        for (int i = 0; i < args.size(); i++) {
            ReadableMap ll = args.getMap(i);
            latLngList.add(new LatLng(ll.getDouble("lat"), ll.getDouble("lng")));
        }

        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(latLngList).build();
        mapBoxMapView.mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100), 1000);
    }

    public void sendJSEvent(String eventName, @Nullable WritableMap params) {
        if (this.context != null)
            this.context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}
