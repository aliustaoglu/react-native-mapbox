package biz.aliustaoglu.mapbox.MapBoxModule;

import android.util.DisplayMetrics;

import com.facebook.react.bridge.ReadableMap;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;
import java.util.stream.Stream;

public class RNMBCamera {
    ReadableMap camera;
    DisplayMetrics displayMetrics;

    public RNMBCamera(ReadableMap camera, DisplayMetrics displayMetrics) {
        this.camera = camera;
        this.displayMetrics = displayMetrics;
    }


    public void update(MapboxMap mapboxMap) {
        CameraPosition.Builder cameraBuilder = new CameraPosition.Builder();


        if (camera.hasKey("target")) {
            ReadableMap target = camera.getMap("target");
            cameraBuilder.target(new LatLng(target.getDouble("lat"), target.getDouble("lng")));
        }
        if (camera.hasKey("zoom")) cameraBuilder = cameraBuilder.zoom(camera.getDouble("zoom"));
        if (camera.hasKey("bearing"))
            cameraBuilder = cameraBuilder.bearing(camera.getDouble("bearing"));
        if (camera.hasKey("tilt")) cameraBuilder = cameraBuilder.tilt(camera.getDouble("tilt"));
        if (camera.hasKey("padding")) {
            Double[] listPadding = camera.getArray("padding").toArrayList().toArray(new Double[0]);
            double[] arrPadding = new double[listPadding.length];
            for (int i = 0; i < arrPadding.length; i++)
                arrPadding[i] = listPadding[i] * displayMetrics.scaledDensity;
            cameraBuilder.padding(arrPadding);
        }

        CameraPosition position = cameraBuilder.build();
        mapboxMap.setCameraPosition(position);
    }
}
