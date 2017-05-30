package com.stasheasy.fos.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.stasheasy.fos.R;

/**
 * Created by vibhormungee on 30/05/17.
 */

public class Utilities {

    private static ProgressDialog progressDialog;

    public static boolean isOnline(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        // if running on emulator return true always.
        return android.os.Build.MODEL.equals("google_sdk");
    }

    public static void showMessage(String message, Activity activity){
        int version = Build.VERSION.SDK_INT;
        if(version<=android.os.Build.VERSION_CODES.LOLLIPOP) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(activity.findViewById(android.R.id.content),message,Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void showProgressDialog(Activity activity){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(activity.getResources().getString(R.string.loading_message));
        if(progressDialog.isShowing()){
            progressDialog.hide();
        }
        progressDialog.show();
    }

    public static void showProgressDialog(Activity activity,String message){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        if(progressDialog.isShowing()){
            progressDialog.hide();
        }
        progressDialog.show();
    }

    public static void hideProgressDialog(Activity activity){
        if(progressDialog.isShowing()){
            progressDialog.hide();
        }
    }
}
