package biz.aliustaoglu.mapbox.Utility;

import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;

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

    public void getCameraPosition(MapBoxMapView mapBoxMapView) {
        if (mapBoxMapView.mapboxMap == null) {
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
        CameraPosition.Builder cameraBuilder = new CameraPosition.Builder();
        ReadableMap cameraArgs = args.getMap(0);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        if (cameraArgs.hasKey("latitude") && cameraArgs.hasKey("longitude")) {
            Double lat = cameraArgs.getDouble("latitude");
            Double lng = cameraArgs.getDouble("longitude");
            cameraBuilder.target(new LatLng(lat, lng));
        }
        if (cameraArgs.hasKey("zoom"))
            cameraBuilder = cameraBuilder.zoom(cameraArgs.getDouble("zoom"));
        if (cameraArgs.hasKey("bearing"))
            cameraBuilder = cameraBuilder.bearing(cameraArgs.getDouble("bearing"));
        if (cameraArgs.hasKey("tilt"))
            cameraBuilder = cameraBuilder.tilt(cameraArgs.getDouble("tilt"));
        if (cameraArgs.hasKey("padding")) {
            Double[] listPadding = cameraArgs.getArray("padding").toArrayList().toArray(new Double[0]);
            double[] arrPadding = new double[listPadding.length];
            for (int i = 0; i < arrPadding.length; i++)
                arrPadding[i] = listPadding[i] * displayMetrics.scaledDensity;
            cameraBuilder.padding(arrPadding);
        }
        CameraPosition cameraPosition = cameraBuilder.build();
        Integer duration = (cameraArgs.hasKey("duration")) ? cameraArgs.getInt("duration") : 2000;

        mapBoxMapView.mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), duration);
    }

    public void setPadding(MapBoxMapView mapBoxMapView, @Nullable ReadableArray args) {
        MapboxMap mapboxMap = mapBoxMapView.mapboxMap;
        if (mapboxMap == null) return;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        Double[] listPadding = args.toArrayList().toArray(new Double[0]);
        double[] arrPadding = new double[listPadding.length];
        for (int i = 0; i < arrPadding.length; i++)
            arrPadding[i] = listPadding[i] * displayMetrics.scaledDensity;
        CameraUpdate cameraUpdate = CameraUpdateFactory.paddingTo(arrPadding);
        mapboxMap.easeCamera(cameraUpdate);
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
