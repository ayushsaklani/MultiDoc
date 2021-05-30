package com.ayushsaklani.multidoc;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ayushsaklani.multidoc.camera.CameraActivity;

import org.jetbrains.annotations.NotNull;

import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String[] REQUEST_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    private static final int PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(checkAndRequestPermission()){
            initApp();
        }
    }



    private boolean checkAndRequestPermission() {
        List<String> listPermissionNeeded = new ArrayList<String>();
        for(String appPermission : REQUEST_PERMISSIONS){
            if (ContextCompat.checkSelfPermission(this,appPermission) != PackageManager.PERMISSION_GRANTED ){
                listPermissionNeeded.add(appPermission);
            }
        }
        //Ask for Non Granted Permission
        if(!listPermissionNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
            return false;
        }
        // App has all Permissions
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;

            //Gather permission grantResults
            for (int i = 0; i < grantResults.length; i++) {
                //Add only those permissions which are denied
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResult.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }

            //Check if denied count is >0
            if (deniedCount == 0) {
                initApp();
            }
            // Atlest one or all permission are denied
            else {
                for (Map.Entry<String, Integer> entry : permissionResult.entrySet()){
                    String perName =entry.getKey();
                    int permResult = entry.getValue();

                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,perName)){
                        shoeDialog("","This app needs Camera and Storage Permission to work properly",
                                "Yes, Grant Permssions",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        checkAndRequestPermission();

                                    }
                                },
                                "No, Exit App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                    }
                                ,false);
                    }
                    else{

                        //Ask user to go to setting to provide  permission
                        shoeDialog("", "Please turn on permissions at [Setting] > [Permission]",
                                "Go to Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();


                                    }
                                }, "No, Exit App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                },false);
                        break;

                    }
                }

            }
        }
    }

    private AlertDialog shoeDialog(String tile, String message, String positiveLabel, DialogInterface.OnClickListener positiveOnClick,
                                   String negativeLabel, DialogInterface.OnClickListener negativeOnClick, boolean isCancleAble) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(tile)
                .setCancelable(isCancleAble)
                .setMessage(message)
                .setPositiveButton(positiveLabel,positiveOnClick)
                .setNegativeButton(negativeLabel,negativeOnClick);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    private void initApp() {
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivity(cameraIntent);
        finish();
    }


}