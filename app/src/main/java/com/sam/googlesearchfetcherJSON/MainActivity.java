package com.sam.googlesearchfetcherJSON;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etextQuery;
    private TextView tResult;
    private ProgressBar pbLoading;
    private EditText eSearchBox;
    private TextView tError;

    private final static String TAG_QUESTIONS = "related_questions";
    private final static String TAG_SNIPPETS = "snippet";

    private ArrayList<String> newLog = new ArrayList<String>();

//  private final static String TAG_QUESTIONS = "search_parameters";
//  private final static String TAG_SNIPPETS = "engine";

    JSONArray contacts = null;
    private String jsonQuestion = null;
    private String answerField = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etextQuery = (EditText) findViewById(R.id.editTextQuery);
        tResult = (TextView) findViewById(R.id.textViewResult);
        pbLoading = (ProgressBar) findViewById(R.id.pb_circular);
        eSearchBox = (EditText) findViewById(R.id.search);
        tError = (TextView) findViewById(R.id.textViewError);
        tError.setVisibility(View.INVISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if(itemThatWasClickedId == R.id.search) {
            makeSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void makeSearchQuery() {
        tResult.setText("");
        String searchQuery = etextQuery.getText().toString();
        URL googleSearchURL = NetworkUtils.buildUR(searchQuery);
        new GoogleQueryTask().execute(googleSearchURL);

    }

    private void showJSONDataView() {
        tError.setVisibility(View.INVISIBLE);

        tResult.setVisibility(View.VISIBLE);
    }

    private void ErrorMessage() {
        tError.setVisibility(View.VISIBLE);

        tResult.setVisibility(View.INVISIBLE);
    }

    private String extractInformation(String googleSearchResults) {
        try {
            //here we created JSONObject of our json file fetched from below URL
            //https://serpapi.com/search.json?engine=google&q=what%20is%20coronavirus&api_key=14fbe2c5053f205218b41cf7082fd7d2e828f5dcad563037f8d686c8834c81dd

            JSONObject jsonObject = new JSONObject(googleSearchResults);

            JSONArray snippets = jsonObject.getJSONArray(TAG_QUESTIONS);

            JSONObject firstSnippet = snippets.getJSONObject(0);

            newLog.add(firstSnippet.getString(TAG_SNIPPETS));
            jsonQuestion = newLog.get(0);

            /*
            JSONObject snippetFirst = jsonObject.getJSONObject(TAG_QUESTIONS);
            jsonQuestion = snippetFirst.getString(TAG_SNIPPETS);
             */
            //tResult.setText(jsonQuestion);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonQuestion;


    }
    public class GoogleQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLoading.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String googleSearchResults = null;

            try {
                googleSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                answerField = extractInformation(googleSearchResults);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answerField;
        }

        @Override
        protected void onPostExecute(String googleSearchResults) {
            pbLoading.setVisibility(View.INVISIBLE);

            if(googleSearchResults != null && !googleSearchResults.equals("")) {
                showJSONDataView();
                tResult.setText(googleSearchResults);
            } else
                ErrorMessage();
        }
    }
}