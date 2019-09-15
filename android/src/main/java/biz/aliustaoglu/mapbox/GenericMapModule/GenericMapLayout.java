package biz.aliustaoglu.mapbox.GenericMapModule;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.widget.LinearLayout;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

public class GenericMapLayout extends LinearLayout{
    public Boolean isMapReady = false;
    public Activity activity;

    // Props - Start
    public Double lat;
    public Double lng;
    public ReadableMap data;
    public Integer zoom;
    public String mapStyle;
    public ReadableArray markers;
    // Props - End

    public GenericMapLayout(Context context) {
        super(context);
        this.activity = getActivity(context);
    }


    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public void setRegion(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    // Reserved prop
    public void setData(ReadableMap data) {

        this.data = data;
    }

    public void setMapStyle(String mapStyle){

        this.mapStyle = mapStyle;
    }

    public void setMarkers(ReadableArray markers){

        this.markers = markers;
    }
    

    public void locateUser(ReadableArray args) {

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