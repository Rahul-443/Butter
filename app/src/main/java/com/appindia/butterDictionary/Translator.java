package com.appindia.butterDictionary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Translator {

    String wordId;
    String jsonToTranslate;
    StringBuilder translationText;
    int lexCatCount = 0;
    ArrayList<String> lexicalCategories = new ArrayList<>();
    ArrayList<String> audioFiles = new ArrayList<>();
    ArrayList<String> phoneticNotations = new ArrayList<>();
    ArrayList<String> phoneticSpellings = new ArrayList<>();
    ArrayList<String> dialects = new ArrayList<>();
    ArrayList<String> notesTexts = new ArrayList<>();
    ArrayList<String> notesTypes = new ArrayList<>();
    ArrayList<String> translations = new ArrayList<>();

    public Translator(String wordId) {
        this.wordId = wordId;
    }

    public String translate() {

        audioFiles.clear();
        phoneticNotations.clear();
        phoneticSpellings.clear();
        dialects.clear();
        lexicalCategories.clear();
        notesTexts.clear();
        notesTypes.clear();
        translations.clear();

        translationText = new StringBuilder();

        try {

            jsonToTranslate = new Oxford().execute(wordId, "translation").get();

            if (jsonToTranslate != null) {

                JSONObject jsonToTranslateObject = new JSONObject(jsonToTranslate);

                if (!jsonToTranslateObject.isNull("id")) {
                    String id = jsonToTranslateObject.getString("id");
                }

                if (!jsonToTranslateObject.isNull("word")) {
                    String word = jsonToTranslateObject.getString("word");
                    translationText.append(word).append("\n\n");
                }

                if (!jsonToTranslateObject.isNull("metadata")) {

                    JSONObject metadataObject = jsonToTranslateObject.getJSONObject("metadata");

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

                if (!jsonToTranslateObject.isNull("results")) {

                    JSONArray resultsArray = jsonToTranslateObject.getJSONArray("results");

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

                                        if (!lexicalEntriesObject.isNull("lexicalCategories")) {

                                            JSONObject lexicalCategoriesObject = lexicalEntriesObject.getJSONObject("lexicalCategories");

                                            if (!lexicalCategoriesObject.isNull("id")) {
                                                String id = lexicalCategoriesObject.getString("id");
                                            }

                                            if (!lexicalCategoriesObject.isNull("text")) {
                                                String text = lexicalCategoriesObject.getString("text");
                                                lexicalCategories.add(text);
                                                translationText.append(lexCatCount++).append(". ").append(text).append("\n\n");
                                            }

                                        }

                                        if (!lexicalEntriesObject.isNull("entries")) {

                                            JSONArray entriesArray = lexicalEntriesObject.getJSONArray("entries");

                                            for (int ea = 0; ea < entriesArray.length(); ea++) {

                                                if (!entriesArray.isNull(ea)) {

                                                    JSONObject entriesObject = entriesArray.getJSONObject(lea);

                                                    if (!entriesObject.isNull("pronunciations")) {

                                                        JSONArray pronunciationsArray = entriesObject.getJSONArray("pronunciations");

                                                        for (int pa = 0; pa < pronunciationsArray.length(); pa++) {

                                                            if (!pronunciationsArray.isNull(pa)) {

                                                                JSONObject pronunciationsObject = pronunciationsArray.getJSONObject(pa);

                                                                if (!pronunciationsObject.isNull("audioFile")) {
                                                                    String audioFile = pronunciationsObject.getString("audioFile");
                                                                    audioFiles.add(audioFile);
                                                                }

                                                                if (!pronunciationsObject.isNull("phoneticNotation")) {
                                                                    String phoneticNotation = pronunciationsObject.getString("phoneticNotation");
                                                                    phoneticNotations.add(phoneticNotation);
                                                                }

                                                                if (!pronunciationsObject.isNull("phoneticSpelling")) {
                                                                    String phoneticSpelling = pronunciationsObject.getString("phoneticSpelling");
                                                                    phoneticSpellings.add(phoneticSpelling);
                                                                }

                                                                if (!pronunciationsObject.isNull("dialects")) {

                                                                    JSONArray dialectsArray = pronunciationsObject.getJSONArray("dialects");

                                                                    for (int da = 0; da < dialectsArray.length(); da++) {

                                                                        if (!dialectsArray.isNull(da)) {
                                                                            String dialect = dialectsArray.getString(da);
                                                                            dialects.add(dialect);
                                                                        }

                                                                    }

                                                                }

                                                            }

                                                        }

                                                    }

                                                    if (!entriesObject.isNull("senses")) {

                                                        JSONArray sensesArray = entriesObject.getJSONArray("senses");

                                                        for (int sa = 0; sa < sensesArray.length(); sa++) {

                                                            if (!sensesArray.isNull(sa)) {

                                                                JSONObject sensesObject = sensesArray.getJSONObject(sa);

                                                                if (!sensesObject.isNull("id")) {
                                                                    String id = sensesObject.getString("id");
                                                                }

                                                                if (!sensesObject.isNull("notes")) {

                                                                    JSONArray notesArray = sensesObject.getJSONArray("notes");

                                                                    for (int na = 0; na < notesArray.length(); na++) {

                                                                        if (!notesArray.isNull(na)) {

                                                                            JSONObject notesObject = notesArray.getJSONObject(na);

                                                                            if (!notesObject.isNull("text")) {
                                                                                String text = notesObject.getString("text");
                                                                                notesTexts.add(text);
                                                                            }

                                                                            if (!notesObject.isNull("type")) {
                                                                                String type = notesObject.getString("type");
                                                                                notesTypes.add(type);
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                if (!sensesObject.isNull("translations")) {

                                                                    JSONArray translationsArray = sensesObject.getJSONArray("translations");
                                                                    translationText.append("Translations").append("\n\n");

                                                                    for (int ta = 0; ta < translationsArray.length(); ta++) {

                                                                        if (!translationsArray.isNull(ta)) {

                                                                            JSONObject translationsObject = translationsArray.getJSONObject(ta);

                                                                            if (!translationsObject.isNull("language")) {
                                                                                String language = translationsObject.getString("language");
                                                                            }

                                                                            if (!translationsObject.isNull("text")) {
                                                                                String text = translationsObject.getString("text");
                                                                                translations.add(text);
                                                                                translationText.append(ta).append(". ").append(text).append("\n");
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return translationText.toString();
    }

}
