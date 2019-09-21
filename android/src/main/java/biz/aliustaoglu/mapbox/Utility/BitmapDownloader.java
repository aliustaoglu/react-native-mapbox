package biz.aliustaoglu.mapbox.Utility;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class BitmapDownloader extends AsyncTask<String, Void, Bitmap> {

    OnAsyncTaskListener<Bitmap> listener;
    Bitmap bitmap = null;
    URL url = null;

    public BitmapDownloader(OnAsyncTaskListener listener){
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        final String url = urls[0];

        try {
            final InputStream inputStream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (final MalformedURLException malformedUrlException) {
            listener.onAsyncTaskFailure(malformedUrlException);
        } catch (final IOException ioException) {
            listener.onAsyncTaskFailure(ioException);
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        listener.onAsyncTaskSuccess(bitmap);
    }
}
