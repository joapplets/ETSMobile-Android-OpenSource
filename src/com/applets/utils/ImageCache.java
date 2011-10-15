package com.applets.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author Philippe David
 * @version 1.0
 */
public class ImageCache {

    private HashMap<String, Bitmap> imageCache;
    private final String TAG = "Applets::ImageCache::";

    public ImageCache() {
	imageCache = new HashMap<String, Bitmap>();
    }

    /**
     * Save the bitmap to the sd Card
     * 
     * @param name
     *            Name of the image file
     * @param b
     */
    public void saveFile(String name, Drawable d) {
	try {
	    File root = Environment.getExternalStorageDirectory();
	    if (root.canWrite()) {

		Bitmap b = converToBitmap(d);

		File file = new File(root, name + ".bmp");

		FileWriter writer = new FileWriter(file);
		BufferedWriter out = new BufferedWriter(writer);

		out.close();

		imageCache.put(name, b);
	    }
	} catch (IOException e) {
	    Log.e("Applets::ImageCache::saveFile",
		    "Could not write file " + e.getMessage());
	}
    }

    /**
     * Creates a bitmap from a Drawable
     * 
     * @param d
     * @return
     */
    public Bitmap converToBitmap(Drawable d) {
	Bitmap bitmap = null;
	try {
	    bitmap = ((BitmapDrawable) d).getBitmap();
	} catch (Exception e) {
	    Log.e(TAG + "convertToBitmap", e.getMessage());
	}
	return bitmap;
    }
}
