package biz.aliustaoglu.mapbox.MapBoxModule;

import com.facebook.react.bridge.ReadableMap;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class RNMBCamera {
    ReadableMap camera;

    public RNMBCamera(ReadableMap camera){
        this.camera = camera;
    }


    public void update(MapboxMap mapboxMap){
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
}
