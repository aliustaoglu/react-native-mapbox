package biz.aliustaoglu.mapbox.MapBoxModule;


import android.content.Context;
import com.facebook.react.uimanager.ThemedReactContext;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import javax.annotation.Nonnull;

import biz.aliustaoglu.mapbox.GenericMapModule.GenericMapLayout;
import biz.aliustaoglu.mapbox.GenericMapModule.GenericMapViewController;


public class MapBoxViewController extends GenericMapViewController {

    private MapboxMap mapboxMap;
    private Context context;
    private MapBoxMapView mapView;

    @Nonnull
    @Override
    public String getName() {
        return "MapBoxViewController";
    }

    @Nonnull
    @Override
    protected GenericMapLayout createViewInstance(@Nonnull ThemedReactContext reactContext) {
        context = reactContext;
        mapView = new MapBoxMapView(reactContext);
        return mapView;
    }



}
