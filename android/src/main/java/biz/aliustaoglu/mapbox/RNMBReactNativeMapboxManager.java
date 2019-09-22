package biz.aliustaoglu.mapbox;

import com.facebook.react.uimanager.ThemedReactContext;
import biz.aliustaoglu.mapbox.MapBoxModule.MapBoxMapView;
import biz.aliustaoglu.mapbox.MapBoxModule.MapBoxViewController;

public class RNMBReactNativeMapboxManager extends MapBoxViewController {

    public static final String REACT_CLASS = "MapBoxViewController";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public MapBoxMapView createViewInstance(ThemedReactContext c) {
        MapBoxMapView mapBoxMapView= new MapBoxMapView(c);
        return mapBoxMapView;
    }
}
