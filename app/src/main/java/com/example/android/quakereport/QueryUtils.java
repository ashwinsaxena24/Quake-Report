package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

/**
 * Created by agent47 on 11/2/18.
 */

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static ArrayList<Earthquake> fetchEarthquakeData(String stringUrl) {
        URL url = createUrl(stringUrl);

        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error in closing stream",e);
        }

        return extractEarthquakes(jsonResponse);
    }

    private static URL createUrl(String url) {
        URL responseUrl = null;
        try {
            responseUrl = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Cannot create URL",e);
        }
        return responseUrl;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String response = "";
        if(url == null) return response;

        HttpURLConnection urlConnection = null;
        InputStream stream = null;

        try {
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int code = urlConnection.getResponseCode();
            if(code == 200) {
                stream = urlConnection.getInputStream();
                response = readFromStream(stream);
            }
            else {
                Log.e(LOG_TAG,"Error response code: "+code);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Cannot retrieve data from URL",e);
        }
        finally {
            if(urlConnection != null) urlConnection.disconnect();
            if(stream != null) stream.close();
        }
        return response;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        if(inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            try {
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Cannot read from Stream", e);
            }
        }
        return builder.toString();
    }

    private static ArrayList<Earthquake> extractEarthquakes(String jsonEarthquake) {

        if (TextUtils.isEmpty(jsonEarthquake)) return null;

        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonEarthquake);
            JSONArray features = jsonObject.optJSONArray("features");
            for (int i=0;i<features.length();i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                long date = properties.getLong("time");
                String url = properties.getString("url");
                earthquakes.add(new Earthquake(mag,place,date,url));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }

}