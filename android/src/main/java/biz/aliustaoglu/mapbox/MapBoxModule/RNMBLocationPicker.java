package biz.aliustaoglu.mapbox.MapBoxModule;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.annotation.Line;


public class RNMBLocationPicker {
    public Boolean isCameraMoving = false;

    ReadableMap locationPicker;
    Boolean pickerEnabled = false;
    MapBoxMapView mapBoxMapView;
    RelativeLayout hoveringMarker;
    ImageView hoveringImage;
    ThemedReactContext context;

    public RNMBLocationPicker(ThemedReactContext context, ReadableMap locationPicker, MapBoxMapView mapBoxMapView) {
        this.locationPicker = locationPicker;
        this.mapBoxMapView = mapBoxMapView;
        this.context = context;

        this.hoveringMarker = new RelativeLayout(mapBoxMapView.getContext());
        this.hoveringImage = new ImageView(mapBoxMapView.getContext());
        this.hoveringImage.setImageResource(com.mapbox.mapboxsdk.R.drawable.mapbox_markerview_icon_default);
        this.hoveringMarker.addView(this.hoveringImage);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        hoveringMarker.setLayoutParams(params);
        hoveringMarker.setVisibility(View.INVISIBLE);
        mapBoxMapView.mapView.addView(hoveringMarker);
    }

    public void update(MapboxMap mapboxMap) {
        if (locationPicker != null) this.locationPicker = locationPicker;

        if (this.locationPicker.getBoolean("pickerEnabled") == true) {
            hoveringMarker.setVisibility(View.VISIBLE);
        } else {
            hoveringMarker.setVisibility(View.INVISIBLE);
        }
    }

    public void update(MapboxMap mapboxMap, ReadableMap locationPicker) {
        if (locationPicker != null) this.locationPicker = locationPicker;

        if (this.locationPicker.getBoolean("pickerEnabled") == true) {
            hoveringMarker.setVisibility(View.VISIBLE);
        } else {
            hoveringMarker.setVisibility(View.INVISIBLE);
        }
    }


}
