package com.stasheasy.fos.network;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;

import com.stasheasy.fos.R;
import com.stasheasy.fos.utils.AppLog;
import com.stasheasy.fos.utils.Constants;
import com.stasheasy.fos.utils.Utilities;

import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by vibhormungee on 30/05/17.
 */

public class NetworkRequests {
    private Map<String, String> map;
    private AsyncTaskCompleteListener mAsynclistener;
    private String mode;
    private OkHttpClient httpclient;
    private Context activity;
    private AsyncHttpRequest request;
    private static final String TAG = "HttpRequester";

    public NetworkRequests(Activity activity, Map<String, String> map,
                           String mode, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.map = map;
        this.activity = activity;
        this.mode = mode;
        // is Internet Connection Available...
        if (Utilities.isOnline(activity)) {
            mAsynclistener = asyncTaskCompleteListener;
            request = (AsyncHttpRequest) new AsyncHttpRequest()
                    .executeOnExecutor(Executors.newSingleThreadExecutor());
        } else {
            Utilities.showMessage(
                    activity.getResources().getString(
                            R.string.toast_no_internet), activity);
        }
    }

    class AsyncHttpRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Response response = null;
            try {
                httpclient = new OkHttpClient();
                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add(Constants.MODE, mode);
                if(map != null && !map.isEmpty()) {
                    for (String key : map.keySet()) {
                        AppLog.Log(TAG, key + "  < === >  " + map.get(key));
                        formBuilder.add(key, map.get(key));
                    }
                }

                RequestBody formBody = formBuilder.build();
                ActivityManager manager = (ActivityManager) activity
                        .getSystemService(Context.ACTIVITY_SERVICE);

                if (manager.getMemoryClass() < 25) {
                    System.gc();
                }

                Request request = new Request.Builder().url(Constants.SERVICE_URL).post(formBody).build();
                AppLog.Log("Post request: Mode -> " + mode, request.url().toString());
                response = httpclient.newCall(request).execute();
                return response.body().string();
            }  catch (OutOfMemoryError oume) {
                System.gc();
            } catch (SocketTimeoutException ste){
                AppLog.handleException(TAG, ste);
            } catch (Exception e) {
                AppLog.handleException(TAG, e);
            }
            finally {
                if(response!=null){
                    response.body().close();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (mAsynclistener != null) {
                mAsynclistener.onTaskCompleted(response, mode);
            }
        }
    }
}
