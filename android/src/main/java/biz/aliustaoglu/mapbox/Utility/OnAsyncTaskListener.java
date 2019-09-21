package biz.aliustaoglu.mapbox.Utility;

public interface OnAsyncTaskListener<T> {
    void onAsyncTaskSuccess(T object);
    void onAsyncTaskFailure(Exception e);
}
