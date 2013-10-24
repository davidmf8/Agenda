package com.example.agendauca;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.example.multimedia.CamaraSurfaceView;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

public class Camara extends Activity{
    private CamaraSurfaceView surface;
    private Camera miCamara;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	//Para que no salga la barra del titulo y así se ve mejor la imagen en la previsualización
    	 requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	//Nuestra "pantalla dinamica", que loque nos permitirá ver lo que la cámaraesta visualizando
    	//Pre-visualización
    	
    	setContentView(R.layout.activity_camara);
    	miCamara = getCameraInstance();
    	surface = new CamaraSurfaceView(this, miCamara);
    	
    	FrameLayout preview = (FrameLayout) findViewById(R.id.previsualizacion);
        preview.addView(surface);

        Button captureButton = (Button) findViewById(R.id.captura);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miCamara.takePicture(null, null, mPicture);
            }
        });
    }
        
    
    private static Camera getCameraInstance() {
        Camera camara = null;
        try {
            camara = Camera.open();
        } catch (Exception e) {}
        return camara;
    }
    
    PictureCallback mPicture = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date(0));
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }
}
