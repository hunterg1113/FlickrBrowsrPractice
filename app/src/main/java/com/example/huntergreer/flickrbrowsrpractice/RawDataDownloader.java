package com.example.huntergreer.flickrbrowsrpractice;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {PROCESSING, IDLE, FAILED_OR_EMPTY, OK}

public class RawDataDownloader extends AsyncTask<String, Void, String> {
    private static final String TAG = "RawDataDownloader";

    private DownloadStatus mStatus;
    private final OnDownloadComplete mCallBack;

    interface OnDownloadComplete {
        void onDownloadComplete(String s, DownloadStatus status);
    }

    RawDataDownloader(OnDownloadComplete callBack) {
        mCallBack = callBack;
        mStatus = DownloadStatus.IDLE;
    }

    void runInSameThread(String s) {
        onPostExecute(doInBackground(s));
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        try {
            mStatus = DownloadStatus.PROCESSING;

            URL url = new URL(strings[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            StringBuilder result = new StringBuilder();
            String line;
            for (line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }
            mStatus = DownloadStatus.OK;
            return result.toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL... \n" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception opening URL connection... \n" + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security Exception, needs permission?... \n" + e.getMessage());
        } finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: IO Exception closing BufferedReader... \n" + e.getMessage());
                }
            }
        }
        mStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null && mCallBack != null && mStatus == DownloadStatus.OK) mCallBack.onDownloadComplete(s, mStatus);
    }
}
