package com.sam.googlesearchfetcherJSON;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String BASE_URL = "https://serpapi.com/search.json?engine=google";
    final static String PARA_QUERY = "q";

    final static String API_KEY = "api_key";
    final static String apiKey = "YOUR_API_KEY";
    /**
     *  Builds the URI used to query
     */

    public static URL buildUR(String searchQuery){
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARA_QUERY,searchQuery)
                .appendQueryParameter(API_KEY,apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {

            InputStream in = urlConnection.getInputStream();
            // we are not using here System.in  why ?
            // since the here we pass URL
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else
                return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
//https://serpapi.com/search.json?engine=google&q=what%20is%20coronavirus&api_key=API_KEY
