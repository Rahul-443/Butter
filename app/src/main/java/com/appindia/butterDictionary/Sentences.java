package com.appindia.butterDictionary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sentences {

    String sentencesJson;

    public Sentences(String json) {
        sentencesJson = json;
    }

    public void solveSentencesJson() {

        try {

            if (sentencesJson != null) {

                JSONObject sentencesJsonObject = new JSONObject(sentencesJson);

                if (!sentencesJsonObject.isNull("id")) {
                    String id = sentencesJsonObject.getString("id");
                }

                if (!sentencesJsonObject.isNull("word")) {
                    String word = sentencesJsonObject.getString("word");
                }

                if (!sentencesJsonObject.isNull("metadata")) {

                    JSONObject metadataObject = sentencesJsonObject.getJSONObject("metadata");

                    if (!metadataObject.isNull("operation")) {
                        String operation = metadataObject.getString("operation");
                    }

                    if (!metadataObject.isNull("provider")) {
                        String provider = metadataObject.getString("provider");
                    }

                    if (!metadataObject.isNull("schema")) {
                        String schema = metadataObject.getString("schema");
                    }

                }

                if (!sentencesJsonObject.isNull("results")) {

                    JSONArray resultsArray = sentencesJsonObject.getJSONArray("results");

                    for (int ra = 0; ra < resultsArray.length(); ra++) {

                        if (!resultsArray.isNull(ra)) {

                            JSONObject resultsObject = resultsArray.getJSONObject(ra);

                            if (!resultsObject.isNull("id")) {
                                String id = resultsObject.getString("id");
                            }

                            if (!resultsObject.isNull("language")) {
                                String language = resultsObject.getString("language");
                            }

                            if (!resultsObject.isNull("type")) {
                                String type = resultsObject.getString("type");
                            }

                            if (!resultsObject.isNull("word")) {
                                String word = resultsObject.getString("word");
                            }

                            if (!resultsObject.isNull("lexicalEntries")) {

                                JSONArray lexicalEntriesArray = resultsObject.getJSONArray("lexicalEntries");

                                for (int lea = 0; lea < lexicalEntriesArray.length(); lea++) {

                                    if (!lexicalEntriesArray.isNull(lea)) {

                                        JSONObject lexicalEntriesObject = lexicalEntriesArray.getJSONObject(lea);

                                        if (!lexicalEntriesObject.isNull("language")) {
                                            String language = lexicalEntriesObject.getString("language");
                                        }

                                        if (!lexicalEntriesObject.isNull("text")) {
                                            String text = lexicalEntriesObject.getString("text");
                                        }

                                        if (!lexicalEntriesObject.isNull("lexicalCategory")) {

                                            JSONObject lexicalCategoryObject = lexicalEntriesObject.getJSONObject("lexicalCategory");

                                            if (!lexicalCategoryObject.isNull("id")) {
                                                String id = lexicalCategoryObject.getString("id");
                                            }

                                            if (!lexicalCategoryObject.isNull("text")) {
                                                String text = lexicalCategoryObject.getString("text");
                                            }

                                        }

                                        if (!lexicalEntriesObject.isNull("sentences")) {

                                            JSONArray sentencesArray = lexicalEntriesObject.getJSONArray("sentences");

                                            for (int sa = 0; sa < sentencesArray.length(); sa++) {

                                                if (!sentencesArray.isNull(sa)) {

                                                    JSONObject sentencesObject = sentencesArray.getJSONObject(sa);

                                                    if (!sentencesObject.isNull("regions")) {

                                                        JSONArray regionsArray = sentencesObject.getJSONArray("regions");

                                                        for (int rga = 0; rga < regionsArray.length(); rga++) {

                                                            if (!regionsArray.isNull(rga)) {

                                                                JSONObject regionsObject = regionsArray.getJSONObject(rga);

                                                                if (!regionsObject.isNull("id")) {
                                                                    String id = regionsObject.getString("id");
                                                                }

                                                                if (!regionsObject.isNull("text")) {
                                                                    String text = regionsObject.getString("text");
                                                                }

                                                            }

                                                        }

                                                    }

                                                    if (!sentencesObject.isNull("senseIds")) {

                                                        JSONArray senseIdsArray = sentencesObject.getJSONArray("senseIds");

                                                        for (int sia = 0; sia < senseIdsArray.length(); sia++) {

                                                            if (!senseIdsArray.isNull(sia)) {

                                                                String senseId = senseIdsArray.getString(sia);

                                                            }

                                                        }

                                                    }

                                                    if (!sentencesObject.isNull("text")) {
                                                        String text = sentencesObject.getString("text");
                                                    }

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
