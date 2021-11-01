package com.appindia.butterDictionary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Thesaurus {

    String thesaurusJson;

    public Thesaurus(String json) {
        thesaurusJson = json;
    }

    public void solveThesaurusJson() {

        try {

            if (thesaurusJson != null) {

                JSONObject thesaurusJsonObject = new JSONObject(thesaurusJson);

                if (!thesaurusJsonObject.isNull("id")) {
                    String id = thesaurusJsonObject.getString("id");
                }

                if (!thesaurusJsonObject.isNull("word")) {
                    String word = thesaurusJsonObject.getString("word");
                }

                if (!thesaurusJsonObject.isNull("metadata")) {

                    JSONObject metadataObject = thesaurusJsonObject.getJSONObject("metadata");

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

                if (!thesaurusJsonObject.isNull("results")) {

                    JSONArray resultsArray = thesaurusJsonObject.getJSONArray("results");

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

                                        if (!lexicalEntriesObject.isNull("entries")) {

                                            JSONArray entriesArray = lexicalEntriesObject.getJSONArray("entries");

                                            for (int ea = 0; ea < entriesArray.length(); ea++) {

                                                if (!entriesArray.isNull(ea)) {

                                                    JSONObject entriesObject = entriesArray.getJSONObject(ea);

                                                    if (!entriesObject.isNull("senses")) {

                                                        JSONArray sensesArray = entriesObject.getJSONArray("senses");

                                                        for (int sa = 0; sa < sensesArray.length(); sa++) {

                                                            if (!sensesArray.isNull(sa)) {

                                                                JSONObject sensesObject = sensesArray.getJSONObject(sa);

                                                                if (!sensesObject.isNull("id")) {
                                                                    String id = sensesObject.getString("id");
                                                                }

                                                                if (!sensesObject.isNull("antonyms")) {

                                                                    JSONArray antonymsArray = sensesObject.getJSONArray("antonyms");

                                                                    for (int aa = 0; aa < antonymsArray.length(); aa++) {

                                                                        if (!antonymsArray.isNull(aa)) {

                                                                            JSONObject antonymsObject = antonymsArray.getJSONObject(aa);

                                                                            if (!antonymsObject.isNull("language")) {
                                                                                String language = antonymsObject.getString("language");
                                                                            }

                                                                            if (!antonymsObject.isNull("text")) {
                                                                                String text = antonymsObject.getString("text");
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                if (!sensesObject.isNull("examples")) {

                                                                    JSONArray examplesArray = sensesObject.getJSONArray("examples");

                                                                    for (int exa = 0; exa < examplesArray.length(); exa++) {

                                                                        if (!examplesArray.isNull(exa)) {

                                                                            JSONObject examplesObject = examplesArray.getJSONObject(exa);

                                                                            if (!examplesObject.isNull("text")) {
                                                                                String text = examplesObject.getString("text");
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                if (!sensesObject.isNull("registers")) {

                                                                    JSONArray registersArray = sensesObject.getJSONArray("registers");

                                                                    for (int rga = 0; rga < registersArray.length(); rga++) {

                                                                        if (!registersArray.isNull(rga)) {

                                                                            JSONObject registersObject = registersArray.getJSONObject(rga);

                                                                            if (!registersObject.isNull("id")) {
                                                                                String id = registersObject.getString("id");
                                                                            }

                                                                            if (!registersObject.isNull("text")) {
                                                                                String text = registersObject.getString("text");
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                if (!sensesObject.isNull("subsenses")) {

                                                                    JSONArray subsensesArray = sensesObject.getJSONArray("subsenses");

                                                                    for (int ssa = 0; ssa < subsensesArray.length(); ssa++) {

                                                                        if (!subsensesArray.isNull(ssa)) {

                                                                            JSONObject subsensesObject = subsensesArray.getJSONObject(ssa);

                                                                            if (!subsensesObject.isNull("id")) {
                                                                                String id = subsensesObject.getString("id");
                                                                            }

                                                                            if (!subsensesObject.isNull("regions")) {

                                                                                JSONArray regionsArray = subsensesObject.getJSONArray("regions");

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

                                                                            if (!subsensesObject.isNull("registers")) {

                                                                                JSONArray registersArraySubSenses = subsensesObject.getJSONArray("registers");

                                                                                for (int rga = 0; rga < registersArraySubSenses.length(); rga++) {

                                                                                    if (!registersArraySubSenses.isNull(rga)) {

                                                                                        JSONObject registersObjectSubsenses = registersArraySubSenses.getJSONObject(rga);

                                                                                        if (!registersObjectSubsenses.isNull("id")) {
                                                                                            String id = registersObjectSubsenses.getString("id");
                                                                                        }

                                                                                        if (!registersObjectSubsenses.isNull("text")) {
                                                                                            String text = registersObjectSubsenses.getString("text");
                                                                                        }

                                                                                    }

                                                                                }

                                                                            }

                                                                            if (!subsensesObject.isNull("synonyms")) {

                                                                                JSONArray synonymsArraySubsenses = subsensesObject.getJSONArray("synonyms");

                                                                                for (int sass = 0; sass < synonymsArraySubsenses.length(); sass++) {

                                                                                    if (!synonymsArraySubsenses.isNull(sass)) {

                                                                                        JSONObject synonymsObjectSubsenses = synonymsArraySubsenses.getJSONObject(sass);

                                                                                        if (!synonymsObjectSubsenses.isNull("language")) {
                                                                                            String language = synonymsObjectSubsenses.getString("language");
                                                                                        }

                                                                                        if (!synonymsObjectSubsenses.isNull("text")) {
                                                                                            String text = synonymsObjectSubsenses.getString("text");
                                                                                        }

                                                                                    }

                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                if (!sensesObject.isNull("synonyms")) {

                                                                    JSONArray synonymsArray = sensesObject.getJSONArray("synonyms");

                                                                    for (int sna = 0; sna < synonymsArray.length(); sna++) {

                                                                        if (!synonymsArray.isNull(sna)) {

                                                                            JSONObject synonymsObject = synonymsArray.getJSONObject(sna);

                                                                            if (!synonymsObject.isNull("language")) {
                                                                                String language = synonymsObject.getString("language");
                                                                            }

                                                                            if (!synonymsObject.isNull("text")) {
                                                                                String text = synonymsObject.getString("text");
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
