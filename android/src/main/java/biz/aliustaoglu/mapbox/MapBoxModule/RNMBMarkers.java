package biz.aliustaoglu.mapbox.MapBoxModule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.collection.LongSparseArray;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.annotation.Annotation;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import biz.aliustaoglu.mapbox.Utility.AssetsUtility;
import biz.aliustaoglu.mapbox.Utility.BitmapDownloader;
import biz.aliustaoglu.mapbox.Utility.OnAsyncTaskListener;

public class RNMBMarkers {
    ReadableArray markers;
    MapboxMap mapboxMap;
    SymbolManager symbolManager;
    Context context;

    JsonParser parser = new JsonParser();

    public RNMBMarkers(ReadableArray markers){
        this.markers = markers;
    }

    public void update(MapboxMap mapboxMap, Context context, SymbolManager symbolManager){
        this.mapboxMap = mapboxMap;
        this.symbolManager = symbolManager;
        this.context = context;

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

        Symbol currentAnnotation = null;

        for (int i=0;i<annotations.size();i++){
            JsonObject data = (JsonObject) annotations.valueAt(i).getData();
            currentAnnotation = annotations.get(i);
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
                    .withIconOffset(iconOffset)
                    .withTextField(label)
                    .withTextHaloColor("rgba(255, 255, 255, 100)")
                    .withTextHaloWidth(5.0f)
                    .withTextAnchor("top")
                    .withTextOffset(new Float[]{0f, 0.5f})
                    .withData(symbolData)
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
