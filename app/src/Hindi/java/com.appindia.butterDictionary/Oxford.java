package com.appindia.butterDictionary;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Oxford extends AsyncTask<String, Integer, String> {

    String meaningJson;
    String sourceLanguage = "hi";
    String targetLanguage = "hi";
    String word_id;
    String restUrl;
    static String queryType;
    static boolean networkError = false;

    @Override
    protected String doInBackground(String... strings) {

        networkError = false;

        word_id = strings[0];

        switch (strings[1]) {
            case "entry":
                restUrl = "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + sourceLanguage + "/" + word_id; //--> entries
                queryType = "entry";

                break;
            case "lemma":
                restUrl = "https://od-api.oxforddictionaries.com:443/api/v2/lemmas/" + sourceLanguage + "/" + word_id; //--> lemmas
                queryType = "entry"; //<-- after getting lemma, restUrl of endpoint entry is being used to get results of query.

                break;
            case "word":
                restUrl = "https://od-api.oxforddictionaries.com:443/api/v2/words/" + sourceLanguage + "?q=" + word_id; //--> words
                queryType = "word";

                break;
            case "translation":
                restUrl = "https://od-api.oxforddictionaries.com:443/api/v2/translations/" + sourceLanguage + "/"  + targetLanguage + "/" + word_id; //--> translations
                queryType = "translation";

                break;
        }

        Log.d("OXFORD", "doInBackground: " + restUrl);

        URL url = null;

        {
            try {
                url = new URL(restUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        HttpsURLConnection urlConnection = null;

        {
            try {
                if (url != null) {
                    urlConnection = (HttpsURLConnection) url.openConnection();
                }
                if (urlConnection != null) {

                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("app_id", "534dad72");
                    urlConnection.setRequestProperty("app_key", "a7d3a216bbdecbe0d99d7d57aad756c9");

                    //urlConnection.setRequestProperty("Accept", "application/json");
                    //urlConnection.setRequestProperty("app_id", "cc68b88d");
                    //urlConnection.setRequestProperty("app_key", "5c4b6fc361372452a073464f36992a6e");

                    //step- 3 read the output from the server
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    meaningJson = stringBuilder.toString();

                }
            } catch (IOException e) {
                e.printStackTrace();

                if (e.getMessage().equals("Unable to resolve host \"od-api.oxforddictionaries.com\": No address associated with hostname")) {
                    networkError = true;
                }

            }
        }
        return meaningJson;
    }

}
