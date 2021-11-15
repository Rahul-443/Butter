package com.appindia.butterDictionary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;

import java.util.concurrent.ExecutionException;

public class GetDefinition {

    String word_id;
    Spannable text = null;
    String textData;
    String defJsonString;
    String entry = "entry";
    Context context;
    boolean networkAvaibilityError = false;

    public Spannable define(String word, String whichWord, Context activityContext) {

        context = activityContext;

        word_id = word.toLowerCase();

        text = getText(whichWord);

        if (networkAvaibilityError || Oxford.networkError) {
            text = new SpannableString(context.getResources().getString(R.string.requestToCheckNetConnection));
        } else if (text == null) {
            text = new SpannableString(context.getResources().getString(R.string.nswf));
        }

        return text;
    }

    public Spannable getText(String ww) {

        Spannable formattedText = null;

        try {

            if (isNetworkAvailable()) {

                defJsonString = new Oxford().execute(word_id, entry).get();

                TrySimilarWords trySimilarWords = new TrySimilarWords();

                if (ww != null) {

                    if (ww.equals(context.getResources().getString(R.string.wtd))) {

                        if (defJsonString != null) {

                            formattedText = getFormattedText(defJsonString, ww);

                        } else if (isNetworkAvailable() && !Oxford.networkError) {

                            defJsonString = new Word(word_id, context).solveJson();

                            if (!defJsonString.equals("")) { //here stringbuilder is returning an empty string.

                                formattedText = new FormatText().getformattedText(ww, "word",  defJsonString, context);

                            } else if (isNetworkAvailable() && !Oxford.networkError) {

                                defJsonString = trySimilarWords.getlemmas(word_id);

                                if (defJsonString != null) {

                                    formattedText = getFormattedText(defJsonString, ww);

                                }

                            } else {

                                networkAvaibilityError = true;

                            }

                        } else {

                            networkAvaibilityError = true;

                        }

                    }

                } else {

                    if (defJsonString != null) {

                        formattedText = getFormattedText(defJsonString, null);

                    } else if (isNetworkAvailable() && !Oxford.networkError) {

                        defJsonString = new Word(word_id, context).solveJson();

                        if (!defJsonString.equals("")) { //here stringbuilder is returning an empty string.

                            formattedText = new FormatText().getformattedText(null, "word", defJsonString, context);

                        } else if (isNetworkAvailable() && !Oxford.networkError) {

                            defJsonString = trySimilarWords.getlemmas(word_id);

                            if (defJsonString != null) {

                                formattedText = getFormattedText(defJsonString, null);

                            }

                        } else {

                            networkAvaibilityError = true;

                        }

                    } else {

                        networkAvaibilityError = true;

                    }

                }

            } else {

                networkAvaibilityError = true;

            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return formattedText;
    }

    public Spannable getFormattedText(String djString, String ww) {

        Spannable ft;
        JsonToText jsonToText = new JsonToText(djString, context);

        if (ww != null) {

            textData = jsonToText.solveJson(ww);
            ft = new FormatText().getformattedText(ww, "entry", textData, context);

        } else {

            textData = jsonToText.solveJson(null);
            ft = new FormatText().getformattedText(null, "entry", textData, context);

        }

        return ft;
    }

    public boolean isNetworkAvailable() {

        boolean connected;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

}
