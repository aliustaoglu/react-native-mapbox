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

        easeCameraPromise(mapBoxMapView,CameraUpdateFactory.newCameraPosition(cameraPosition), duration, "onSetCamera", null);
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
        easeCameraPromise(mapBoxMapView, cameraUpdate, 500, "onSetPadding", null);
    }

    public void setBounds(MapBoxMapView mapBoxMapView, @Nullable ReadableArray args) {
        List<LatLng> latLngList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            ReadableMap ll = args.getMap(i);
            latLngList.add(new LatLng(ll.getDouble("lat"), ll.getDouble("lng")));
        }
        double[] padding;
        double[] currentPadding = mapBoxMapView.mapboxMap.getCameraPosition().padding;

        // Optional extra paddings
        if (args.size() > 2) {
            ReadableMap paddings = args.getMap(2);
            padding = new double[]{currentPadding[0] + paddings.getDouble("paddingLeft"), currentPadding[1] + paddings.getDouble("paddingLeft"), currentPadding[2] + paddings.getDouble("paddingLeft"), currentPadding[3] + paddings.getDouble("paddingLeft")};
        } else {
            padding = currentPadding;
        }

        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(latLngList).build();
        CameraUpdate boundsCamera = CameraUpdateFactory.newLatLngBounds(latLngBounds, (int) padding[0], (int) padding[1], (int) padding[2], (int) padding[3]);
        easeCameraPromise(mapBoxMapView, boundsCamera, 1000, "onSetBounds", null);
    }

    private void easeCameraPromise(MapBoxMapView mapBoxMapView, CameraUpdate cameraUpdate, int duration, final String promiseName, final WritableMap promiseParams){
        mapBoxMapView.mapboxMap.easeCamera(cameraUpdate, duration, new MapboxMap.CancelableCallback() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish() {
                sendJSEvent(promiseName, promiseParams);
            }
        });
    }

    public void sendJSEvent(String eventName, @Nullable WritableMap params) {
        if (this.context != null)
            this.context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}
