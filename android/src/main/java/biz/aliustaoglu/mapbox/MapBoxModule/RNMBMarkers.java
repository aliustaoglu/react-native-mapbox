package biz.aliustaoglu.mapbox.MapBoxModule;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.collection.LongSparseArray;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Annotation;
import com.mapbox.mapboxsdk.plugins.annotation.Circle;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.plugins.annotation.Fill;
import com.mapbox.mapboxsdk.plugins.annotation.FillManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import biz.aliustaoglu.mapbox.R;
import biz.aliustaoglu.mapbox.Utility.AssetsUtility;
import biz.aliustaoglu.mapbox.Utility.BitmapDownloader;
import biz.aliustaoglu.mapbox.Utility.OnAsyncTaskListener;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;

public class RNMBMarkers {
    ReadableArray markers;
    MapboxMap mapboxMap;
    SymbolManager symbolManager;
    CircleManager circleManager;
    FillManager fillManager;
    LineManager lineManager;

    Context context;
    Style style;

    JsonParser parser = new JsonParser();

    public RNMBMarkers(ReadableArray markers){
        this.markers = markers;
    }

    public void update(MapBoxMapView mapBoxMapView){
        this.mapboxMap = mapBoxMapView.mapboxMap;
        this.symbolManager = mapBoxMapView.symbolManager;
        this.circleManager = mapBoxMapView.circleManager;
        this.lineManager = mapBoxMapView.lineManager;
        this.fillManager = mapBoxMapView.fillManager;
        this.context = mapBoxMapView.getContext();
        this.style = mapBoxMapView.style;

        for (int i = 0; i < markers.size(); i++) {
            ReadableMap marker = markers.getMap(i);

            setMarker(marker);
        }
    }

    /**
     * Set markers
     * @param marker - Readable props
     */
    private void setMarker(ReadableMap marker){
        final String id = marker.getString("id");
        final Double lat = marker.getDouble("lat");
        final Double lng = marker.getDouble("lng");
        final String title = marker.getString("title");
        final String subtitle = marker.getString("subtitle");
        final String strIcon = marker.getMap("icon").getString("uri");

        final LatLng latLng = new LatLng(lat, lng);

        final Float[] iconOffset = new Float[]{-1.5f, -15f};
        // Debug
        if (strIcon.startsWith("http")) {

            BitmapDownloader bd = new BitmapDownloader(new OnAsyncTaskListener<Bitmap>() {
                @Override
                public void onAsyncTaskSuccess(Bitmap bm) {
                    setSymbolIcon(id, strIcon, bm, latLng, title, 2f, iconOffset);
                }

                @Override
                public void onAsyncTaskFailure(Exception e) {

                }
            });
            bd.execute(strIcon);
        } else {
            // Prod
            AssetsUtility assetsUtility = new AssetsUtility(this.context);
            int resourceId = assetsUtility.getAssetFromResource(strIcon);
            Bitmap bm = BitmapFactory.decodeResource(this.context.getResources(), resourceId);
            setSymbolIcon(id, strIcon, bm, latLng, title, 1f, iconOffset);
        }
    }

    private void setSymbolIcon1(String id, String iconName, Bitmap bm, LatLng latLng, String label, Float iconSize, Float[] iconOffset) {
        ValueAnimator circleAnimator = ValueAnimator.ofFloat(0f,20f);
        circleAnimator.setDuration(1500);
        circleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        circleAnimator.setRepeatMode(ValueAnimator.RESTART);
        circleAnimator.start();

        final Circle circle = this.circleManager.create(new CircleOptions()
                .withCircleStrokeWidth(1f)
                .withCircleStrokeColor("red")
                .withCircleColor("red")
                .withCircleOpacity(0.05f)
                .withGeometry(Point.fromLngLat(174, -36))
        );


        circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float val = Float.valueOf((float)animation.getAnimatedValue());
                circle.setCircleRadius(val);
                circleManager.update(circle);
            }
        });


    }

    /**
     * Set symbol icon for annotation. Before creating a new one first check if symbol exits. If so update the existing one
     * @param id Marker id - comes from props
     * @param iconName Icon name
     * @param bm Bitmap object
     * @param latLng Latitude and longitude - comes from props
     * @param label Title - comes from props
     * @param iconSize - icon size
     * @param iconOffset - icon offset
     */
    private void setSymbolIcon(String id, String iconName, Bitmap bm, LatLng latLng, String label, Float iconSize, Float[] iconOffset) {
        this.mapboxMap.getStyle().addImage(iconName, bm);
        LongSparseArray<Symbol> annotations = this.symbolManager.getAnnotations();
        Boolean hasSymbol = false;

        final ValueAnimator circleAnimator = ValueAnimator.ofFloat(0f,20f);
        circleAnimator.setDuration(1500);
        circleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        circleAnimator.setRepeatMode(ValueAnimator.RESTART);
        circleAnimator.start();

        Symbol currentAnnotation = null;
        Circle currentCircle = null;

        for (int i=0;i<annotations.size();i++){
            JsonObject data = (JsonObject) annotations.valueAt(i).getData();
            currentAnnotation = annotations.get(i);
            currentCircle = this.circleManager.getAnnotations().get(i);
            if (data.get("id").getAsString().equals(id)){
                hasSymbol = true;
                break;
            }
        }

        if (!hasSymbol) {
            JsonElement symbolData = parser.parse("{\"id\":\"" + id + "\"}");
            Symbol symbol = this.symbolManager.create(new SymbolOptions()
                    .withLatLng(latLng)
                    .withIconImage(iconName)
                    .withIconSize(iconSize)
                    .withData(symbolData)
            );
            final Circle circle = this.circleManager.create(new CircleOptions()
                    .withCircleStrokeWidth(1f)
                    .withCircleStrokeColor("red")
                    .withCircleColor("red")
                    .withCircleOpacity(0.05f)
                    .withGeometry(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
            );
        } else {
            currentAnnotation.setLatLng(latLng);
            currentAnnotation.setIconImage(iconName);
            currentAnnotation.setIconSize(iconSize);
            currentAnnotation.setTextField(label);

            currentCircle.setGeometry(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));

            symbolManager.update(currentAnnotation);
        }







        circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float val = Float.valueOf((float)animation.getAnimatedValue());
                Circle circle = circleManager.getAnnotations().get(0);
                circle.setCircleRadius(val);
                circleManager.update(circle);
            }
        });


    }

}
