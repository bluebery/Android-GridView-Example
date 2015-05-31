package com.javatechig.gridviewexample;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Bluebery on 5/27/2015.
 */
public class FileHelper {
    public static Bitmap GetImageFromExternalStorage(String filePath) {
        File f = new File(filePath);

        if (!f.exists()) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        options.inJustDecodeBounds = false;

        Bitmap tmp = BitmapFactory.decodeFile(filePath, options);

        return tmp;
    }

    // gets a bitmap from external storage, using a uri (i.e. from the file chooser or captured image)
    // calculates the down sampling using calculateInSampleSize
    public static Bitmap GetImageFromExternalStorage(ContentResolver resolver, Uri uri) throws FileNotFoundException {

        InputStream stream = resolver.openInputStream(uri);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);

        stream = resolver.openInputStream(uri);

        // Calculate inSampleSize
        int sampleSize = calculateInSampleSize(options, 500, 500); // todo: play with these numbers
        options.inSampleSize = sampleSize >= 4 ? sampleSize : sampleSize * 2;

        Log.d("FileHelper", "actual sample size: " + options.inSampleSize);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(stream, null, options);
    }

    // helps to rotate the bitmap using a matrix
    // note that this creates a new bitmap in memory!
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        // don't bother rotating of the angle is 0
        if(angle == 0) {
            return source;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        Log.d("FileHelper", "Sample size: " + inSampleSize);

        return inSampleSize;
    }

    public static Bitmap GetImageFromInternalStorage(FileInputStream fis) throws IOException {

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;

        Bitmap image = BitmapFactory.decodeStream(fis, null, opt);
        fis.close();
        return image;
    }

    public static void SaveImageToInternalStorage(FileOutputStream fos, Bitmap image) throws IOException {
        image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
    }

    public static void DeleteFileFromInternalStorage(Context context, String filename) {
        context.deleteFile(filename);
    }

    public static void ListFiles(Context context) {
        File[] files = context.getFilesDir().listFiles();

        for(int i = 0; i < files.length; i++) {
            Log.d("FileHelper", files[i].toString());
        }
    }

    public static void DeleteAllFilesFromInternalStorage(Context context) {
        File[] files = context.getFilesDir().listFiles();

        for(int i = 0; i < files.length; i++) {
            if(!files[i].toString().contains("CACHE")) {
                Log.d("FileHelper", "Removing " + files[i].toString());
                files[i].delete();
            }
        }
    }

    // In the case that our design has been cached to disk, grab the raw bitmap from internal storage
    public static Bitmap GetBitmapFromInternalStorage() {
        return null;
    }

    // helper method to perform a 'centercrop' scale by providing a matrix to transform the bitmap
    //
    // "Scale the image uniformly (maintain the image's aspect ratio) so that both dimensions (width and height)
    // of the image will be equal to or larger than the corresponding dimension of the view (minus padding)."
    public static Matrix ScaleCenterCrop(Bitmap result, Bitmap backgroundImage) {

        float bgWidth = backgroundImage.getWidth();
        float bgHeight = backgroundImage.getHeight();

        // the ratio of view size to background image size
        float scaleWidth = result.getWidth() / bgWidth;
        float scaleHeight = result.getHeight() / bgHeight;

        // we have to scale by one of the ratios; choose the max
        float scaleFactor = Math.max(scaleWidth, scaleHeight);

        float yTranslate = 0;
        float xTranslate = 0;

        // if we are scaling the width(/height) perfectly, translate the y(/x);
        // take the new height(/width) (after scaling), and subtract the visible area. take half the difference.
        if(scaleFactor == scaleWidth) {
            float yDiff = (bgHeight * scaleFactor) - result.getHeight();
            yTranslate = yDiff / 2;
        }
        else {
            float xDiff = (bgWidth * scaleFactor) - result.getWidth();
            xTranslate = xDiff  / 2;
        }

        Log.d("TemplateFragment", "scalefactor " + scaleFactor);
        Log.d("TemplateFragment", "xTranslate " + xTranslate);
        Log.d("TemplateFragment", "yTranslate " + yTranslate);

        Matrix matrix = new Matrix();
        matrix.preScale(scaleFactor, scaleFactor);
        matrix.postTranslate(-xTranslate, -yTranslate);

        return matrix;
    }

    /** Returns how much we have to rotate */
    public static float RotationForImage(Context context, Uri uri) {
        try{
            if (uri.getScheme().equals("content")) {
                //From the media gallery
                String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
                Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
                if (c.moveToFirst()) {
                    return c.getInt(0);
                }
            } else if (uri.getScheme().equals("file")) {
                //From a file saved by the camera
                ExifInterface exif = new ExifInterface(uri.getPath());
                int rotation = (int) exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL));
                return rotation;
            }
            return 0;

        } catch (IOException e) {
            Log.e("FileHelper", "Error checking exif", e);
            return 0;
        }
    }

    /** Get rotation in degrees */
    private static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}
