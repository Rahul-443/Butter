package com.appindia.butterDictionary;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonToText {
    String meaningJson;
    StringBuilder meaning;
    static String word;
    Context context;
    static ArrayList<String> etymologies = new ArrayList<>();
    static ArrayList<String> languages = new ArrayList<>();
    static ArrayList<String> types = new ArrayList<>();
    static ArrayList<String> audiofiles = new ArrayList<>();
    static ArrayList<String> phoneticNotations = new ArrayList<>();
    static ArrayList<String> phoneticSpellings = new ArrayList<>();
    static ArrayList<String> dialects = new ArrayList<>();
    static ArrayList<ArrayList<String>> allDefinititions = new ArrayList<>();
    static ArrayList<ArrayList<String>> allDomainClasses = new ArrayList<>();
    static ArrayList<ArrayList<String>> allNoteTexts = new ArrayList<>();
    static ArrayList<ArrayList<String>> allNoteTypes = new ArrayList<>();
    static ArrayList<ArrayList<String>> allExamples = new ArrayList<>();
    static ArrayList<String> registersIds = new ArrayList<>();
    static ArrayList<String> registersTexts = new ArrayList<>();
    static ArrayList<String> grammaticalFeatures;
    static ArrayList<String> definitions;
    static ArrayList<String> domainClasses;
    static ArrayList<String> noteTexts;
    static ArrayList<String> noteTypes;
    static ArrayList<String> examples;
    static ArrayList<String> shortDefinitions;
    static ArrayList<String> synonyms;
    static ArrayList<String> phrases;
    static ArrayList<ArrayList<String>> allShortDefinitions = new ArrayList<>();
    static ArrayList<ArrayList<String>> allSynonyms = new ArrayList<>();
    static ArrayList<String> lexicalCategoryIds = new ArrayList<>();
    static ArrayList<String> lexicalCategoryTexts = new ArrayList<>();
    static ArrayList<ArrayList<String>> allPhrases = new ArrayList<>();
    static ArrayList<ArrayList<String>> allEtymologies = new ArrayList<>();
    static ArrayList<ArrayList<String>> allGrammaticalFeatures = new ArrayList<>();
    int lcCounts;

    JsonToText(String mj, Context context) {
        meaningJson = mj;
        this.context = context;
    }

    public String solveJson(String whichWord) {

        String id = null;
        JSONObject metadataObject = null;
        JSONArray resultsArray = null;

        lcCounts = 1;
        word = null;

        //languages.clear();
        //allDefinititions.clear();
        //allDomainClasses.clear();
        //allNoteTexts.clear();
        //allNoteTypes.clear();
        //allExamples.clear();
        //registersIds.clear();
        //registersTexts.clear();
        //allShortDefinitions.clear();
        //allSynonyms.clear();
        //lexicalCategoryIds.clear();
        //lexicalCategoryTexts.clear();
        //allPhrases.clear();
        //allEtymologies.clear();
        //allGrammaticalFeatures.clear();

        try {
            JSONObject jsonObject = new JSONObject(meaningJson);

            if (!jsonObject.isNull("id")) {
                id = jsonObject.getString("id");
            }

            if (!jsonObject.isNull("metadata")) {
                metadataObject = jsonObject.getJSONObject("metadata");
            }

            if (!jsonObject.isNull("results")) {
                resultsArray = jsonObject.getJSONArray("results");
            }

            if (!jsonObject.isNull("word")) {
                word = jsonObject.getString("word");
                //word = word.replace(word.charAt(0), Character.toUpperCase(word.charAt(0))); TODO <- replacing every character.
            }

            String operation = null;
            String provider = null;
            String schema = null;

            if (metadataObject != null) {
                operation = metadataObject.getString("operation");
                provider = metadataObject.getString("provider");
                schema = metadataObject.getString("schema");
            }

            ArrayList<String> wordMeta = new ArrayList<>();

            if (!jsonObject.isNull("operation")) {
                operation = jsonObject.getString("operation");
                wordMeta.add(id);
            }

            if (!jsonObject.isNull("provider")) {
                provider = jsonObject.getString("provider");
                wordMeta.add(word);
            }

            if (!jsonObject.isNull("schema")) {
                schema = jsonObject.getString("schema");
                wordMeta.add(provider);
            }

            if (whichWord != null) {
                meaning = new StringBuilder(whichWord);
                meaning.append("\n\n").append(word);
            } else {
                if (word != null) {
                    meaning = new StringBuilder(word);
                }
            }

            if (resultsArray != null) {

                for (int i = 0; i < resultsArray.length(); i++) {

                    if (!resultsArray.isNull(i)) {

                        JSONObject resultsObject = resultsArray.getJSONObject(i);

                        if (!resultsObject.isNull("language")) {
                            String language = resultsObject.getString("language");
                            //languages.add(language);
                            //meaning.append("\n").append(language);
                        }


                        if (!resultsObject.isNull("type")) {
                            String type = resultsObject.getString("type");
                            types.add(type);
                            meaning.append(" (").append(type).append(")").append("\n");
                        }

                        if (!resultsObject.isNull("lexicalEntries")) {

                            JSONArray lexicalEntriesArray = resultsObject.getJSONArray("lexicalEntries");

                            for (int lei = 0; lei < lexicalEntriesArray.length(); lei++) {

                                if (!lexicalEntriesArray.isNull(lei)) {

                                    JSONObject lexicalEntriesObject = lexicalEntriesArray.getJSONObject(lei);

                                    if (!lexicalEntriesObject.isNull("entries")) {

                                        JSONArray entriesArray = lexicalEntriesObject.getJSONArray("entries");

                                        for (int ea = 0; ea < entriesArray.length(); ea++) {

                                            if (!entriesArray.isNull(ea)) {

                                                JSONObject entriesObject = entriesArray.getJSONObject(ea);

                                                if (!lexicalEntriesObject.isNull("lexicalCategory")) {

                                                    JSONObject lexicalCategoryObject = lexicalEntriesObject.getJSONObject("lexicalCategory");

                                                    if (!lexicalCategoryObject.isNull("id")) {
                                                        String lexicalCategoryId = lexicalCategoryObject.getString("id");
                                                        //lexicalCategoryIds.add(lexicalCategoryId);
                                                        //meaning.append("\n").append(lexicalCategoryId);
                                                    }

                                                    if (!lexicalCategoryObject.isNull("text")) {
                                                        String lexicalCategoryText = lexicalCategoryObject.getString("text");
                                                        lexicalCategoryTexts.add(lexicalCategoryText);
                                                        meaning.append("\n").append(lcCounts).append(". ").append(lexicalCategoryText).append("\n");
                                                        lcCounts += 1;
                                                    }

                                                }

                                                if (!entriesObject.isNull("etymologies")) {

                                                    JSONArray etymologiesArray = entriesObject.getJSONArray("etymologies");

                                                    for (int eai = 0; eai < etymologiesArray.length(); eai++) {

                                                        if (!etymologiesArray.isNull(eai)) {
                                                            String etymology = etymologiesArray.getString(eai);
                                                            etymologies.add(etymology);
                                                        }

                                                    }

                                                    //allEtymologies.add(etymologies);

                                                }

                                                if (!entriesObject.isNull("grammaticalFeatures")) {

                                                    grammaticalFeatures = new ArrayList<>();
                                                    JSONArray grammaticalFeaturesArray = entriesObject.getJSONArray("grammaticalFeatures");

                                                    meaning.append("\n" + context.getResources().getString(R.string.grammaticalFeatures) + "\n\n");

                                                    for (int gfi = 0; gfi < grammaticalFeaturesArray.length(); gfi++) {

                                                        if (!grammaticalFeaturesArray.isNull(gfi)) {

                                                            JSONObject grammaticalFeatureObject = grammaticalFeaturesArray.getJSONObject(gfi);

                                                            if (!grammaticalFeatureObject.isNull("text")) {
                                                                String grammaticalFeature = grammaticalFeatureObject.getString("text");
                                                                //grammaticalFeatures.add(grammaticalFeature);
                                                                meaning.append("").append(gfi + 1).append(". ").append(grammaticalFeature).append("\n");
                                                            }

                                                        }

                                                    }

                                                    //allGrammaticalFeatures.add(grammaticalFeatures);

                                                }

                                                if (!entriesObject.isNull("pronunciations")) {

                                                    JSONArray pronunciationsArray = entriesObject.getJSONArray("pronunciations");

                                                    for (int pa = 0; pa < pronunciationsArray.length(); pa++) {

                                                        if (!pronunciationsArray.isNull(pa)) {

                                                            JSONObject pronunciationsObject = pronunciationsArray.getJSONObject(pa);

                                                            if (!pronunciationsObject.isNull("audioFile")) {

                                                                String audiofile = pronunciationsObject.getString("audioFile");
                                                                audiofiles.add(audiofile);
                                                                //meaning.append("\n").append("Audio").append("\n").append(audiofile).append("\n");
                                                            }

                                                            if (!pronunciationsObject.isNull("phoneticNotation")) {

                                                                String phoneticNotation = pronunciationsObject.getString("phoneticNotation");
                                                                //phoneticNotations.add(phoneticNotation);
                                                                //meaning.append("\n").append("Phonetic Notation").append("\n").append(phoneticNotation).append("\n");
                                                            }

                                                            if (!pronunciationsObject.isNull("phoneticSpelling")) {

                                                                String phoneticSpelling = pronunciationsObject.getString("phoneticSpelling");
                                                                phoneticSpellings.add(phoneticSpelling);
                                                                //meaning.append("\n").append("Phonetic Spelling").append("\n").append(phoneticSpelling).append("\n");
                                                            }

                                                            if (!pronunciationsObject.isNull("dialects")) {

                                                                JSONArray dialectsArray = pronunciationsObject.getJSONArray("dialects");

                                                                for (int da = 0; da < dialectsArray.length(); da++) {

                                                                    if (!dialectsArray.isNull(da)) {

                                                                        String dialect = dialectsArray.getString(da);
                                                                        dialects.add(dialect);
                                                                        //meaning.append("\n").append("Dialects").append("\n").append(dialect).append("\n");

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

                                                            if (!sensesObject.isNull("definitions")) {

                                                                JSONArray definitionsArray = sensesObject.getJSONArray("definitions");

                                                                definitions = new ArrayList<>();

                                                                meaning.append("\n").append(sa + 1).append(". ").append(context.getResources().getString(R.string.definitions) + "\n\n");

                                                                for (int di = 0; di < definitionsArray.length(); di++) {

                                                                    if (!definitionsArray.isNull(di)) {
                                                                        String definition = definitionsArray.getString(di);
                                                                        //definitions.add(definition);
                                                                        meaning.append("").append(di + 1).append(". ").append(definition).append("\n");
                                                                    }

                                                                }

                                                                //allDefinititions.add(definitions);

                                                            }

                                                            if (!sensesObject.isNull("domainClasses")) {

                                                                JSONArray domainClassesArray = sensesObject.getJSONArray("domainClasses");

                                                                domainClasses = new ArrayList<>();

                                                                meaning.append("\n").append(context.getResources().getString(R.string.domainClasses)).append("\n\n");

                                                                for (int dci = 0; dci < domainClassesArray.length(); dci++) {

                                                                    if (!domainClassesArray.isNull(dci)) {

                                                                        JSONObject domainClassesObject = domainClassesArray.getJSONObject(dci);

                                                                        if (!domainClassesObject.isNull("text")) {
                                                                            String domainClass = domainClassesObject.getString("text");
                                                                            //domainClasses.add(domainClass);
                                                                            meaning.append("").append(dci + 1).append(". ").append(domainClass).append("\n");
                                                                        }

                                                                    }

                                                                }

                                                                //allDomainClasses.add(domainClasses);

                                                            }

                                                            if (!sensesObject.isNull("examples")) {

                                                                noteTexts = new ArrayList<>();
                                                                noteTypes = new ArrayList<>();
                                                                examples = new ArrayList<>();

                                                                JSONArray examplesArray = sensesObject.getJSONArray("examples");

                                                                meaning.append("\n").append(context.getResources().getString(R.string.examples)).append("\n\n");

                                                                for (int exi = 0; exi < examplesArray.length(); exi++) {

                                                                    if (!examplesArray.isNull(exi)) {

                                                                        JSONObject examplesObject = examplesArray.getJSONObject(exi);

                                                                        if (!examplesObject.isNull("notes")) {

                                                                            JSONArray notesArray = examplesObject.getJSONArray("notes");

                                                                            for (int na = 0; na < notesArray.length(); na++) {

                                                                                if (!notesArray.isNull(na)) {

                                                                                    JSONObject notesObject = notesArray.getJSONObject(na);

                                                                                    if (!notesObject.isNull("text")) {
                                                                                        String noteText = notesObject.getString("text");
                                                                                        //noteTexts.add(noteText);
                                                                                        //meaning.append("\n").append(noteText);
                                                                                    }

                                                                                    if (!notesObject.isNull("type")) {
                                                                                        String noteType = notesObject.getString("type");
                                                                                        //noteTypes.add(noteType);
                                                                                        //meaning.append("\n").append(noteType);
                                                                                    }

                                                                                }

                                                                            }

                                                                        }

                                                                        if (!examplesObject.isNull("text")) {
                                                                            String example = examplesObject.getString("text");
                                                                            //examples.add(example);
                                                                            meaning.append("").append(exi + 1).append(". ").append(example).append("\n");
                                                                        }

                                                                    }

                                                                }

                                                                //allNoteTexts.add(noteTexts);
                                                                //allNoteTypes.add(noteTypes);
                                                                //allExamples.add(examples);

                                                            }

                                                            if (!sensesObject.isNull("registers")) {

                                                                JSONArray registersArray = sensesObject.getJSONArray("registers");

                                                                for (int ra = 0; ra < registersArray.length(); ra++) {

                                                                    if (!registersArray.isNull(ra)) {

                                                                        JSONObject registersObject = registersArray.getJSONObject(ra);

                                                                        //meaning.append("\n").append("Registers");

                                                                        if (!registersObject.isNull("id")) {
                                                                            String registersId = registersObject.getString("id");
                                                                            //registersIds.add(registersId);
                                                                            //meaning.append("\n").append(registersId);
                                                                        }

                                                                        if (!registersObject.isNull("text")) {
                                                                            String registersText = registersObject.getString("text");
                                                                            //registersTexts.add(registersText);
                                                                            //meaning.append(registersText).append("\n");
                                                                        }

                                                                    }

                                                                }

                                                            }

                                                            if (!sensesObject.isNull("shortDefinitions")) {

                                                                shortDefinitions = new ArrayList<>();

                                                                meaning.append("\n").append(context.getResources().getString(R.string.shortDefinitions)).append("\n\n");

                                                                JSONArray shortDefinitionsArray = sensesObject.getJSONArray("shortDefinitions");

                                                                for (int sdi = 0; sdi < shortDefinitionsArray.length(); sdi++) {

                                                                    if (!shortDefinitionsArray.isNull(sdi)) {
                                                                        String shortDefinition = shortDefinitionsArray.getString(sdi);
                                                                        //shortDefinitions.add(shortDefinition);
                                                                        meaning.append("").append(sdi + 1).append(". ").append(shortDefinition).append("\n");
                                                                    }

                                                                }

                                                                //allShortDefinitions.add(shortDefinitions);

                                                            }

                                                            if (!sensesObject.isNull("synonyms")) {

                                                                StringBuilder synonymBuilder = new StringBuilder();

                                                                synonyms = new ArrayList<>();

                                                                JSONArray synonymsArray = sensesObject.getJSONArray("synonyms");

                                                                meaning.append("\n").append(context.getResources().getString(R.string.synonyms)).append("\n\n");

                                                                for (int sai = 0; sai < synonymsArray.length(); sai++) {

                                                                    if (!synonymsArray.isNull(sai)) {

                                                                        JSONObject synonymsObject = synonymsArray.getJSONObject(sai);

                                                                        if (!synonymsObject.isNull("text")) {
                                                                            String synonym = synonymsObject.getString("text");
                                                                            if (sai < (synonymsArray.length() - 1)) {
                                                                                synonymBuilder.append(synonym).append(", ");
                                                                            } else {
                                                                                synonymBuilder.append(synonym).append(".");
                                                                            }
                                                                        }

                                                                    }

                                                                }
                                                                meaning.append(synonymBuilder.toString()).append("\n");
                                                                //synonyms.add(synonymBuilder.toString());
                                                                //allSynonyms.add(synonyms);

                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                        if (!lexicalEntriesObject.isNull("phrases")) {

                                            phrases = new ArrayList<>();

                                            meaning.append("\n").append(context.getResources().getString(R.string.phrases)).append("\n\n");

                                            JSONArray phrasesArray = lexicalEntriesObject.getJSONArray("phrases");

                                            for (int pai = 0; pai < phrasesArray.length(); pai++) {

                                                if (!phrasesArray.isNull(pai)) {

                                                    JSONObject phraseObject = phrasesArray.getJSONObject(pai);

                                                    if (!phraseObject.isNull("text")) {
                                                        String phrase = phraseObject.getString("text");
                                                        //phrases.add(phrase);
                                                        meaning.append("").append(pai + 1).append(". ").append(phrase).append("\n");
                                                    }

                                                }

                                            }

                                            //allPhrases.add(phrases);

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

        return meaning.toString();
    }

    public static void refreshAllLists() {
        audiofiles.clear();
        phoneticSpellings.clear();
        dialects.clear();
        lexicalCategoryTexts.clear();
        types.clear();
        etymologies.clear();
    }

}



