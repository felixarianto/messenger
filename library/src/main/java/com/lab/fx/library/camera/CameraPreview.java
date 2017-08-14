package com.lab.fx.library.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

/**
 * Created by febri on 09/08/17.
 */

public class CameraPreview extends TextureView implements TextureView.SurfaceTextureListener {

    private static String TAG ="CameraPreview";
    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int mCameraId;
    private Camera mCamera;
    public void create(int p_cameraid) {
        mCameraId = p_cameraid;
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            mCamera = Camera.open(mCameraId);
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    int rotate = 90;
    public void addRotation() {
        mCamera.stopPreview();
        Camera.Parameters parameter = mCamera.getParameters();
        parameter.setRotation(rotate);
        mCamera.setParameters(parameter);
        mCamera.startPreview();
        requestLayout();
        invalidate();
        rotate += 90;
    }

}
