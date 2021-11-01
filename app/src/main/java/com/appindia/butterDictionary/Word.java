package com.appindia.butterDictionary;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Word {

    String wordId;
    static String query = null;
    String wordJson;
    StringBuilder wordResultsString;
    int lexCatCount;
    Context context;
    static ArrayList<String> metadata = new ArrayList<>();
    static ArrayList<String> wordTypeList = new ArrayList<>();
    static ArrayList<String> derivativesList = new ArrayList<>();
    static ArrayList<String> lexicalCategoryList = new ArrayList<>();
    static ArrayList<String> etymologiesList = new ArrayList<>();
    static ArrayList<String> grammaticalFeaturesText = new ArrayList<>();
    static ArrayList<String> grammaticalFeaturesType = new ArrayList<>();
    static ArrayList<String> inflectedFormList = new ArrayList<>();
    static ArrayList<String> grammaticalFeaturesInflectedFormTextList = new ArrayList<>();
    static ArrayList<String> grammaticalFeaturesInflectedFormTypeList = new ArrayList<>();
    static ArrayList<String> notesTextList = new ArrayList<>();
    static ArrayList<String> notesTypeList = new ArrayList<>();
    static ArrayList<String> audioFileList = new ArrayList<>();
    static ArrayList<String> phoneticNotationList = new ArrayList<>();
    static ArrayList<String> phoneticSpellingList = new ArrayList<>();
    static ArrayList<String> dialectsList = new ArrayList<>();
    static ArrayList<String> sensesIdList = new ArrayList<>();
    static ArrayList<String> definitionsList = new ArrayList<>();
    static ArrayList<String> domainClassesList = new ArrayList<>();
    static ArrayList<String> examplesList = new ArrayList<>();
    static ArrayList<String> semanticClassesList = new ArrayList<>();
    static ArrayList<String> shortDefinitionsList = new ArrayList<>();
    static ArrayList<String> idSubsensesList = new ArrayList<>();
    static ArrayList<String> definitionsSubsensesList = new ArrayList<>();
    static ArrayList<String> domainClassesSubsensesList = new ArrayList<>();
    static ArrayList<String> examplesSubsensesList = new ArrayList<>();
    static ArrayList<String> notesTextSubsensesList = new ArrayList<>();
    static ArrayList<String> notesTypeSubsensesList = new ArrayList<>();
    static ArrayList<String> shortDefinitionsSubsensesList = new ArrayList<>();
    static ArrayList<String> synonymsList = new ArrayList<>();
    static ArrayList<String> thesaurusLinksEntryIdList = new ArrayList<>();
    static ArrayList<String> thesaurusLinksSenseIdList = new ArrayList<>();
    static ArrayList<String> phrasesList = new ArrayList<>();

    public Word(String wordId, Context context) {
        this.wordId = wordId;
        this.context = context;
    }

    public String solveJson() {

        wordResultsString = new StringBuilder();
        lexCatCount = 0;

        try {

            wordJson = new Oxford().execute(wordId, "word").get();

            Log.d("GET DEFINITION", "getText: " + wordJson);

            if (wordJson != null) {

                JSONObject wordObject = new JSONObject(wordJson);

                if (!wordObject.isNull("metadata")) {

                    JSONObject metadataObject = wordObject.getJSONObject("metadata");

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

                if (!wordObject.isNull("query")) {
                    query = wordObject.getString("query");
                    wordResultsString.append(query);
                }

                if (!wordObject.isNull("results")) {

                    JSONArray resultsArray = wordObject.getJSONArray("results");

                    for (int i = 0; i < resultsArray.length(); i++) {

                        if (!resultsArray.isNull(i)) {

                            JSONObject resultsObject = resultsArray.getJSONObject(i);

                            if (resultsObject != null) {

                                if (!resultsObject.isNull("id")) {
                                    String id = resultsObject.getString("id");
                                }

                                if (!resultsObject.isNull("language")) {
                                    String language = resultsObject.getString("language");
                                }

                                if (!resultsObject.isNull("type")) {
                                    String type = resultsObject.getString("type");
                                    wordTypeList.add(type);
                                    wordResultsString.append(" ").append("(").append(type).append(")").append("\n\n");
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

                                            if (!lexicalEntriesObject.isNull("derivatives")) {

                                                JSONArray derivativesArray = lexicalEntriesObject.getJSONArray("derivatives");
                                                //wordResultsString.append("Derivatives").append("\n\n");

                                                for (int da = 0; da < derivativesArray.length(); da++) {

                                                    if (!derivativesArray.isNull(da)) {

                                                        JSONObject derivativesObject = derivativesArray.getJSONObject(da);

                                                        if (!derivativesObject.isNull("id")) {
                                                            String id = derivativesObject.getString("id");
                                                        }

                                                        if (!derivativesObject.isNull("text")) {
                                                            String text = derivativesObject.getString("text");
                                                            //wordResultsString.append(da+ 1).append(". ").append(text).append("\n");
                                                        }

                                                    }

                                                }

                                            }

                                            if (!lexicalEntriesObject.isNull("lexicalCategory")) {

                                                JSONObject lexicalCategoryObject = lexicalEntriesObject.getJSONObject("lexicalCategory");

                                                if (!lexicalCategoryObject.isNull("id")) {
                                                    String id = lexicalCategoryObject.getString("id");
                                                }

                                                if (!lexicalCategoryObject.isNull("text")) {
                                                    String text = lexicalCategoryObject.getString("text");
                                                    lexicalCategoryList.add(text);
                                                    lexCatCount = lexCatCount + 1;
                                                    wordResultsString.append(lexCatCount).append(". ").append(text).append("\n");
                                                }

                                            }

                                            if (!lexicalEntriesObject.isNull("entries")) {

                                                JSONArray entriesArray = lexicalEntriesObject.getJSONArray("entries");

                                                for (int ea = 0; ea < entriesArray.length(); ea++) {

                                                    if (!entriesArray.isNull(ea)) {

                                                        JSONObject entriesObject = entriesArray.getJSONObject(ea);

                                                        if (!entriesObject.isNull("etymologies")) {

                                                            JSONArray etymologiesArray = entriesObject.getJSONArray("etymologies");

                                                            for (int eta = 0; eta < etymologiesArray.length(); eta++) {

                                                                if (!etymologiesArray.isNull(eta)) {

                                                                    String etymology = etymologiesArray.getString(eta);
                                                                    etymologiesList.add(etymology);

                                                                }

                                                            }

                                                        }

                                                        if (!entriesObject.isNull("grammaticalFeatures")) {

                                                            JSONArray grammaticalFeaturesArray = entriesObject.getJSONArray("grammaticalFeatures");
                                                            //wordResultsString.append("Grammatical Features").append("\n\n");

                                                            for (int gfa = 0; gfa < grammaticalFeaturesArray.length(); gfa++) {

                                                                if (!grammaticalFeaturesArray.isNull(gfa)) {

                                                                    JSONObject grammaticalFeaturesObject = grammaticalFeaturesArray.getJSONObject(gfa);

                                                                    if (!grammaticalFeaturesObject.isNull("id")) {
                                                                        String id = grammaticalFeaturesObject.getString("id");
                                                                    }

                                                                    if (!grammaticalFeaturesObject.isNull("text")) {
                                                                        String text = grammaticalFeaturesObject.getString("text");
                                                                        //wordResultsString.append(gfa++).append(". ").append(text).append("\n");
                                                                    }

                                                                    if (!grammaticalFeaturesObject.isNull("type")) {
                                                                        String type = grammaticalFeaturesObject.getString("type");
                                                                    }

                                                                }

                                                            }

                                                        }

                                                        if (!entriesObject.isNull("inflections")) {

                                                            JSONArray inflectionsArray = entriesObject.getJSONArray("inflections");

                                                            for (int ia = 0; ia < inflectionsArray.length(); ia++) {

                                                                if (!inflectionsArray.isNull(ia)) {

                                                                    JSONObject inflectionsObject = inflectionsArray.getJSONObject(ia);

                                                                    String inflectedForm = null;
                                                                    if (!inflectionsObject.isNull("inflectedForm")) {
                                                                        inflectedForm = inflectionsObject.getString("inflectedForm");
                                                                        //wordResultsString.append(ia + 1).append(". ").append(inflectedForm).append("\n");
                                                                    }

                                                                    if (!inflectionsObject.isNull("grammaticalFeatures")) {

                                                                        JSONArray grammaticalFeaturesArray = inflectionsObject.getJSONArray("grammaticalFeatures");
                                                                        //wordResultsString.append("\n").append("Grammatical Features of Inflected form").append("\n\n");

                                                                        if (inflectedForm != null && inflectedForm.equals(query)) {
                                                                            wordResultsString.append("\n").append(context.getResources().getString(R.string.grammaticalFeatures)).append("\n\n");
                                                                        }

                                                                        for (int gfa = 0; gfa < grammaticalFeaturesArray.length(); gfa++) {

                                                                            if (!grammaticalFeaturesArray.isNull(gfa)) {

                                                                                JSONObject grammaticalFeaturesObject = grammaticalFeaturesArray.getJSONObject(gfa);

                                                                                if (!grammaticalFeaturesObject.isNull("id")) {
                                                                                    String id = grammaticalFeaturesObject.getString("id");
                                                                                }

                                                                                String text = null;
                                                                                if (!grammaticalFeaturesObject.isNull("text")) {
                                                                                    text = grammaticalFeaturesObject.getString("text");
                                                                                }

                                                                                String type = null;
                                                                                if (!grammaticalFeaturesObject.isNull("type")) {
                                                                                    type = grammaticalFeaturesObject.getString("type");
                                                                                }

                                                                                if (inflectedForm != null && inflectedForm.equals(query) && text != null && type != null) {
                                                                                    wordResultsString.append("").append(gfa + 1).append(". ").append(text).append(" ").append(type).append(": ").append(query).append("\n");
                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                    if (!inflectionsObject.isNull("pronunciations") && inflectedForm.equals(query)) {

                                                                        JSONArray pronunciationsArray = inflectionsObject.getJSONArray("pronunciations");

                                                                        for (int pa = 0; pa < pronunciationsArray.length(); pa++) {

                                                                            if (!pronunciationsArray.isNull(pa)) {

                                                                                JSONObject pronunciationsObject = pronunciationsArray.getJSONObject(pa);

                                                                                if (!pronunciationsObject.isNull("audioFile")) {
                                                                                    String audioFile = pronunciationsObject.getString("audioFile");
                                                                                    audioFileList.add(audioFile);
                                                                                }

                                                                                if (!pronunciationsObject.isNull("phoneticNotation")) {
                                                                                    String phoneticNotation = pronunciationsObject.getString("phoneticNotation");
                                                                                    //phoneticNotationList.add(phoneticNotation);
                                                                                }

                                                                                if (!pronunciationsObject.isNull("phoneticSpelling")) {
                                                                                    String phoneticSpelling = pronunciationsObject.getString("phoneticSpelling");
                                                                                    phoneticSpellingList.add(phoneticSpelling);
                                                                                }

                                                                                if (!pronunciationsObject.isNull("dialects")) {

                                                                                    JSONArray dialectsArray = pronunciationsObject.getJSONArray("dialects");

                                                                                    for (int da = 0; da < dialectsArray.length(); da++) {

                                                                                        if (!dialectsArray.isNull(da)) {
                                                                                            String dialect = dialectsArray.getString(da);
                                                                                            dialectsList.add(dialect);
                                                                                        }

                                                                                    }

                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                            }

                                                        }

                                                        if (!entriesObject.isNull("notes")) {

                                                            JSONArray notesArray = entriesObject.getJSONArray("notes");
                                                            wordResultsString.append("\n").append(context.getResources().getString(R.string.notes)).append("\n\n");

                                                            for (int na = 0; na < notesArray.length(); na++) {

                                                                if (!notesArray.isNull(na)) {

                                                                    JSONObject notesObject = notesArray.getJSONObject(na);

                                                                    if (!notesObject.isNull("text")) {
                                                                        String text = notesObject.getString("text");
                                                                        wordResultsString.append(na + 1).append(". ").append(text).append("\n");
                                                                    }

                                                                    if (!notesObject.isNull("type")) {
                                                                        String type = notesObject.getString("type");
                                                                    }

                                                                }

                                                            }

                                                        }

                                                        if (!entriesObject.isNull("pronunciations")) {

                                                            JSONArray pronunciationsArray = entriesObject.getJSONArray("pronunciations");

                                                            for (int pa = 0; pa < pronunciationsArray.length(); pa++) {

                                                                if (!pronunciationsArray.isNull(pa)) {

                                                                    JSONObject pronunciationsObject = pronunciationsArray.getJSONObject(pa);

                                                                    if (!pronunciationsObject.isNull("audioFile")) {
                                                                        String audioFile = pronunciationsObject.getString("audioFile");
                                                                        //audioFileList.add(audioFile);
                                                                    }

                                                                    if (!pronunciationsObject.isNull("phoneticNotation")) {
                                                                        String phoneticNotation = pronunciationsObject.getString("phoneticNotation");
                                                                        //phoneticNotationList.add(phoneticNotation);
                                                                    }

                                                                    if (!pronunciationsObject.isNull("phoneticSpelling")) {
                                                                        String phoneticSpelling = pronunciationsObject.getString("phoneticSpelling");
                                                                        //phoneticSpellingList.add(phoneticSpelling);
                                                                    }

                                                                    if (!pronunciationsObject.isNull("dialects")) {

                                                                        JSONArray dialectsArray = pronunciationsObject.getJSONArray("dialects");

                                                                        for (int da = 0; da < dialectsArray.length(); da++) {

                                                                            if (!dialectsArray.isNull(da)) {
                                                                                String dialect = dialectsArray.getString(da);
                                                                                //dialectsList.add(dialect);
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

                                                                    if (!sensesObject.isNull("definitions")) {

                                                                        JSONArray definitionsArray = sensesObject.getJSONArray("definitions");
                                                                        wordResultsString.append("\n").append(sa + 1).append(". ").append(context.getResources().getString(R.string.definitions)).append("\n\n");

                                                                        for (int da = 0; da < definitionsArray.length(); da++) {

                                                                            if (!definitionsArray.isNull(da)) {
                                                                                String definition = definitionsArray.getString(da);
                                                                                wordResultsString.append(da + 1).append(". ").append(definition).append("\n");
                                                                            }

                                                                        }

                                                                    }

                                                                    if (!sensesObject.isNull("domainClasses")) {

                                                                        JSONArray domainClassesArray = sensesObject.getJSONArray("domainClasses");
                                                                        wordResultsString.append("\n").append(context.getResources().getString(R.string.domainClasses)).append("\n\n");

                                                                        for (int dca = 0; dca < domainClassesArray.length(); dca++) {

                                                                            if (!domainClassesArray.isNull(dca)) {

                                                                                JSONObject domainClassesObject = domainClassesArray.getJSONObject(dca);

                                                                                if (!domainClassesObject.isNull("id")) {
                                                                                    String id = domainClassesObject.getString("id");
                                                                                }

                                                                                if (!domainClassesObject.isNull("text")) {
                                                                                    String text = domainClassesObject.getString("text");
                                                                                    wordResultsString.append(dca + 1).append(". ").append(text).append("\n");
                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                    if (!sensesObject.isNull("examples")) {

                                                                        JSONArray examplesArray = sensesObject.getJSONArray("examples");
                                                                        wordResultsString.append("\n").append(context.getResources().getString(R.string.examples)).append("\n\n");

                                                                        for (int exa = 0; exa < examplesArray.length(); exa++) {

                                                                            if (!examplesArray.isNull(exa)) {

                                                                                JSONObject examplesObject = examplesArray.getJSONObject(exa);

                                                                                if (!examplesObject.isNull("text")) {
                                                                                    String text = examplesObject.getString("text");
                                                                                    wordResultsString.append(exa + 1).append(". ").append(text).append("\n");
                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                    if (!sensesObject.isNull("semanticClasses")) {

                                                                        JSONArray semanticClassesArray = sensesObject.getJSONArray("semanticClasses");
                                                                        wordResultsString.append("\n").append(context.getResources().getString(R.string.semanticClasses)).append("\n\n");

                                                                        for (int sca = 0; sca < semanticClassesArray.length(); sca++) {

                                                                            if (!semanticClassesArray.isNull(sca)) {

                                                                                JSONObject semanticClassesObject = semanticClassesArray.getJSONObject(sca);

                                                                                if (!semanticClassesObject.isNull("id")) {
                                                                                    String id = semanticClassesObject.getString("id");
                                                                                }

                                                                                if (!semanticClassesObject.isNull("text")) {
                                                                                    String text = semanticClassesObject.getString("text");
                                                                                    wordResultsString.append(sca + 1).append(". ").append(text).append("\n");
                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                    if (!sensesObject.isNull("shortDefinitions")) {

                                                                        JSONArray shortDefinitionsArray = sensesObject.getJSONArray("shortDefinitions");
                                                                        wordResultsString.append("\n").append(context.getResources().getString(R.string.shortDefinitions)).append("\n\n");

                                                                        for (int sda = 0; sda < shortDefinitionsArray.length(); sda++) {

                                                                            if (!shortDefinitionsArray.isNull(sda)) {
                                                                                String shortDefinition = shortDefinitionsArray.getString(sda);
                                                                                wordResultsString.append(sda + 1).append(". ").append(shortDefinition).append("\n");
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

                                                                                if (!subsensesObject.isNull("definitions")) {

                                                                                    JSONArray definitionsArraySubSenses = sensesObject.getJSONArray("definitions");

                                                                                    for (int da = 0; da < definitionsArraySubSenses.length(); da++) {

                                                                                        if (!definitionsArraySubSenses.isNull(da)) {
                                                                                            String definitionSubSenses = definitionsArraySubSenses.getString(da);
                                                                                        }

                                                                                    }

                                                                                }

                                                                                if (!subsensesObject.isNull("domainClasses")) {

                                                                                    JSONArray domainClassesArraySubSenses = subsensesObject.getJSONArray("domainClasses");

                                                                                    for (int dcss = 0; dcss < domainClassesArraySubSenses.length(); dcss++) {

                                                                                        if (!domainClassesArraySubSenses.isNull(dcss)) {

                                                                                            JSONObject domainClassesObjectSubSenses = domainClassesArraySubSenses.getJSONObject(dcss);

                                                                                            if (!domainClassesObjectSubSenses.isNull("id")) {
                                                                                                String id = domainClassesObjectSubSenses.getString("id");
                                                                                            }

                                                                                            if (!domainClassesObjectSubSenses.isNull("text")) {
                                                                                                String text = domainClassesObjectSubSenses.getString("text");
                                                                                            }

                                                                                        }

                                                                                    }

                                                                                }

                                                                                if (!subsensesObject.isNull("examples")) {

                                                                                    JSONArray examplesArraySubSenses = subsensesObject.getJSONArray("examples");

                                                                                    for (int eass = 0; eass < examplesArraySubSenses.length(); eass++) {

                                                                                        if (!examplesArraySubSenses.isNull(eass)) {

                                                                                            JSONObject examplesObjectSubSenses = examplesArraySubSenses.getJSONObject(eass);

                                                                                            if (!examplesObjectSubSenses.isNull("text")) {
                                                                                                String text = examplesObjectSubSenses.getString("text");
                                                                                            }

                                                                                        }

                                                                                    }

                                                                                }

                                                                                if (!subsensesObject.isNull("notes")) {

                                                                                    JSONArray notesArraySubSenses = subsensesObject.getJSONArray("notes");

                                                                                    for (int nass = 0; nass < notesArraySubSenses.length(); nass++) {

                                                                                        if (!notesArraySubSenses.isNull(nass)) {

                                                                                            JSONObject notesObjectSubSenses = notesArraySubSenses.getJSONObject(nass);

                                                                                            if (!notesObjectSubSenses.isNull("text")) {
                                                                                                String text = notesObjectSubSenses.getString("text");
                                                                                            }

                                                                                            if (!notesObjectSubSenses.isNull("type")) {
                                                                                                String type = notesObjectSubSenses.getString("type");
                                                                                            }

                                                                                        }

                                                                                    }

                                                                                }

                                                                                if (!subsensesObject.isNull("shortDefinitions")) {

                                                                                    JSONArray shortDefinitionsArraySubSenses = subsensesObject.getJSONArray("shortDefinitions");

                                                                                    for (int sdass = 0; sdass < shortDefinitionsArraySubSenses.length(); sdass++) {

                                                                                        if (!shortDefinitionsArraySubSenses.isNull(sdass)) {
                                                                                            String shortDefinition = shortDefinitionsArraySubSenses.getString(sdass);
                                                                                        }

                                                                                    }

                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                    if (!sensesObject.isNull("synonyms")) {

                                                                        JSONArray synonymsArray = sensesObject.getJSONArray("synonyms");
                                                                        wordResultsString.append("\n").append(context.getResources().getString(R.string.synonyms)).append("\n\n");

                                                                        StringBuilder synonymBuilder = new StringBuilder();

                                                                        for (int sna = 0; sna < synonymsArray.length(); sna++) {

                                                                            if (!synonymsArray.isNull(sna)) {

                                                                                JSONObject synonymsObject = synonymsArray.getJSONObject(sna);

                                                                                if (!synonymsObject.isNull("language")) {
                                                                                    String language = synonymsObject.getString("language");
                                                                                }

                                                                                if (!synonymsObject.isNull("text")) {
                                                                                    String synonym = synonymsObject.getString("text");

                                                                                    if (sna < (synonymsArray.length() - 1)) {
                                                                                        synonymBuilder.append(synonym).append(", ");
                                                                                    } else {
                                                                                        synonymBuilder.append(synonym).append(".");
                                                                                    }

                                                                                }

                                                                            }

                                                                        }

                                                                        wordResultsString.append(synonymBuilder).append("\n");

                                                                    }

                                                                    if (!sensesObject.isNull("thesaurusLinks")) {

                                                                        JSONArray thesaurusLinksArray = sensesObject.getJSONArray("thesaurusLinks");

                                                                        for (int tla = 0; tla < thesaurusLinksArray.length(); tla++) {

                                                                            if (!thesaurusLinksArray.isNull(tla)) {

                                                                                JSONObject thesaurusLinksObject = thesaurusLinksArray.getJSONObject(tla);

                                                                                if (!thesaurusLinksObject.isNull("entry_id")) {
                                                                                    String entryId = thesaurusLinksObject.getString("entry_id");
                                                                                }

                                                                                if (!thesaurusLinksObject.isNull("sense_id")) {
                                                                                    String senseId = thesaurusLinksObject.getString("sense_id");
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

                                            if (!lexicalEntriesObject.isNull("phrases")) {

                                                JSONArray phrasesArray = lexicalEntriesObject.getJSONArray("phrases");
                                                wordResultsString.append("\n").append(context.getResources().getString(R.string.phrases)).append("\n\n");

                                                for (int pa = 0; pa < phrasesArray.length(); pa++) {

                                                    if (!phrasesArray.isNull(pa)) {

                                                        JSONObject phrasesObject = phrasesArray.getJSONObject(pa);

                                                        if (!phrasesObject.isNull("id")) {
                                                            String id = phrasesObject.getString("id");
                                                        }

                                                        if (!phrasesObject.isNull("text")) {
                                                            String text = phrasesObject.getString("text");
                                                            wordResultsString.append(pa + 1).append(". ").append(text).append("\n");
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

        return wordResultsString.toString();
    }

    public static void refreshAllLists() {

        wordTypeList.clear();
        etymologiesList.clear();
        audioFileList.clear();
        lexicalCategoryList.clear();
        phoneticSpellingList.clear();
        dialectsList.clear();

    }

}
