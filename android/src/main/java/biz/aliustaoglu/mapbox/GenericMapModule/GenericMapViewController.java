package biz.aliustaoglu.mapbox.GenericMapModule;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

public abstract class GenericMapViewController extends SimpleViewManager<GenericMapLayout> {
    private static final int COMMAND_LOCATE_USER = 0;

    @ReactProp(name = "region")
    public void setRegion(GenericMapLayout mapLayout, @Nullable ReadableMap coords) {
        mapLayout.setRegion(coords.getDouble("lat"), coords.getDouble("lng"));

    }

    @ReactProp(name = "zoom")
    public void setZoom(GenericMapLayout mapLayout, @Nullable Integer zoom) {
        mapLayout.setZoom(zoom);
    }

    @ReactProp(name = "data")
    public void setData(GenericMapLayout mapLayout, @Nullable ReadableMap data) {
        mapLayout.setData(data);
    }

    @ReactProp(name="mapStyle")
    public void setMapStyle(GenericMapLayout mapLayout, @Nullable String mapStyle){
        mapLayout.setMapStyle(mapStyle);
    }

    @ReactProp(name="markers")
    public void setMarkers(GenericMapLayout mapLayout, @Nullable ReadableArray markers){
        mapLayout.setMarkers(markers);
    }


    // @ReactProp cannot be used for events. All events should be built here and then called using RCTEventEmitter (see reactNativeEvent)
    @Nullable
    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put("onMapReady", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onMapReady")))
                .put("onMarkerClick", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onMarkerClick")))
                .build();
    }

    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("locateUser", COMMAND_LOCATE_USER);
    }

    // Receives commands from JavaScript using UIManager.dispatchViewManagerCommand
    @Override
    public void receiveCommand(GenericMapLayout mapLayout, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(mapLayout, commandId, args);
        switch (commandId) {
            case COMMAND_LOCATE_USER:
                mapLayout.locateUser(args);
                break;
        }
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
