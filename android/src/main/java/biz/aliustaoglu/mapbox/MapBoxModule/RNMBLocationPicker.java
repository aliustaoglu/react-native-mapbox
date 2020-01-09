package biz.aliustaoglu.mapbox.MapBoxModule;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mapbox.mapboxsdk.maps.MapboxMap;


public class RNMBLocationPicker {
    public Boolean isCameraMoving = false;

    Boolean locationPicker;
    MapBoxMapView mapBoxMapView;
    ImageView hoveringMarker;

    public RNMBLocationPicker(Boolean locationPicker, MapBoxMapView mapBoxMapView) {
        this.locationPicker = locationPicker;
        this.mapBoxMapView = mapBoxMapView;

        this.hoveringMarker = new ImageView(mapBoxMapView.getContext());

        hoveringMarker.setImageResource(com.mapbox.mapboxsdk.R.drawable.mapbox_markerview_icon_default);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        hoveringMarker.setLayoutParams(params);
        hoveringMarker.setVisibility(View.INVISIBLE);
        mapBoxMapView.mapView.addView(hoveringMarker);

    }

    public void update(MapboxMap mapboxMap){
        if (this.locationPicker.equals(true)){
            hoveringMarker.setVisibility(View.VISIBLE);
        } else {
            hoveringMarker.setVisibility(View.INVISIBLE);
        }
    }
}
