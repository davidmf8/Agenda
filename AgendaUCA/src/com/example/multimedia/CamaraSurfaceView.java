package com.example.multimedia;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class CamaraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    SurfaceHolder miholder;
    Camera miCamara;
	
	
	@SuppressWarnings("deprecation")
	public CamaraSurfaceView(Context context, Camera miCamara) {
		super(context);
		this.miCamara = miCamara;
		miholder = this.getHolder();
		miholder.addCallback(this);
		miholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		if(holder.getSurface() == null)
			return;
		
		 try {
	        miCamara.stopPreview();
	     } catch (Exception e){}

		
		try {
            miCamara.setPreviewDisplay(holder);
            miCamara.startPreview();

        } catch (Exception e){}

		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        try {
           miCamara.setPreviewDisplay(holder);
           miCamara.startPreview();
        } catch (IOException exception) { }
	}
    
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		miCamara.stopPreview();
		miCamara.release();
		
	}

}