package biz.aliustaoglu.mapbox.MapBoxModule;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.collection.LongSparseArray;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Circle;
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.plugins.annotation.FillManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import java.util.ArrayList;
import java.util.List;

import biz.aliustaoglu.mapbox.Utility.AssetsUtility;
import biz.aliustaoglu.mapbox.Utility.BitmapDownloader;
import biz.aliustaoglu.mapbox.Utility.OnAsyncTaskListener;

public class RNMBMarkers {
    ReadableArray markers;
    MapboxMap mapboxMap;
    SymbolManager symbolManager;
    CircleManager pulsatorManager;
    FillManager fillManager;
    LineManager lineManager;

    Context context;
    Style style;

    JsonParser parser = new JsonParser();

    public RNMBMarkers(ReadableArray markers) {
        this.markers = markers;
    }

    public void update(MapBoxMapView mapBoxMapView) {
        this.mapboxMap = mapBoxMapView.mapboxMap;
        this.symbolManager = mapBoxMapView.symbolManager;
        this.pulsatorManager = mapBoxMapView.pulsatorManager;
        this.lineManager = mapBoxMapView.lineManager;
        this.fillManager = mapBoxMapView.fillManager;
        this.context = mapBoxMapView.getContext();
        this.style = mapBoxMapView.style;


        List<String> symbolIDs = getSymbolIDs();

        for (int i = 0; i < markers.size(); i++) {
            ReadableMap marker = markers.getMap(i);
            String markerID = marker.getString("id");
            symbolIDs.remove(markerID);
            setMarker(marker);
            setPulsator(marker);
        }

        removeMarkers(symbolIDs);
        removePulsators(symbolIDs);
    }

    private List<String> getSymbolIDs() {
        LongSparseArray<Symbol> sparseArray = this.symbolManager.getAnnotations();
        List<String> symbolIDs = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            String s = sparseArray.valueAt(i).getData().getAsJsonObject().get("id").getAsString();
            symbolIDs.add(s);
        }
        return symbolIDs;
    }

    private void setPulsator(ReadableMap marker) {
        final String id = marker.getString("id");
        final Double lat = marker.getDouble("lat");
        final Double lng = marker.getDouble("lng");
        final String title = marker.getString("title");
        final String subtitle = marker.getString("subtitle");
        final String strIcon = marker.getMap("icon").getString("uri");
        final Boolean canPulse = marker.hasKey("pulsator");
        final ReadableMap pulsator = canPulse ? marker.getMap("pulsator") : null;

        if (!canPulse) return;

        float circleRadius = pulsator != null && pulsator.hasKey("radius") ? (float) pulsator.getDouble("radius") : 20f;

        Circle currentPulsator = null;
        LongSparseArray<Circle> pulsators = this.pulsatorManager.getAnnotations();
        for (int i = 0; i < pulsators.size(); i++) {
            String pulsatorId = pulsators.valueAt(i).getData().getAsJsonObject().get("id").getAsString();
            if (pulsatorId.equals(id)) {
                currentPulsator = pulsators.valueAt(i);
                break;
            }
        }
        if (currentPulsator == null) {
            JsonElement pulsatorData = parser.parse("{\"id\":\"" + id + "\"}");
            CircleOptions pulsatorOptions = new CircleOptions()
                    .withGeometry(Point.fromLngLat(lng, lat))
                    .withData(pulsatorData)
                    .withCircleOpacity(0.3f)
                    .withCircleBlur(0.1f);

            if (pulsator.hasKey("color"))
                pulsatorOptions = pulsatorOptions.withCircleColor(pulsator.getString("color"));


            final Circle pulsatorCircle = this.pulsatorManager.create(pulsatorOptions);

            ValueAnimator pulseAnimator = ValueAnimator.ofFloat(0f, 20f);
            pulseAnimator.setDuration(1500);
            pulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
            pulseAnimator.setRepeatMode(ValueAnimator.RESTART);
            pulseAnimator.start();

            pulseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float val = Float.valueOf((float) animation.getAnimatedValue());
                    pulsatorCircle.setCircleRadius(val);
                    pulsatorManager.update(pulsatorCircle);
                }
            });

        } else {
            currentPulsator.setGeometry(Point.fromLngLat(lng, lat));
            this.pulsatorManager.update(currentPulsator);
        }


    }

    // If removed from props, remove from map as well
    private void removeMarkers(List<String> symbolIDs) {
        LongSparseArray<Symbol> symbols = this.symbolManager.getAnnotations().clone();
        for (int i = 0; i < symbolIDs.size(); i++) {
            for (int j = 0; j < symbols.size(); j++) {
                Symbol symbol = symbols.get(j);
                String id = symbol.getData().getAsJsonObject().get("id").getAsString();
                if (id.equals(symbolIDs.get(i))) {
                    this.symbolManager.delete(symbol);
                }
            }
        }
    }

    private void removePulsators(List<String> symbolIDs) {
        LongSparseArray<Circle> pulsators = this.pulsatorManager.getAnnotations().clone();
        for (int i = 0; i < symbolIDs.size(); i++) {
            for (int j = 0; j < pulsators.size(); j++) {
                Circle circle = pulsators.get(j);
                String id = circle.getData().getAsJsonObject().get("id").getAsString();
                if (id.equals(symbolIDs.get(i))) {
                    this.pulsatorManager.delete(circle);
                }
            }
        }
    }

    /**
     * Set markers
     *
     * @param marker - Readable props
     */
    private void setMarker(ReadableMap marker) {
        final String id = marker.getString("id");
        final Double lat = marker.getDouble("lat");
        final Double lng = marker.getDouble("lng");
        final String title = marker.getString("title");
        final String subtitle = marker.getString("subtitle");
        final String strIcon = marker.getMap("icon").getString("uri");
        final Boolean canPulse = marker.hasKey("pulsator");
        final ReadableMap pulsator = canPulse ? marker.getMap("pulsator") : null;

        final LatLng latLng = new LatLng(lat, lng);

        final Float[] iconOffset = marker.hasKey("centerOffset") ?
                new Float[]{(float) marker.getArray("centerOffset").getDouble(0), (float) marker.getArray("centerOffset").getDouble(1)}
                : new Float[]{0f, 0f};
        // Debug
        if (strIcon.startsWith("http")) {
 
            BitmapDownloader bd = new BitmapDownloader(new OnAsyncTaskListener<Bitmap>() {
                @Override
                public void onAsyncTaskSuccess(Bitmap bm) {
                    setSymbolIcon(id, strIcon, bm, latLng, title, 2f, pulsator, iconOffset);
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
            setSymbolIcon(id, strIcon, bm, latLng, title, 1f, pulsator, iconOffset);
        }
    }


    /**
     * Set symbol icon for annotation. Before creating a new one first check if symbol exits. If so update the existing one
     *
     * @param id         Marker id - comes from props
     * @param iconName   Icon name
     * @param bm         Bitmap object
     * @param latLng     Latitude and longitude - comes from props
     * @param label      Title - comes from props
     * @param iconSize   - icon size
     * @param iconOffset - icon offset
     */
    private void setSymbolIcon(String id, String iconName, Bitmap bm, LatLng latLng, String label, Float iconSize, ReadableMap pulsator, Float[] iconOffset) {
        this.mapboxMap.getStyle().addImage(iconName, bm);
        LongSparseArray<Symbol> annotations = this.symbolManager.getAnnotations();
        Boolean hasSymbol = false;


        Symbol currentAnnotation = null;

        for (int i = 0; i < annotations.size(); i++) {
            JsonObject data = (JsonObject) annotations.valueAt(i).getData();
            currentAnnotation = annotations.valueAt(i);
            if (data.get("id").getAsString().equals(id)) {
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
                    .withIconOffset(iconOffset)
            );


        } else {
            currentAnnotation.setLatLng(latLng);
            currentAnnotation.setIconImage(iconName);
            currentAnnotation.setIconSize(iconSize);
            currentAnnotation.setTextField(label);


            symbolManager.update(currentAnnotation);
        }


    }

}
