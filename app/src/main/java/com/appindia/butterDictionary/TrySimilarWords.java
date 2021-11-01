package com.appindia.butterDictionary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class TrySimilarWords {

    String entry = "entry";
    String lemma = "lemma";

    public String getlemmas(String searchedWord) {

        String rootWord = null;
        String meaningJson = null;

        try {
            String lemmaJson = new Oxford().execute(searchedWord, lemma).get();
            if (lemmaJson != null) {
                JSONObject lemmaObject = new JSONObject(lemmaJson);
                if (!lemmaObject.isNull("results")) {
                    JSONArray lemmaArray = lemmaObject.getJSONArray("results");
                    for (int i = 0; i < lemmaArray.length(); i++) {
                        JSONObject LEObject = lemmaArray.getJSONObject(i);
                        if (!LEObject.isNull("lexicalEntries")) {
                            JSONArray LEArray = LEObject.getJSONArray("lexicalEntries");
                            for (int i1 = 0; i1 < LEArray.length(); i1++) {
                                JSONObject inflectionObject = LEArray.getJSONObject(i1);
                                if (!inflectionObject.isNull("inflectionOf")) {
                                    JSONArray inflectionArray = inflectionObject.getJSONArray("inflectionOf");
                                    for (int i2 = 0; i2 < inflectionArray.length(); i2++) {
                                        JSONObject textObject = inflectionArray.getJSONObject(i2);
                                        rootWord = textObject.getString("text");
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (rootWord != null) {
                meaningJson = new Oxford().execute(rootWord, entry).get();
            }

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

        return meaningJson;
    }

}
