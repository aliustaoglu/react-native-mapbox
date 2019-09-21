package biz.aliustaoglu.mapbox.Utility;


import android.content.Context;

public class AssetsUtility {
    private Context context;
    public AssetsUtility(Context context){
        this.context = context;
    }

    public int getAssetFromResource(String resourceName){
        int resourceId = this.context.getResources().getIdentifier(resourceName, "drawable", this.context.getPackageName());
        return resourceId;
    }
}
