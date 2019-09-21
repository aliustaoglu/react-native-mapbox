package biz.aliustaoglu.mapbox.MapBoxModule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import java.net.URL;

import biz.aliustaoglu.mapbox.GenericMapModule.GenericMapLayout;
import biz.aliustaoglu.mapbox.Utility.BitmapDownloader;
import biz.aliustaoglu.mapbox.Utility.OnAsyncTaskListener;



public class MapBoxMapView extends GenericMapLayout implements OnMapReadyCallback, Style.OnStyleLoaded {
    MapboxMap mapInstance = null;
    MapView mapView = null;
    ReadableMap coords;
    Style style;
    SymbolManager symbolManager;

    public MapBoxMapView(@NonNull Context context) {
        super(context);
        mapView = new MapView(context);
        this.addView(mapView);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.isMapReady = true;
        mapInstance = mapboxMap;
        mapInstance.setStyle(Style.MAPBOX_STREETS);
        reactNativeEvent("onMapReady", null);
        mapInstance.getStyle(this);


        // Init props
        if (this.zoom != null) setZoom(this.zoom);
        if (this.lat != null && this.lng != null) setRegion(this.lat, this.lng);
        if (this.mapStyle != null) setMapStyle(this.mapStyle);
    }

    @Override
    public void setZoom(Integer zoom) {
        super.setZoom(zoom);
        if (this.isMapReady) {
            CameraPosition position = new CameraPosition.Builder().zoom(this.zoom).build();
            mapInstance.setCameraPosition(position);
        }
    }

    @Override
    public void setRegion(Double lat, Double lng) {
        super.setRegion(lat, lng);
        if (this.isMapReady) {
            CameraPosition position = new CameraPosition.Builder().target(new LatLng(this.lat, this.lng)).build();
            mapInstance.setCameraPosition(position);
        }
    }

    @Override
    public void setData(ReadableMap data) {
        super.setData(data);
    }

    @Override
    public void setMapStyle(String mapStyle) {
        super.setMapStyle(mapStyle);
        if (this.isMapReady) {
            try {
                String strStyle = this.mapStyle;
                if (!this.mapStyle.startsWith("mapbox://style")) {
                    strStyle = Style.class.getField(this.mapStyle).get(new Object()).toString();
                }
                mapInstance.setStyle(strStyle);
            } catch (Exception e) {

            }
            mapInstance.getStyle(this);
        }
    }

    @Override
    public void setMarkers(ReadableArray markers) {
        super.setMarkers(markers);

        if (this.isMapReady && this.style != null) {
            symbolManager = new SymbolManager(mapView, mapInstance, this.style);

            for (int i = 0; i < markers.size(); i++) {
                ReadableMap marker = markers.getMap(i);
                final LatLng latLng = new LatLng(marker.getDouble("lat"), marker.getDouble("lng"));
                final String label = marker.getString("label");
                String strIcon = marker.getMap("icon").getString("uri");

                // DEBUG
                if (strIcon.startsWith("http")) {
                    BitmapDownloader bd = new BitmapDownloader(new OnAsyncTaskListener<Bitmap>() {
                        @Override
                        public void onAsyncTaskSuccess(Bitmap bm) {
                            setSymbolIcon(bm, latLng, label, 2f);
                        }

                        @Override
                        public void onAsyncTaskFailure(Exception e) {

                        }
                    });
                    bd.execute(strIcon);
                } else {
                    int resourceId = this.assetsUtility.getAssetFromResource(strIcon);
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), resourceId);
                    setSymbolIcon(bm, latLng, label, 0.5f);
                }
            }


        }
    }

    private void setSymbolIcon(Bitmap bm, LatLng latLng, String label, Float iconSize) {
        mapInstance.getStyle().addImage("my-marker", bm);
        symbolManager.create(new SymbolOptions()
                .withLatLng(latLng)
                //set the below attributes according to your requirements
                .withIconImage("my-marker")
                .withIconSize(iconSize)
                .withIconOffset(new Float[]{0f, -1.5f})
                .withTextField(label)
                .withTextHaloColor("rgba(255, 255, 255, 100)")
                .withTextHaloWidth(5.0f)
                .withTextAnchor("top")
                .withTextOffset(new Float[]{0f, 0.5f})
        );
    }


    @Override
    public void locateUser(ReadableArray args) {
        coords = args.getMap(0);
        if (this.style != null && this.isMapReady) {
            LocationComponent locationComponent = mapInstance.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(activity, style).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            CameraPosition position = new CameraPosition.Builder().target(new LatLng(coords.getDouble("latitude"), coords.getDouble("longitude"))).zoom(zoom).build();
            mapInstance.setCameraPosition(position);
        }
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) throws SecurityException {
        this.style = style;


        //symbolManager.setIconAllowOverlap(true);
        //symbolManager.setTextAllowOverlap(true);

        if (this.markers != null) this.setMarkers(this.markers);
    }


}