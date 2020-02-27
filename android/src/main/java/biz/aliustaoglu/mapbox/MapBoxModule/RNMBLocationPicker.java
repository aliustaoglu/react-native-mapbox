package biz.aliustaoglu.mapbox.MapBoxModule;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.uimanager.ThemedReactContext;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Line;

import biz.aliustaoglu.mapbox.R;
import biz.aliustaoglu.mapbox.Utility.NativeEventsHelper;


public class RNMBLocationPicker {
    public Boolean isCameraMoving = false;

    ReadableMap locationPicker;
    Boolean pickerEnabled = false;
    MapBoxMapView mapBoxMapView;
    RelativeLayout hoveringMarker;
    ImageView hoveringImage;
    ThemedReactContext context;
    NativeEventsHelper nativeEventsHelper;

    public RNMBLocationPicker(ThemedReactContext context, ReadableMap locationPicker, MapBoxMapView mapBoxMapView) {
        this.locationPicker = locationPicker;
        this.mapBoxMapView = mapBoxMapView;
        this.context = context;
        this.nativeEventsHelper = new NativeEventsHelper();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.location_picker, mapBoxMapView.mapView, true);
        hoveringMarker = mapBoxMapView.mapView.findViewById(R.id.layoutLocationPicker);
        hoveringImage = hoveringMarker.findViewById(R.id.imgPicker);
        hoveringMarker.setVisibility(View.INVISIBLE);

    }

    public void update(MapboxMap mapboxMap) {
        if (locationPicker != null) this.locationPicker = locationPicker;

        if (this.locationPicker.getBoolean("pickerEnabled") == true) {
            showMarker();
        } else {
            hideMarker();
        }
    }

    public void update(MapboxMap mapboxMap, ReadableMap locationPicker) {
        if (locationPicker != null) this.locationPicker = locationPicker;

        if (this.locationPicker.getBoolean("pickerEnabled") == true) {
            showMarker();
        } else {
            hideMarker();
        }
    }

    private void showMarker() {
        hoveringMarker.setVisibility(View.VISIBLE);

    }

    private void hideMarker() {
        hoveringMarker.setVisibility(View.INVISIBLE);
    }


}
