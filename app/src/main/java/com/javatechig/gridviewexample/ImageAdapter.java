package com.javatechig.gridviewexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bluebery on 5/27/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String TAG = "ImageAdapter";

    int pixels;
    private ArrayList<String> fileNames = null;

    private HashMap<Integer, ImageView> views;

    private final String BUNDLE_URL = "url";
    private final String BUNDLE_BM = "bm";
    private final String BUNDLE_POS = "pos";
    private final String BUNDLE_ID = "id";
    private final String BUNDLE_VIEW = "view";

    ImageDownloader imageDownloader;

    public ImageAdapter(Context c, ArrayList<String> fileNames) {
        mContext = c;

        final float scale = c.getResources().getDisplayMetrics().density;
        pixels = (int) (100 * scale + 0.5f);

        this.fileNames = fileNames;
        views = new HashMap<Integer, ImageView>();

        imageDownloader = new ImageDownloader();
    }

    public int getCount() {
        return fileNames.size();
    }

    public Object getItem(int position) {
        return fileNames.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public ImageDownloader getImageDownloader() {
        return imageDownloader;
    }

    // we need to cancel the render on a imageview when we recycle it again after sending it to be rendered
    // http://android-developers.blogspot.ca/2010/07/multithreading-for-performance.html

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            //Log.d(TAG, "Fresh");
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(pixels, pixels));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
            //Log.d(TAG, "Recycled");
            imageView = (ImageView) convertView;
            //imageView.setImageResource(android.R.color.background_light);
        }

        Log.d(TAG, "putting position " + position);

        imageDownloader.download(fileNames.get(position), imageView);
        //new LoadImage().execute(position);

        //imageView.setImageResource(mThumbIds[position]);
        //imageView.setImageBitmap(FileHelper.GetImageFromExternalStorage(fileNames.get(position)));
        return imageView;
    }

    // http://stackoverflow.com/questions/9832944/android-load-asynchronous-pictures-in-listview

    //asyncTackClass for loadingpictures
    private class LoadImage extends AsyncTask<Integer, Void, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... position) {

            Bitmap bitmap = FileHelper.GetImageFromExternalStorage(fileNames.get(position[0]));

            // get info from bundle
            Bundle bundle = new Bundle();
            bundle.putParcelable("BITMAP", bitmap);
            bundle.putInt("POSITION", position[0]);

            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            super.onPostExecute(result);

            int pos = result.getInt("POSITION");

            //get picture saved in the map + set
            ImageView view = views.get(result.getInt("POSITION"));
            Bitmap bitmap = (Bitmap) result.getParcelable("BITMAP");

            Log.d(TAG, "rendering position " + result.getInt("POSITION"));

            if (bitmap != null){ //if bitmap exists...
                view.setImageBitmap(bitmap);
                int x = 0;
                x = x + 1;
            }else{ //if not picture, display the default ressource
                view.setImageResource(R.drawable.image_1);
            }
        }

    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9,
            R.drawable.image_1, R.drawable.image_1,
            R.drawable.image_2, R.drawable.image_2,
            R.drawable.image_3, R.drawable.image_3,
            R.drawable.image_4, R.drawable.image_4,
            R.drawable.image_5, R.drawable.image_5,
            R.drawable.image_6, R.drawable.image_6,
            R.drawable.image_7, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_8,
            R.drawable.image_9, R.drawable.image_9
    };
}
