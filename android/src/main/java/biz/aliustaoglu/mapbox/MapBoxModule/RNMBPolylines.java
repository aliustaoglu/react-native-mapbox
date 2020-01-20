package biz.aliustaoglu.mapbox.MapBoxModule;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

public class RNMBPolylines {

    ReadableArray polylines;

    public RNMBPolylines(ReadableArray polylines) {
        this.polylines = polylines;
    }

    public void update(MapBoxMapView mapBoxMapView) {
        Style style = mapBoxMapView.mapboxMap.getStyle();
        if (style != null && style.isFullyLoaded()) {
            updatePolylines(style);
        }
    }

    private void updatePolylines(Style style) {
        for (int i = 0; i < this.polylines.size(); i++) {
            ReadableMap polyline = polylines.getMap(i);
            ReadableMap properties = polyline.getMap("properties");
            String id = properties.getString("id");

            // Construct optional varargs values for LineLayer.withProperties(...)
            List<PropertyValue<?>> propertyValues = new ArrayList<PropertyValue<?>>();

            propertyValues.add(PropertyFactory.lineJoin(Property.LINE_JOIN_MITER));
            if (properties.hasKey("lineWidth"))
                propertyValues.add(PropertyFactory.lineWidth((float) properties.getDouble("lineWidth")));
            if (properties.hasKey("lineColor"))
                propertyValues.add(PropertyFactory.lineColor(Color.parseColor(properties.getString("lineColor"))));
            if (properties.hasKey("lineType")) {
                if (properties.getString("lineType").equals("dash")) {
                    propertyValues.add(PropertyFactory.lineDasharray(new Float[]{1f, 1f}));
                }
            }

            PropertyValue<?>[] argsPropertyValues = propertyValues.toArray(new PropertyValue<?>[propertyValues.size()]);

            String sourceId = "source" + id;
            String lineId = "line" + id;
            getJSON(polyline);

            Feature feature = Feature.fromJson(getJSON(polyline));
            style.addSource(new GeoJsonSource(sourceId, feature));


            LineLayer lineLayer = new LineLayer(lineId, sourceId).withProperties(argsPropertyValues);
            style.addLayer(lineLayer);


        }
    }

    private String getJSON(ReadableMap readableMap) {
        String json = readableMap.toString();
        json = json.replaceAll("\\{ NativeMap: ", "");
        json = json.substring(0, json.lastIndexOf('}'));
        return json;
    }

    private String getJSON(ReadableArray readableArray) {
        return polylines.toString();
    }
}
