package com.ayushsaklani.multidoc.camera;

import android.os.Bundle;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.ayushsaklani.multidoc.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(480, 480);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        previewView = findViewById(R.id.view_frame);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);


    }

    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider) {

        previewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);

        Preview preview = new Preview.Builder()
                .setTargetResolution(DESIRED_PREVIEW_SIZE)
                .build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        cameraProvider.bindToLifecycle((LifecycleOwner)this,
                cameraSelector,preview);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraProviderFuture.addListener((Runnable) () -> {
            try{
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindImageAnalysis(cameraProvider);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(this));

    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraProviderFuture.cancel(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!cameraProviderFuture.isCancelled()){
            cameraProviderFuture.cancel(true);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}