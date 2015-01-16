package com.concordy.pro.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
/**
 * 
 * @author Scleo
 *
 */
public class CameraUtils {
	/** Create a file Uri for saving an image or video */ 
    private static Uri getOutputMediaFileUri(int type){ 
          return Uri.fromFile(getOutputMediaFile(type)); 
    }
    /** Create a File for saving an image or video */ 
    private static File getOutputMediaFile(int type){ 
        // To be safe, you should check that the SDCard is mounted  
        // using Environment.getExternalStorageState() before doing this.  
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory( 
                  Environment.DIRECTORY_PICTURES), "MyCameraApp"); 
        // This location works best if you want the created images to be shared  
        // between applications and persist after your app has been uninstalled.  
 
        if (! mediaStorageDir.exists()){ 
            if (! mediaStorageDir.mkdirs()){ 
                Log.d("MyCameraApp", "failed to create directory"); 
                return null; 
            } 
        } 
        // Create a media file name  
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); 
        File mediaFile = null; 
        if (type == 0){ 
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + 
            "IMG_"+ timeStamp + ".jpg"); 
        } 
        return mediaFile; 
    } 
} 

