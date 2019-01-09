package com.mobiotics.playvideoapp.presentor;

import android.os.AsyncTask;

import com.mobiotics.playvideoapp.model.Highlight;
import com.mobiotics.playvideoapp.model.repository.DBHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HighlightAsyncTask extends AsyncTask<String,String,String> {

    private List<Highlight> highlights;
    private String result;
    private final String url="https://interview-e18de.firebaseio.com/media.json?print=pretty";
    private OnSavedListener listener;

    public HighlightAsyncTask(OnSavedListener listener) {
        this.listener = listener;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... strings) {
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet httpPost = new HttpGet(url);

        HttpResponse httpResponse = null;

        try {
            httpResponse = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            result=EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Read content & Log
        return result;

    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONArray jsonArray=new JSONArray(result);
            List<Highlight> highlights=new ArrayList<>();
            for(int i=0; i < jsonArray.length(); i++) {

                JSONObject jObject = jsonArray.getJSONObject(i);

                String title = jObject.getString("title");
                String id=jObject.getString("id");
                String description = jObject.getString("description");
                String thumb=jObject.getString("thumb");
                String url=jObject.getString("url");
                Highlight highlight=new Highlight();
                highlight.setId(id);
                highlight.setDescription(description);
                highlight.setTitle(title);
                highlight.setThumb(thumb);
                highlight.setUrl(url);
                highlights.add(highlight);

            }
            DBHelper.getInstance().savehighlights(highlights);
            if (listener!=null){
                listener.onSaved();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public interface OnSavedListener{
        void onSaved();
    }
}
