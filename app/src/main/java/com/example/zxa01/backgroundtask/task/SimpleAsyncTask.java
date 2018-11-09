package com.example.zxa01.backgroundtask.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.example.zxa01.backgroundtask.entity.Book;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class SimpleAsyncTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private RecyclerView recyclerView;
    private SimpleAdapter simpleAdapter;

    private ArrayList<Book> books = new ArrayList<>();
    private String param;

    public SimpleAsyncTask(String param, RecyclerView recyclerView, SimpleAdapter simpleAdapter, Context context) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.simpleAdapter = simpleAdapter;
        this.param = param;
    }

    @Override
    public String doInBackground(Void... voids) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;

        try {
            // API
            final String API = "https://www.googleapis.com/books/v1/volumes?";
            final String QUERY_PARAM = "q"; // Parameter for the search string.
            final String MAX_RESULTS = "maxResults"; // Parameter that limits search results.
            final String PRINT_TYPE = "printType"; // Parameter to filter by print type.
            Uri builtURI = Uri.parse(API).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, param)
                    .appendQueryParameter(MAX_RESULTS, "5")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();
            URL requestURL = new URL(builtURI.toString());
            // Open connection
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            // Read the response
            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                builder.append(line + "\n");
            }
            if (builder.length() == 0) return null;
            response = builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    public void onPostExecute(String response) {
        super.onPostExecute(response);
        try {
            // response 轉換成 json
            JSONObject jsonObject = new JSONObject(response);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            int i = 0;
            StringBuffer authorStr;
            while (i < itemsArray.length()) {
                JSONObject jsonObj = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = jsonObj.getJSONObject("volumeInfo");

                JSONArray authorList = volumeInfo.getJSONArray("authors");
                authorStr = new StringBuffer();
                for (int index = 0; index < authorList.length(); index++) {
                    authorStr.append(authorList.getString(index));
                    authorStr.append(" ");
                }
                try {
                    books.add(new Book(volumeInfo.getString("title"), authorStr.toString(), volumeInfo.getString("printType")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // RecyclerView setting
        simpleAdapter = new SimpleAdapter(context, books);
        recyclerView.setAdapter(simpleAdapter);
    }

}