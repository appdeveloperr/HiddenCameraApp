package com.example.hiddencamera;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    Camera mCamera;
    Camera camera;
    private boolean safeToTakePicture = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button capImgBt = (Button)findViewById(R.id.CapImgBt);

        capImgBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent camclass = new Intent(MainActivity.this,Main2Activity.class);
                camclass.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(camclass);
                finish();

               /* startService(new Intent(getApplicationContext(), cameraservice.class));
                Toast.makeText(MainActivity.this, "started", Toast.LENGTH_SHORT).show();*/
            }
        });

    }



}
