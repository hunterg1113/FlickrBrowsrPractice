package com.example.huntergreer.flickrbrowsrpractice;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FlickrJsonParser extends AsyncTask<String, Void, List<Photo>> implements RawDataDownloader.OnDownloadComplete {
    private static final String TAG = "FlickrJsonParser";
    private static final String FORMAT = "json";
    private static final String JSON_CALLBACK = "1";

    private String mBaseUrl;
    private String mLang;
    private boolean mMatchAll;

    private List<Photo> mPhotosList;
    private DownloadStatus mStatus;

    private final OnDataAvailable mCallBack;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> photoList);
    }

    FlickrJsonParser(String baseUrl, String lang, boolean matchAll, OnDataAvailable callBack) {
        mBaseUrl = baseUrl;
        mLang = lang;
        mMatchAll = matchAll;
        mCallBack = callBack;
    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        String destinationUrl = createUri(strings[0]);
        RawDataDownloader rawDataDownloader = new RawDataDownloader(this);
        rawDataDownloader.runInSameThread(destinationUrl);
        return mPhotosList;
    }

    private String createUri(String searchCriteria) {
        return Uri.parse(mBaseUrl).buildUpon().appendQueryParameter("format", FORMAT)
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", mMatchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", mLang)
                .appendQueryParameter("nojsoncallback", JSON_CALLBACK).build().toString();
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        if (mStatus == DownloadStatus.OK) mCallBack.onDataAvailable(photos);
    }

    @Override
    public void onDownloadComplete(String s, DownloadStatus status) {
        mPhotosList = new ArrayList<>();
        mStatus = status;
        try {
            JSONObject jsonData = new JSONObject(s);
            JSONArray jsonArray = jsonData.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String dateTaken = jsonObject.getString("date_taken");
                String description = jsonObject.getString("description");
                String author = jsonObject.getString("author");
                String tags = jsonObject.getString("tags");
                String image = jsonObject.getJSONObject("media").getString("m").replaceFirst("_m.", "_b.");
                Photo photo = new Photo(title, image, dateTaken, description, author, tags);
                mPhotosList.add(photo);
            }
        } catch (JSONException e) {
            Log.e(TAG, "onDownloadComplete: JSON Exception... \n" + e.getMessage());
            mStatus = DownloadStatus.FAILED_OR_EMPTY;
        }
    }
}
