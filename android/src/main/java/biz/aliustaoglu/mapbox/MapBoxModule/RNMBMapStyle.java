package biz.aliustaoglu.mapbox.MapBoxModule;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;


public class RNMBMapStyle {
    String mapStyle;


    public RNMBMapStyle(String mapStyle){
        this.mapStyle = mapStyle;
    }

    public void update(MapboxMap mapboxMap){
        switch (mapStyle.toUpperCase()){
            case "OUTDOORS": mapboxMap.setStyle(Style.OUTDOORS);break;
            case "LIGHT": mapboxMap.setStyle(Style.LIGHT);break;
            case "DARK": mapboxMap.setStyle(Style.DARK);break;
            case "SATELLITE": mapboxMap.setStyle(Style.SATELLITE);break;
            case "SATELLITE_STREETS": mapboxMap.setStyle(Style.SATELLITE_STREETS);break;
            case "TRAFFIC_DAY": mapboxMap.setStyle(Style.TRAFFIC_DAY);break;
            default: mapboxMap.setStyle(Style.MAPBOX_STREETS);
        }

    }
}
