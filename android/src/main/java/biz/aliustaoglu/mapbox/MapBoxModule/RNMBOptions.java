package biz.aliustaoglu.mapbox.MapBoxModule;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.core.PermissionListener;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.pluginscalebar.ScaleBarOptions;
import com.mapbox.pluginscalebar.ScaleBarPlugin;

public class RNMBOptions implements PermissionListener {
    ReadableMap options;

    public RNMBOptions(ReadableMap options) {
        this.options = options;
    }

    public void update(MapboxMap mapboxMap, MapView mapView, Context context) {
        Boolean showsUserHeadingIndicator = false;
        Boolean showsScale = false;
        Boolean showsHeading = false;
        Boolean showsUserLocation = false;

        if (options.hasKey("showsUserHeadingIndicator"))
            showsUserHeadingIndicator = options.getBoolean("showsUserHeadingIndicator");
        if (options.hasKey("showsScale")) showsScale = options.getBoolean("showsScale");
        if (options.hasKey("showsHeading")) showsHeading = options.getBoolean("showsHeading");
        if (options.hasKey("showsUserLocation"))
            showsUserLocation = options.getBoolean("showsUserLocation");

        if (showsScale) {
            ScaleBarPlugin scaleBarPlugin = new ScaleBarPlugin(mapView, mapboxMap);
            scaleBarPlugin.create(new ScaleBarOptions(context));
        }

        if (!showsUserLocation) return;


        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(context, mapboxMap.getStyle()).build());
        locationComponent.setLocationComponentEnabled(true);
        if (showsUserHeadingIndicator) {
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        }


    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        return false;
    }
}
