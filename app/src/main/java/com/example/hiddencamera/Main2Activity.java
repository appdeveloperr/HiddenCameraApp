package com.example.hiddencamera;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main2Activity extends Activity  {

    private static final String TAG = "CameraTest";
    Camera mCamera;
    boolean mPreviewRunning = false;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Log.e(TAG, "onCreate");

        setContentView(R.layout.activity_main2);


        int i = findFrontFacingCamera();

        mCamera = Camera.open(i);
        try {
            mCamera.setPreviewTexture(new SurfaceTexture(10));
        } catch (IOException e1) {
            Log.e(TAG, e1.getMessage());
        }

        Camera.Parameters params = mCamera.getParameters();
        params.setPreviewSize(640, 480);
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        params.setPictureFormat(ImageFormat.JPEG);
        mCamera.setParameters(params);
        mCamera.startPreview();
        mCamera.takePicture(null, null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {


                // AFTER PICTURE TAKEN FUNCTION CODE
                if (data != null) {
                    // Intent mIntent = new Intent();
                    // mIntent.putExtra("image",imageData);

                    mCamera.stopPreview();
                    mPreviewRunning = false;
                    mCamera.release();

                    Toast.makeText(Main2Activity.this, "File Creating", Toast.LENGTH_SHORT).show();

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

                        Uri img = Uri.fromFile(new File(String.valueOf(f)));
                        Toast.makeText(cw,"oye ye rhi img: "+img, Toast.LENGTH_SHORT).show();

                        // remember close de FileOutput
                        fo.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // StoreByteImage(mContext, imageData, 50,"ImageName");
                    // setResult(FOTO_MODE, mIntent);
                    setResult(585);
                    finish();
                }


            }
        });
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


}
