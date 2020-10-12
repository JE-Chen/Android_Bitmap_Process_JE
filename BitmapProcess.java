package com.je_chen.je_ocr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class BitmapProcess {

    //從路徑取得Bitmap
    public static android.graphics.Bitmap getFitSampleBitmap(String fileName, String filePath, File sdPath) {
        String file_path = (filePath + fileName);
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            String search_path = (sdPath.getPath() + fileName);
            File search_file = new File(search_path);
            if (search_file.exists()) {
                file_path = search_path;
            }
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        android.graphics.Bitmap get_bm = BitmapFactory.decodeFile(file_path, options);
        int get_width = get_bm.getWidth();
        int get_height = get_bm.getHeight();
        options.inSampleSize = getFitInSampleSize(get_width, get_height, options);
        options.inJustDecodeBounds = false;
        android.graphics.Bitmap bm = BitmapFactory.decodeFile(file_path, options);
        android.graphics.Bitmap sampleImage = android.graphics.Bitmap.createScaledBitmap(bm, get_width, get_height, true);
        bm.recycle();
        return sampleImage;
    }


    //從Resources 取得Bitmap
    public static android.graphics.Bitmap getFitSampleBitmap(Resources resources, int resId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = getFitInSampleSize(width, height, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    //從輸入流取得Bitmap
    public static android.graphics.Bitmap getFitSampleBitmap(InputStream inputStream, int width, int height) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        byte[] bytes = readStream(inputStream);
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = getFitInSampleSize(width, height, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }


    public static int getFitInSampleSize(int Require_Width, int Require_Height, BitmapFactory.Options options) {
        int inSampleSize = 1;
        if (options.outWidth > Require_Width || options.outHeight > Require_Height) {
            int widthRatio = Math.round((float) options.outWidth / (float) Require_Width);
            int heightRatio = Math.round((float) options.outHeight / (float) Require_Height);
            inSampleSize = Math.min(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static float DP_From_PX(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float PX_Form_DP(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public android.graphics.Bitmap reScaleBitmap(android.graphics.Bitmap rescaleBitmap, int sizeX, int sizeY) {
        return android.graphics.Bitmap.createScaledBitmap(rescaleBitmap, sizeX, sizeY, true);
    }

    public void Recycle_Bitmap(ImageView image) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
        if (!bitmapDrawable.getBitmap().isRecycled() && bitmapDrawable.getBitmap() != null) {
            bitmapDrawable.getBitmap().recycle();
        }
        image.setImageBitmap(null);
    }

    public void Recycle_Bitmap(android.graphics.Bitmap[] bitmap) {
        for (android.graphics.Bitmap Need_Recycle_Bitmap : bitmap) {
            if (Need_Recycle_Bitmap != null && !Need_Recycle_Bitmap.isRecycled()) {
                Need_Recycle_Bitmap.recycle();
            }
        }
    }

}