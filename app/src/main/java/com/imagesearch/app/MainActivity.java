package com.imagesearch.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private final int PAGE_SIZE = 8;

    private final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=" + PAGE_SIZE;

//    private ArrayList<GoogleImage> listImages;
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private Button searchButton;
    private EditText searchText;
    public LruCache<String, Bitmap> imageCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.grid_view);
        searchButton = (Button) findViewById(R.id.search_button);
        searchText = (EditText) findViewById(R.id.search_input);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(searchText.getText().toString());
            }
        });
    }

    protected void search(String query) {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        try {
            perform(BASE_URL + "&q=" + URLEncoder.encode(query, "UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
    }

    protected void perform(String query) {
        new GetImagesTask().execute(query);
    }

    private class GetImagesTask extends AsyncTask<String, Void, Void> {

        JSONObject jsonObj;

        @Override
        protected Void doInBackground(String... params) {
            String query = params[0];
            Log.i(TAG, "Query in async task is " + query);
            URL url;
            try {
                url = new URL(query);
                URLConnection connection = url.openConnection();

                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                Log.i(TAG, "Builder string is " + builder.toString());

                jsonObj = new JSONObject(builder.toString());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                JSONObject responseObject = jsonObj.getJSONObject("responseData");
                JSONArray resultArray = responseObject.getJSONArray("results");
                JSONObject cursor = responseObject.getJSONObject("cursor");
                String moreUrl = cursor.getString("moreResultsUrl");
                Log.i(TAG, "More url is " + moreUrl);

                new DownloadImageTask().execute(resultArray);
                System.out.println("Result array length is: " + resultArray.length());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<JSONArray, Void, ArrayList<GoogleImage>> {

        protected ArrayList<GoogleImage> doInBackground(JSONArray... urls) {

            ArrayList<GoogleImage> listImages = new ArrayList<GoogleImage>();
            GoogleImage image;
            JSONArray resultArray = urls[0];
            try {
                for(int i = 0; i < resultArray.length(); i++){
                    JSONObject obj;
                    obj = resultArray.getJSONObject(i);
                    image = new GoogleImage();

                    image.setTitle(obj.getString("title"));

                    String tbUrl = obj.getString("tbUrl");
                    image.setThumbUrl(tbUrl);
                    image.setActualUrl(obj.getString("url"));

                    Log.i(TAG, "Thumb URL is " + obj.getString("tbUrl"));

                    listImages.add(image);

                    Bitmap mIcon = null;
                    try {
                        InputStream in = new java.net.URL(tbUrl).openStream();
                        mIcon = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                    imageCache.put(tbUrl, mIcon);

                }
                return listImages;
            } catch (JSONException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(ArrayList<GoogleImage> result) {
            setGridViewAdapter(result);
        }
    }

    public void setGridViewAdapter(ArrayList<GoogleImage> images) {
        adapter = new GridViewImageAdapter(this, images);
        gridView.setAdapter(adapter);
    }
}
