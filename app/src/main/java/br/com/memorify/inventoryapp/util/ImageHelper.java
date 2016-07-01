package br.com.memorify.inventoryapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageHelper {

    public static Bitmap getPhotoTakenFromAndroid(String takenPhotoPath) {
        final int PHOTO_WIDTH = 200;
        final int PHOTO_HEIGHT = 200;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(takenPhotoPath, opts);

        /** scale approximately **/
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = calculateInSampleSize(opts, PHOTO_WIDTH, PHOTO_HEIGHT, true);
        return BitmapFactory.decodeFile(takenPhotoPath, opts);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, boolean fitInside) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            if (fitInside) {
                inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
            } else {
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
        }
        return inSampleSize;
    }
}
